package cn.granitech.autoconfig;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

@Configuration
public class JacksonConfiguration {
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Bean
    public SimpleModule simpleModule() {
        SimpleModule simpleModule = new SimpleModule();
        DateSerializer dateSerializer = new DateSerializer();
        DateTimeSerializer dateTimeSerializer = new DateTimeSerializer();
        simpleModule.addSerializer(Date.class, dateSerializer);
        simpleModule.addSerializer(java.util.Date.class, dateTimeSerializer);
        return simpleModule;
    }

    static class DateSerializer extends JsonSerializer<Date> {
        DateSerializer() {
        }

        @Override
        public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            if (date == null) {
                jsonGenerator.writeObject(null);
            } else {
                jsonGenerator.writeString(JacksonConfiguration.dateFormat.format(date));
            }
        }
    }

    static class DateTimeSerializer extends JsonSerializer<java.util.Date> {
        DateTimeSerializer() {
        }

        public void serialize(java.util.Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            if (date == null) {
                jsonGenerator.writeObject(null);
            } else {
                jsonGenerator.writeString(JacksonConfiguration.dateTimeFormat.format(date));
            }
        }
    }
}
