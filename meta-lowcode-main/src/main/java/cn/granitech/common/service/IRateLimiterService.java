package cn.granitech.common.service;

import cn.granitech.aop.annotation.VisitLimit;

public interface IRateLimiterService {
    boolean tryAcquire(String name,VisitLimit accessLimit);
}
