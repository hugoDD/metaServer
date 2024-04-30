package cn.granitech.interceptor;

import cn.granitech.aop.annotation.VisitLimit;
import cn.granitech.business.service.CrudService;
import cn.granitech.common.service.IRateLimiterService;
import cn.granitech.exception.ServiceException;
import cn.granitech.util.IPHelper;
import cn.granitech.util.ServletHelper;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.web.enumration.RedisKeyEnum;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
public class ApiAuthInterceptor implements HandlerInterceptor {
    private static final String APPID = "appId";
    private static final String APP_SECRET = "appSecret";
    private static final String BIND_IPS = "bindIps";
    private static final String BIND_USER = "bindUser";
    private static final String ENTITY_NAME = "MetaApi";
    private static final String SIGN = "sign";
    private static final String TIMESTAMP = "timestamp";
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Resource
    CallerContext callerContext;
    @Resource
    CrudService crudService;

    @Resource
    IRateLimiterService rateLimiterService;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        checkLimit(handler, request);
        String requestBody = ServletHelper.getRequestBody(request);
        String appId = request.getHeader(APPID);
        String timeStr = request.getHeader(TIMESTAMP);
        String sign = request.getHeader(SIGN);
        String requestURL = request.getRequestURL().toString();
        this.log.info("RequestHeader - AppId: " + appId + ", Sign: " + sign);
        this.log.info("Request URL: " + requestURL);
        if (appId == null || sign == null || timeStr == null) {
            throw new ServiceException("Missing appId or sign or timestamp in request headers");
        }
        long userTime = Long.parseLong(timeStr);
        long expireTime = DateUtil.offsetSecond(DateUtil.date(), 15).getTime();
        if (userTime >= DateUtil.date().getTime() || userTime <= expireTime) {
            EntityRecord apiRecord = this.crudService.queryOneRecord(ENTITY_NAME, String.format("%s = '%s'", APPID, appId), null, null);
            if (apiRecord == null) {
                throw new ServiceException("appId application information not found");
            }
            String appSecret = apiRecord.getFieldValue(APP_SECRET);
            String bindIps = apiRecord.getFieldValue(BIND_IPS);
            this.callerContext.setCallerId(((ID) apiRecord.getFieldValue(BIND_USER)).getId());
            if (StrUtil.isNotBlank(bindIps)) {
                String clientIp = IPHelper.getRequestIp(request);
                if (!bindIps.contains(clientIp)) {
                    throw new ServiceException("Client ip not in whitelist : " + clientIp);
                }
            }
            String sortParams = ServletHelper.sortAndJoinParams(request.getParameterMap());
            StringBuilder sign2 = new StringBuilder();
            sign2.append(sortParams);
            if (StrUtil.isNotBlank(requestBody)) {
                sign2.append(requestBody);
            }
            sign2.append(appId).append(".").append(appSecret);
            String sign2sign = DigestUtil.sha256Hex(sign2.toString());
            this.log.info("sign result:" + sign2sign);
            if (sign.equals(sign2sign)) {
                return true;
            }
            throw new ServiceException("Invalid [sign] : " + sign);
        }
        throw new ServiceException("The timestamp has expired");
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        this.callerContext.cleanThreadLocal();
    }

    private boolean checkLimit(Object handler, HttpServletRequest request) {
        if (handler instanceof HandlerMethod) {
            Method method = ((HandlerMethod) handler).getMethod();
            VisitLimit accessLimit = method.getAnnotation(VisitLimit.class);
            if (method.isAnnotationPresent(VisitLimit.class) && accessLimit != null) {
                String name = RedisKeyEnum.TMP_CACHE.getKey(IPHelper.getRequestIp(request) + request.getRequestURI());
                if (!rateLimiterService.tryAcquire(name, accessLimit)) {
                    throw new ServiceException("请求过于频繁");
                }

//                RRateLimiter rateLimiter = this.redissonClient.getRateLimiter(RedisKeyEnum.TMP_CACHE.getKey(IPHelper.getRequestIp(request) + request.getRequestURI()));
//                rateLimiter.trySetRate(RateType.OVERALL, accessLimit.limit(), accessLimit.sec(), RateIntervalUnit.SECONDS);
//                if (!rateLimiter.tryAcquire()) {
//                    throw new ServiceException("请求过于频繁");
//                }
            }
        }
        return true;
    }
}
