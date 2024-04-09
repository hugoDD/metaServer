package cn.granitech.variantorm.serializer;

import cn.granitech.variantorm.pojo.Field;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class FieldSerializer extends JsonSerializer<Field> {
    public FieldSerializer() {
    }

    public void serialize(Field field, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (field != null) {
            Map<String,String> a = new LinkedHashMap<>();
            a.put("name", field.getName());
            a.put("label", field.getLabel());
            jsonGenerator.writeObject(a);
        } else {
            jsonGenerator.writeObject(null);
        }
    }
}
