package cn.granitech.business.service.database;

import cn.granitech.autoconfig.DatabaseConfig;
import cn.granitech.business.service.BaseService;
import cn.granitech.exception.ServiceException;
import cn.granitech.util.JsonHelper;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.pojo.Pagination;
import cn.granitech.variantorm.pojo.UploadItem;
import cn.granitech.web.pojo.UploadDir;
import cn.granitech.web.pojo.application.SystemSetting;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.system.OsInfo;
import cn.hutool.system.SystemUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.nio.file.Paths;
import java.util.*;

@Service
public class DatabaseService extends BaseService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Resource
    DatabaseConfig databaseConfig;
    @Resource
    UploadDir uploadDir;
    @Resource
    SystemSetting systemSetting;

    public DatabaseService() {
    }

    @Scheduled(
            cron = "0 0 3 * * ? "
    )
    public void autoBackupDatabase() {
        if (this.systemSetting.getAutoBackup() != null && this.systemSetting.getAutoBackup()) {
            String backupCycle = this.systemSetting.getBackupCycle();
            int backupCycleNum = StringUtils.isNotBlank(backupCycle) && NumberUtil.isNumber(backupCycle) ? NumberUtil.toBigDecimal(backupCycle).intValueExact() : 1;
            if (DateUtil.date().getTime() / 1000L / 60L / 60L / 24L % (long) backupCycleNum == 0L) {
                this.log.info("开始自动备份.............");
                String backupOverdueDay = this.systemSetting.getBackupOverdueDay();
                backupOverdueDay = StringUtils.isBlank(backupOverdueDay) ? "30" : backupOverdueDay;
                this.callerContext.setCallerId("0000021-00000000000000000000000000000001");
                this.backupDatabase();
                List<EntityRecord> entityRecords = super.queryListRecord("BackupDatabase", "state = 1 AND overdue = 0 AND DATEDIFF(NOW(),createdOn) > " + backupOverdueDay, null, null, new Pagination(1, 50), "backupFile", "backupDatabaseId");
                this.log.info("自动备份完成");
                if (!CollUtil.isEmpty(entityRecords)) {

                    for (EntityRecord entityRecord : entityRecords) {
                        List<UploadItem> backupFiles = entityRecord.getFieldValue("backupFile");

                        for (UploadItem uploadItem : backupFiles) {
                            String path = uploadItem.getUrl().substring("/file/get".length());
                            File file = new File(this.uploadDir.getFileDir() + path);
                            if (file.exists()) {
                                file.delete();
                            }
                        }

                        entityRecord.setFieldValue("overdue", true);
                        entityRecord.setFieldValue("backupFile", null);
                        super.updateRecord(entityRecord);
                    }

                    this.log.info("清除过期备份完成");
                }
            }
        }
    }

    public void backupDatabase() {
        String database = null;
        boolean state = true;
        String errorLog = null;
        String backupSqlPath;
        try {
            String[] connection = getConnectionByUrl(this.databaseConfig.getUrl());
            String host = connection[0];
            String port = connection[1];
            database = connection[2];
            String username = this.databaseConfig.getUsername();
            String password = this.databaseConfig.getPassword();
            String BACKUP_SQL_PATH = "/backup/";
            File directory = new File(Paths.get(this.uploadDir.getFileDir(), BACKUP_SQL_PATH).toString());
            if (!directory.exists()) {
                directory.mkdirs();
            }
            String dumpPath = getDumpPath();
            backupSqlPath = BACKUP_SQL_PATH + database + DateUtil.format(new Date(), DatePattern.PURE_DATETIME_FORMAT) + ".sql";
            String sqlPath = Paths.get(this.uploadDir.getFileDir(), backupSqlPath).toString();
            String command = "%s -h %s -P %s -u %s -p%s mysql -B %s --default-character-set=utf8 --opt --extended-insert=true --hex-blob -R -x> %s";
            ProcessBuilder builder = new ProcessBuilder();
            OsInfo osInfo = SystemUtil.getOsInfo();
            if (osInfo.isWindows()) {
                command = String.format(command, dumpPath, host, port, username, password, database, sqlPath);
                builder.command("cmd.exe", "/c", command);
            } else if (osInfo.isLinux()) {
                command = String.format(command, dumpPath, host, port, username, password, database, sqlPath);
                builder.command("/bin/sh", "-c", command);
            } else if (osInfo.isMac()) {
                command = String.format("%s %s -h %s -P %s -u %s -p%s        --default-character-set=utf8        --extended-insert=true        --opt --hex-blob        -R        -x >%s", dumpPath, database, host, port, username, password, sqlPath);
                builder.command(command);
            }
            this.log.info("Backup Database command:{}", command);
            builder.redirectErrorStream(true);
            if (builder.start().waitFor() != 0) {
                errorLog = "Backup failed";
                this.log.info(errorLog);
                state = false;
            }
            EntityRecord entityRecord = this.pm.newRecord("BackupDatabase");
            entityRecord.setFieldValue("database", database);
            entityRecord.setFieldValue("state", state);
            entityRecord.setFieldValue("errorLog", errorLog);
            entityRecord.setFieldValue("overdue", null);
            if (state) {
                List<Map<String, String>> backupFile = new ArrayList<>();
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("name", database + "备份.sql");
                hashMap.put("url", "/file/get/" + backupSqlPath);
                backupFile.add(hashMap);
                entityRecord.setFieldValue("backupFile", JsonHelper.writeObjectAsString(backupFile));
            }
            super.saveOrUpdateRecord(null, entityRecord);
        } catch (Exception e) {
            try {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                errorLog = sw.toString();
            } catch (Exception e2) {
                this.log.error("获取数据库备份异常信息失败", e2);
            }
            this.log.info("数据库备份异常", e);
            EntityRecord entityRecord2 = this.pm.newRecord("BackupDatabase");
            entityRecord2.setFieldValue("database", database);
            entityRecord2.setFieldValue("state", null);
            entityRecord2.setFieldValue("errorLog", errorLog);
            entityRecord2.setFieldValue("overdue", null);
            super.saveOrUpdateRecord(null, entityRecord2);
        } catch (Throwable th) {
            EntityRecord entityRecord3 = this.pm.newRecord("BackupDatabase");
            entityRecord3.setFieldValue("database", database);
            entityRecord3.setFieldValue("state", null);
            entityRecord3.setFieldValue("errorLog", errorLog);
            entityRecord3.setFieldValue("overdue", null);
            super.saveOrUpdateRecord(null, entityRecord3);
            throw th;
        }
        throw new ServiceException("数据库备份失败");
    }

    private String getDumpPath() throws IOException {
        OsInfo osInfo = SystemUtil.getOsInfo();
        String dumpPath;
        if (osInfo.isWindows()) {
            dumpPath = "sql/backup/mysqldump.exe";
        } else if (osInfo.isLinux()) {
            dumpPath = "sql/backup/mysqldump";
        } else {
            if (!osInfo.isMac()) {
                throw new ServiceException("当前操作系统({})不支持自动备份！", osInfo.getName());
            }

            dumpPath = "sql/backup/mysqldump_mac";
        }

        File dump = new File(Paths.get(this.uploadDir.getFileDir(), "/" + dumpPath).toString());
        if (!dump.exists()) {
            (new File(dump.getParent())).mkdirs();
            InputStream inputStream = (new ClassPathResource(dumpPath)).getInputStream();
            Throwable var5 = null;

            try {

                try (FileOutputStream outputStream = new FileOutputStream(dump)) {
                    byte[] buffer = new byte[1024];

                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
            } catch (Throwable var33) {
                var5 = var33;
                throw var33;
            } finally {
                if (var5 != null) {
                    try {
                        inputStream.close();
                    } catch (Throwable var29) {
                        var5.addSuppressed(var29);
                    }
                } else {
                    inputStream.close();
                }

            }

            if (osInfo.isLinux()) {
                (new ProcessBuilder()).command("/bin/sh", "-c", "chmod 744 " + dump.getPath()).redirectErrorStream(true).start();
            }
        }

        return dump.getPath();
    }

    private String[] getConnectionByUrl(String url) {
        String MYSQL_SCHEME = "mysql://";
        int begIndex = url.indexOf(MYSQL_SCHEME) + MYSQL_SCHEME.length();
        return url.substring(begIndex, url.indexOf("?", begIndex)).split(":|/");
    }
}

