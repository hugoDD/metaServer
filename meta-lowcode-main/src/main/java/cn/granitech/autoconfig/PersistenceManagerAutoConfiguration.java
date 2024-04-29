package cn.granitech.autoconfig;

import cn.granitech.business.query.QueryHelper;
import cn.granitech.business.service.QueryCacheImpl;
import cn.granitech.util.RedisUtil;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.persistence.impl.PersistenceManagerImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;

@Configuration
@EnableConfigurationProperties
public class PersistenceManagerAutoConfiguration {

    @Resource
    RedisUtil redisUtil;

    @Bean
    public PersistenceManager persistenceManager(JdbcTemplate jdbcTemplate) {
        PersistenceManagerImpl persistenceManager = new PersistenceManagerImpl(jdbcTemplate, new QueryHelper());
        persistenceManager.setQueryCache(new QueryCacheImpl(this.redisUtil, persistenceManager));
        return persistenceManager;
    }
}
