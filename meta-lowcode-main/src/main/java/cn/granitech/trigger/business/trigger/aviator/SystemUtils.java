package cn.granitech.trigger.business.trigger.aviator;

import cn.granitech.interceptor.CallerContext;
import cn.granitech.util.SpringHelper;
import com.googlecode.aviator.annotation.Import;
import com.googlecode.aviator.annotation.ImportScope;

import java.util.Date;


@Import(ns = "system", scopes = {ImportScope.Static})
public class SystemUtils {
    private static final CallerContext callerContext = SpringHelper.getBean(CallerContext.class);


    public static String callerId() {
        return callerContext.getCallerId();
    }

    public static String departmentId() {
        return callerContext.getDepartmentId();
    }

    public static Date now() {
        return new Date();
    }
}



