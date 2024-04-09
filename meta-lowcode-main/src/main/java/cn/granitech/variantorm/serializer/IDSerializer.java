package cn.granitech.variantorm.serializer;

import cn.granitech.variantorm.metadata.ID;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

public class IDSerializer extends JsonSerializer<ID> {
    public void serialize(ID id, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (id != null) {
            jsonGenerator.writeString(id.toString());
        } else {
            jsonGenerator.writeNull();
        }
    }


}
