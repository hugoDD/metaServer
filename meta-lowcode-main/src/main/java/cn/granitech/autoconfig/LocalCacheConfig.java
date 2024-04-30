package cn.granitech.autoconfig;

import cn.granitech.common.cache.LoadingCacheUtil;
import cn.granitech.common.service.IRateLimiterService;
import cn.granitech.common.service.impl.RateLimiterServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnProperty(name="spring.redis.enabled",havingValue = "false",matchIfMissing = true)
@Configuration
public class LocalCacheConfig {




    @Bean
    public LoadingCacheUtil RedisCacheUtil() {
        return new LoadingCacheUtil();
    }

    @Bean
    public IRateLimiterService rateLimiterService(){
        return new RateLimiterServiceImpl();
    }


}
