package cn.granitech.interceptor;

import cn.granitech.business.service.UserService;
import cn.granitech.util.SpringHelper;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class SessionListener implements HttpSessionListener {
    public void sessionDestroyed(HttpSessionEvent se) {
        SpringHelper.getBean(UserService.class).updateLoginLog(se.getSession().getId(), "登录超时");
    }

    public void sessionCreated(HttpSessionEvent se) {
    }
}
