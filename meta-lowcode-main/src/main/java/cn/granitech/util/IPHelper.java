package cn.granitech.util;

import javax.servlet.http.HttpServletRequest;

public class IPHelper {
    public static String getRequestIp(HttpServletRequest request) {
        try {
            String ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
            }
            if (ipAddress == null || ipAddress.length() <= 15 || ipAddress.indexOf(",") <= 0) {
                return ipAddress;
            }
            return ipAddress.substring(0, ipAddress.indexOf(","));
        } catch (Exception e) {
            return "";
        }
    }
}
