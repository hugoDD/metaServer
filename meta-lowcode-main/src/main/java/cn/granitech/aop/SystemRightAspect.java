package cn.granitech.aop;

import cn.granitech.aop.annotation.SystemRight;
import cn.granitech.exception.ServiceException;
import cn.granitech.interceptor.CallerContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author ly-dourx
 */
@Aspect
@Component
public class SystemRightAspect {
    @Resource
    CallerContext callerContext;

    @Pointcut("@annotation(cn.granitech.aop.annotation.SystemRight)")
    public void check() {
    }

    @Before("check()")
    public void digestCheck(JoinPoint joinPoint) {
        if (!this.callerContext.checkSystemRight((((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(SystemRight.class)).value())) {
            throw new ServiceException("当前用户没有权限！");
        }
    }
}
