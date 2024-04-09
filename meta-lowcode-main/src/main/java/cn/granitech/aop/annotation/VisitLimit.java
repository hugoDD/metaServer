package cn.granitech.aop.annotation;

import java.lang.annotation.*;

/**
 * @author ly-dourx
 */
@Inherited
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface VisitLimit {
    int limit() default 5;

    long sec() default 2;
}
