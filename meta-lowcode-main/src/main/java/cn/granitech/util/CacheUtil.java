package cn.granitech.util;

import java.util.List;
import java.util.Set;


public interface CacheUtil {


    boolean set(String key, Object value);

    boolean set(String key, Object value, Long expireTime);

    boolean getAndSet(String key, String value);

    void remove(String... keys);

    void removePattern(String pattern);

    void remove(String key);

    boolean exists(String key);

    <T> T get(String key);

    void hmSet(String key, Object hashKey, Object value);

    Object hmGet(String key, Object hashKey);

    void lPush(String k, Object v);

    List<Object> lRange(String k, long l, long l1);

    void addSet(String key, Object value);

    void removeSetAll(String key);

    Boolean isMember(String key, Object member);

    Set<Object> setMembers(String key);

    void zAdd(String key, Object value, double source);

    Set<Object> rangeByScore(String key, double source, double source1);

    Set<Object> range(String key, Long source, Long source1);

    Set<Object> reverseRange(String key, Long source, Long source1);
}
