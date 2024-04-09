package cn.granitech.interceptor;

import cn.granitech.aop.MethodTimeAspect;
import cn.granitech.util.JsonHelper;
import cn.granitech.util.LoginInterceptorHelper;
import cn.granitech.web.constant.SessionNames;
import cn.granitech.web.pojo.ResponseBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    CallerContext callerContext;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getServletPath();
        if (LoginInterceptorHelper.check(path)) {
            return true;
        }
        ResponseBean<String> rBean = new ResponseBean<>(403, "请登录", "用户未登录", "");
        String uId = (String) request.getSession().getAttribute(SessionNames.LoginUserId);
        if (!path.contains(MethodTimeAspect.IGNORE)) {
            System.err.println("path: " + path);
            System.err.println("uId 222: " + uId);
        }
        if (StringUtils.isBlank(uId)) {
            JsonHelper.writeResponseJson(response, rBean);
            return false;
        }
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(SessionNames.LoginUserId) == null) {
            JsonHelper.writeResponseJson(response, rBean);
            return false;
        } else if (!uId.equalsIgnoreCase((String) session.getAttribute(SessionNames.LoginUserId))) {
            JsonHelper.writeResponseJson(response, rBean);
            return false;
        } else {
            this.callerContext.setCallerId(uId);
            return true;
        }
    }

    private String getCookieUid(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if ("uid".equalsIgnoreCase(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private String getHeaderUserId(HttpServletRequest request) {
        return request.getHeader("ruId");
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        this.callerContext.cleanThreadLocal();
    }
}
