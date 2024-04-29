package cn.granitech.common.service.impl;

import cn.granitech.aop.annotation.VisitLimit;
import cn.granitech.common.service.IRateLimiterService;
import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class RateLimiterServiceImpl implements IRateLimiterService {
    private final static java.util.Map<String,RateLimiter> map = new ConcurrentHashMap<>();
    @Override
    public boolean tryAcquire(String name, VisitLimit accessLimit) {
        if(!map.containsKey(name)){
            RateLimiter  rateLimiter = RateLimiter.create(accessLimit.limit(),accessLimit.sec(), TimeUnit.SECONDS);
           map.put(name,rateLimiter);
        }
        RateLimiter rateLimiter = map.get(name);

        return rateLimiter.tryAcquire();
    }
}
