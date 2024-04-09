package cn.granitech.interceptor;

import cn.granitech.util.IPHelper;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class SessionIPChecker {
    private static final Map<String, String> sessionIdIPMap = new HashMap<>();

    public static synchronized void addSessionIdIP(HttpServletRequest request, String sessionId) {
        synchronized (SessionIPChecker.class) {
            String requestIP = IPHelper.getRequestIp(request);
            if (StringUtils.isNotBlank(requestIP)) {
                sessionIdIPMap.put(sessionId, requestIP);
            }
        }
    }

    public static synchronized boolean checkRequestIP(HttpServletRequest request, String sessionId) {
        boolean z;
        synchronized (SessionIPChecker.class) {
            if (sessionIdIPMap.containsKey(sessionId)) {
                z = sessionIdIPMap.get(sessionId).equals(IPHelper.getRequestIp(request));
            } else {
                z = false;
            }
        }
        return z;
    }

    public static synchronized void removeExpiredSession(String sessionId) {
        synchronized (SessionIPChecker.class) {
            sessionIdIPMap.remove(sessionId);
        }
    }
}

