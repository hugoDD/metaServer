package cn.granitech.util;

import cn.granitech.exception.ServiceException;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Map;

public class JsonHelper {
    private JsonHelper() {
    }

    public static void writeResponseJson(HttpServletResponse response, Object object) throws IOException {
        response.setHeader("Content-type", "text/plain;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        writer.print(new ObjectMapper().writeValueAsString(object));
        writer.close();
        response.flushBuffer();
    }

    public static String writeObjectAsString(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new ServiceException("对象解析失败=" + object.toString());
        }
    }

    public static <T> T readJsonValue(String content, TypeReference<T> valueTypeRef) {
        if (StrUtil.isBlank(content)) {
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return objectMapper.readValue(content, valueTypeRef);
        } catch (JsonProcessingException e) {
            throw new ServiceException("对象解析失败=" + content);
        }
    }

    public static <T> T readJsonValue(String content, Class<T> valueType) {
        if (StrUtil.isBlank(content)) {
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return objectMapper.readValue(content, valueType);
        } catch (JsonProcessingException e) {
            throw new ServiceException("对象解析失败=" + content);
        }
    }

    public static Map<String, Object> writeObjectAsMap(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return (Map) objectMapper.convertValue(object, Map.class);
    }
}
