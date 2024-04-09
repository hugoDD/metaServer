package cn.granitech.variantorm.serializer;

import cn.granitech.variantorm.pojo.Entity;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class EntitySerializer extends JsonSerializer<Entity> {
    public void serialize(Entity entity, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (entity != null) {
            Map<String,String> map=new LinkedHashMap<>();
            map.put("name", entity.getName());
            map.put("label", entity.getLabel());
            jsonGenerator.writeObject(map);
        } else {
            jsonGenerator.writeObject(null);
        }
    }
}
