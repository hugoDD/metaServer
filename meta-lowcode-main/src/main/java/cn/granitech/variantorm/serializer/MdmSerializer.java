package cn.granitech.variantorm.serializer;

import cn.granitech.variantorm.metadata.MetadataManager;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class MdmSerializer extends JsonSerializer<MetadataManager> {


    public void serialize(MetadataManager metadataManager, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeObject(null);
    }
}
