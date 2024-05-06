package cn.granitech.autoconfig;

import cn.granitech.util.UpgradeScriptHelper;
import cn.granitech.variantorm.dbmapping.DBMappingManager;
import cn.granitech.variantorm.metadata.MetadataManager;
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
import java.io.IOException;
import java.util.*;

@Configuration
@EnableConfigurationProperties
public class DBMappingManagerAutoConfiguration {
    private final Logger log = LoggerFactory.getLogger(getClass());


    @Bean
    public DBMappingManager dBMappingManager(PersistenceManager persistenceManager) throws IOException {
        JdbcTemplate jdbcTemplate = persistenceManager.getJdbcTemplate();
        MetadataManager metadataManager = persistenceManager.getMetadataManager();
        upgradeDatabase(jdbcTemplate);
        return new DBMappingManager(jdbcTemplate, metadataManager,persistenceManager);
    }

    public void upgradeDatabase(JdbcTemplate jdbcTemplate) throws IOException {
        jdbcTemplate.execute("COMMIT;");
        try {
            jdbcTemplate.queryForMap("SHOW TABLES LIKE 't_system_setting';");
        } catch (EmptyResultDataAccessException e) {
            this.log.info("---------Initialize Database---------");
            executeUpgradeSql(jdbcTemplate, UpgradeScriptHelper.readInitializeScript());
        }
        String sqlVersion =  jdbcTemplate.queryForObject("SELECT settingValue FROM t_system_setting WHERE settingName = 'sqlVersion'",String.class);
        Map<String, StringBuffer> scriptMap = UpgradeScriptHelper.readUpgradeScript(sqlVersion);
        List<String> versionList = new ArrayList<>(scriptMap.keySet());
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
        PlatformTransactionManager platformTransactionManager = new DataSourceTransactionManager(Objects.requireNonNull(jdbcTemplate.getDataSource()));
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
