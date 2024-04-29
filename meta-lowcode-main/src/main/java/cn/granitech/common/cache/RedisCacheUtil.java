package cn.granitech.common.cache;

import cn.granitech.util.RedisUtil;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;


public class RedisCacheUtil implements RedisUtil {

    public RedisCacheUtil(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private RedisTemplate<String, Object> redisTemplate;

    public boolean set(String key, Object value) {
        this.redisTemplate.opsForValue().set(key, value);
        return true;

    }

    public boolean set(String key, Object value, Long expireTime) {

        this.redisTemplate.opsForValue().set(key, value);
        this.redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
        return true;

    }

    public boolean getAndSet(String key, String value) {
        this.redisTemplate.opsForValue().getAndSet(key, value);
        return true;

    }

    public void remove(String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    public void removePattern(String pattern) {
        Set<String> keys = this.redisTemplate.keys(pattern);
        if (keys != null && keys.size() > 0) {
            this.redisTemplate.delete(keys);
        }
    }

    public void remove(String key) {
        if (exists(key)) {
            this.redisTemplate.delete(key);
        }
    }

    public boolean exists(String key) {
        return BooleanUtils.isTrue(this.redisTemplate.hasKey(key));
    }

    public <T> T get(String key) {
        return (T) this.redisTemplate.opsForValue().get(key);
    }

    public void hmSet(String key, Object hashKey, Object value) {
        this.redisTemplate.opsForHash().put(key, hashKey, value);
    }

    public Object hmGet(String key, Object hashKey) {
        return this.redisTemplate.opsForHash().get(key, hashKey);
    }

    public void lPush(String k, Object v) {
        this.redisTemplate.opsForList().rightPush(k, v);
    }

    public List<Object> lRange(String k, long l, long l1) {
        return this.redisTemplate.opsForList().range(k, l, l1);
    }

    public void addSet(String key, Object value) {
        this.redisTemplate.opsForSet().add(key, value);
    }

    public void removeSetAll(String key) {
        SetOperations<String, Object> set = this.redisTemplate.opsForSet();
        Set<Object> objectSet = set.members(key);
        if (objectSet != null && !objectSet.isEmpty()) {
            for (Object o : objectSet) {
                set.remove(key, o);
            }
        }
    }

    public Boolean isMember(String key, Object member) {
        return this.redisTemplate.opsForSet().isMember(key, member);
    }

    public Set<Object> setMembers(String key) {
        return this.redisTemplate.opsForSet().members(key);
    }

    public void zAdd(String key, Object value, double source) {
        this.redisTemplate.opsForZSet().add(key, value, source);
    }

    public Set<Object> rangeByScore(String key, double source, double source1) {
        return this.redisTemplate.opsForZSet().rangeByScore(key, source, source1);
    }

    public Set<Object> range(String key, Long source, Long source1) {
        return this.redisTemplate.opsForZSet().range(key, source, source1);
    }

    public Set<Object> reverseRange(String key, Long source, Long source1) {
        return this.redisTemplate.opsForZSet().reverseRange(key, source, source1);
    }
}
