package cn.granitech.autoconfig;

import cn.granitech.util.UpgradeScriptHelper;
import cn.granitech.variantorm.dbmapping.DBMappingManager;
import cn.granitech.variantorm.persistence.PersistenceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableConfigurationProperties
public class DBMappingManagerAutoConfiguration {
    public static final String SETTING_VALUE = "settingValue";
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Resource
    DataSource dataSource;
    @Resource
    PersistenceManager persistenceManager;

    @Bean
    public DBMappingManager dBMappingManager() throws IOException {
        upgradeDatabase();
        return new DBMappingManager(this.dataSource, this.persistenceManager.getMetadataManager(), this.persistenceManager);
    }

    public void upgradeDatabase() throws IOException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(this.dataSource);
        jdbcTemplate.execute("COMMIT;");
        try {
            jdbcTemplate.queryForMap("SHOW TABLES LIKE 't_system_setting';");
        } catch (EmptyResultDataAccessException e) {
            this.log.info("---------Initialize Database---------");
            executeUpgradeSql(jdbcTemplate, UpgradeScriptHelper.readInitializeScript());
        }
        String sqlVersion = (String) jdbcTemplate.queryForMap("SELECT settingValue FROM t_system_setting WHERE settingName = 'sqlVersion';").get("settingValue");
        Map<String, StringBuffer> scriptMap = UpgradeScriptHelper.readUpgradeScript(sqlVersion);
        List<String> versionList = scriptMap.keySet().stream().collect(Collectors.toList());
        Collections.reverse(versionList);
        for (String version : versionList) {
            this.log.info("upgrade Database:{}", version);
            executeUpgradeSql(jdbcTemplate, version, scriptMap.get(version));
            sqlVersion = version;
        }
        this.log.info("sqlVersion:{}", sqlVersion);
    }

    public void executeUpgradeSql(JdbcTemplate jdbcTemplate, String version, StringBuffer sql) {
        StringBuffer executeSql = new StringBuffer();
        executeSql.append(sql).append("\n").append(String.format("UPDATE `t_system_setting` SET `settingValue`='%s' WHERE  `settingName`='sqlVersion';", version));
        executeUpgradeSql(jdbcTemplate, executeSql);
    }

    public void executeUpgradeSql(JdbcTemplate jdbcTemplate, StringBuffer sql) {
        PlatformTransactionManager platformTransactionManager = new DataSourceTransactionManager(this.dataSource);
        TransactionStatus transactionStatus = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            jdbcTemplate.execute(sql.toString());
            platformTransactionManager.commit(transactionStatus);
        } catch (Exception e) {
            platformTransactionManager.rollback(transactionStatus);
            throw e;
        }
    }
}
