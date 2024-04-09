package cn.granitech.variantorm.serializer;

import cn.granitech.variantorm.persistence.EntityRecord;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

public class EntityRecordSerializer extends JsonSerializer<EntityRecord> {
    public void serialize(EntityRecord entityRecord, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeObject(entityRecord.getValuesMap());
    }
}
