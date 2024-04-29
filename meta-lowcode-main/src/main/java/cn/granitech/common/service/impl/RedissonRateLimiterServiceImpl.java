package cn.granitech.common.service.impl;

import cn.granitech.aop.annotation.VisitLimit;
import cn.granitech.common.service.IRateLimiterService;
import cn.granitech.util.IPHelper;
import cn.granitech.web.enumration.RedisKeyEnum;
import com.google.common.util.concurrent.RateLimiter;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class RedissonRateLimiterServiceImpl implements IRateLimiterService {
    private RedissonClient redissonClient;

    public RedissonRateLimiterServiceImpl(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public boolean tryAcquire(String name, VisitLimit accessLimit) {
        RRateLimiter rateLimiter = this.redissonClient.getRateLimiter(name);
        rateLimiter.trySetRate(RateType.OVERALL, accessLimit.limit(), accessLimit.sec(), RateIntervalUnit.SECONDS);
       return  rateLimiter.tryAcquire();
    }
}
