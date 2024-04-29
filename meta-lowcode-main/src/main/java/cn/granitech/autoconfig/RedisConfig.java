package cn.granitech.autoconfig;

import cn.granitech.common.cache.RedisCacheUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Value("${spring.redis.host:}")
    private String address;
    @Value("${spring.redis.cluster.nodes:}")
    private String clusterNodes;
    @Value("${spring.redis.database:}")
    private Integer database;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.port:}")
    private String port;

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        Config config = new Config();
        if (StringUtils.isNotBlank(this.clusterNodes)) {
            String[] addressList = this.clusterNodes.split(",");
            ClusterServersConfig clusterServersConfig = config.useClusterServers().setScanInterval(2000);
            int length = addressList.length;
            for (int i = 0; i < length; i++) {
                clusterServersConfig.addNodeAddress("redis://" + addressList[i]);
                if (StringUtils.isNotEmpty(this.password)) {
                    clusterServersConfig.setPassword(this.password);
                }
            }
        } else {
            config.useSingleServer().setDatabase(this.database.intValue()).setAddress("redis://" + this.address.trim() + ":" + this.port.trim());
            if (StringUtils.isNotEmpty(this.password)) {
                config.useSingleServer().setPassword(this.password);
            }
        }
        return Redisson.create(config);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public RedisCacheUtil RedisCacheUtil(RedisTemplate<String, Object> redisTemplate) {
        return new RedisCacheUtil(redisTemplate);
    }
}
