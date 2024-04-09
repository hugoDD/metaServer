package cn.granitech.util;

import cn.granitech.exception.ServiceException;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.ServletRequest;

public class ServletHelper {
    public static String sortAndJoinParams(Map<String, String[]> parameterMap) {
        Map<String, String> sortedMap = new TreeMap<>();
        for (Map.Entry<String, String[]> e : parameterMap.entrySet()) {
            String[] vv = e.getValue();
            sortedMap.put(e.getKey(), (vv == null || vv.length == 0) ? null : vv[0]);
        }
        StringBuilder sign = new StringBuilder();
        for (Map.Entry<String, String> e : sortedMap.entrySet())
            sign.append(e.getKey()).append('=').append(e.getValue()).append('&');
        return sign.toString();
    }

    public static String getRequestBody(ServletRequest request) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = request.getReader()) {
            if (br != null) {
                String line;
                while ((line = br.readLine()) != null)
                    sb.append(line);
            }
        } catch (IOException ex) {
            throw new ServiceException(ex.getMessage(), ex);
        }
        return sb.toString();
    }
}
