package cn.granitech.common.cache;

import cn.granitech.util.RedisUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


public class LoadingCacheUtil implements RedisUtil {


   private final Cache<String, Object> cache = CacheBuilder.newBuilder()
            .maximumSize(10000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();

    public boolean set(String key, Object value) {
        cache.put(key,value);
        return true;

    }

    public boolean set(String key, Object value, Long expireTime) {
        cache.put(key, value);

        return true;

    }

    public boolean getAndSet(String key, String value) {
        synchronized (key){
            cache.put(key,value);
        }

        return true;

    }

    public void remove(String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    public void removePattern(String pattern) {
        Set<String> keys = cache.asMap().keySet().stream().filter(key->key.matches(pattern)).collect(Collectors.toSet());
        for (String key : keys) {
            cache.invalidate(key);
        }

    }

    public void remove(String key) {
        if (exists(key)) {
            cache.invalidate(key);
        }
    }

    public boolean exists(String key) {
        return  cache.asMap().containsKey(key);
    }

    public <T> T get(String key) {

        return (T) cache.getIfPresent(key);
    }

    public void hmSet(String key, Object hashKey, Object value) {
        Object present = cache.getIfPresent(key);
        if(present == null){
            Map<Object,Object> map = new HashMap<>();
            map.put(hashKey,value);
            cache.put(key,value);
        }else if(present instanceof Map) {
            Map<Object,Object> map =(Map<Object,Object>) present;
            map.put(hashKey,value);
            cache.put(key,map);
        }
    }

    public Object hmGet(String key, Object hashKey) {
        Object value = cache.getIfPresent(key);
        if(value instanceof Map){
           return  ((Map)value).get(hashKey);
        }
       return null;
    }

    public void lPush(String k, Object v) {
    }

    public List<Object> lRange(String k, long l, long l1) {
        return  new ArrayList<>();
    }

    public void addSet(String key, Object value) {
    }

    public void removeSetAll(String key) {

    }

    public Boolean isMember(String key, Object member) {
       return true;
    }

    public Set<Object> setMembers(String key) {
       return  null;
    }

    public void zAdd(String key, Object value, double source) {

    }

    public Set<Object> rangeByScore(String key, double source, double source1) {
      return null;
    }

    public Set<Object> range(String key, Long source, Long source1) {
      return null;
    }

    public Set<Object> reverseRange(String key, Long source, Long source1) {
       return null;
    }
}
