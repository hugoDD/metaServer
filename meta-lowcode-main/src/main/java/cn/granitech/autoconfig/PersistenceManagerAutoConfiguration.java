package cn.granitech.autoconfig;

import cn.granitech.business.query.QueryHelper;
import cn.granitech.business.service.QueryCacheImpl;
import cn.granitech.util.RedisUtil;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.persistence.impl.PersistenceManagerImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties
public class PersistenceManagerAutoConfiguration {
    @Resource
    DataSource dataSource;
    @Resource
    RedisUtil redisUtil;

    /* access modifiers changed from: package-private */
    @Bean
    public PersistenceManager persistenceManager() {
        PersistenceManagerImpl persistenceManager = new PersistenceManagerImpl(this.dataSource, new QueryHelper());
        persistenceManager.setQueryCache(new QueryCacheImpl(this.redisUtil, persistenceManager));
        return persistenceManager;
    }
}
