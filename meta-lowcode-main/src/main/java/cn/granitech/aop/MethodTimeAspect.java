package cn.granitech.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MethodTimeAspect {
    public static final String IGNORE = "checkStatus";
    private final Logger log = LoggerFactory.getLogger(getClass());
    private String methodName;
    private long startTime;

    @Pointcut("execution( public * cn.granitech.web.controller..*.*(..))")
    public void aopPointCut() {
    }

    @Before("aopPointCut()")
    public void doBefore(JoinPoint joinPoint) {
        this.methodName = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        this.startTime = System.currentTimeMillis();
    }

    @After("aopPointCut()")
    public void doAfter() {
        long E_time = System.currentTimeMillis() - this.startTime;
        if (!this.methodName.contains(IGNORE)) {
            this.log.info("执行 " + this.methodName + " 耗时为：" + E_time + "ms");
        }
    }

    @AfterReturning(pointcut = "aopPointCut()", returning = "object")
    public void doAfterReturning(Object object) {
    }

    @Around("aopPointCut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long end = System.currentTimeMillis();
            if (!joinPoint.toString().contains(IGNORE)) {
                this.log.info("+++++around " + joinPoint + "\tUse time : " + (end - start) + " ms!");
            }
            return result;
        } catch (Throwable e) {
            this.log.error("+++++around " + joinPoint + "\tUse time : " + (System.currentTimeMillis() - start) + " ms with exception : " + e.getMessage());
            throw e;
        }
    }
}
