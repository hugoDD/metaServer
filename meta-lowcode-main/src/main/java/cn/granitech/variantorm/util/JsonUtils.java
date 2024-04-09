package cn.granitech.variantorm.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;

public class JsonUtils {
    public static String writeObjectAsString(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        try {
          return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException var4) {
            throw new RuntimeException((new StringBuilder()).insert(0,"对象解析失败=").append(object.toString()).toString());
        }
    }


    public static <T> T readJsonValue(String content, TypeReference<T> valueTypeRef) {
        ObjectMapper mapper=new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            return mapper.readValue(content, valueTypeRef);
        } catch (JsonProcessingException var5) {
            throw new RuntimeException((new StringBuilder()).insert(0, "对象解析失败=").append(content).toString());
        }
    }
}
