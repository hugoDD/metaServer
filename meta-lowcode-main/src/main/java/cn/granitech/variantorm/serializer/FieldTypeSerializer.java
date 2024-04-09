package cn.granitech.variantorm.serializer;


import cn.granitech.variantorm.metadata.FieldType;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class FieldTypeSerializer extends JsonSerializer<FieldType> {
    public void serialize(FieldType fieldType, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (fieldType != null) {
            jsonGenerator.writeString(fieldType.getName());
        } else {
            jsonGenerator.writeObject(null);
        }
    }

    public FieldTypeSerializer() {
    }
}
