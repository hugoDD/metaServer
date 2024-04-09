package cn.granitech.variantorm.serializer;


import cn.granitech.variantorm.pojo.Entity;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.*;

public class EntitySetSerializer extends JsonSerializer<Set<Entity>> {
    public void serialize(Set<Entity> entities, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (entities != null && entities.size() > 0) {
            List<Map<String,String>> entityList = new ArrayList<>();
            entities.forEach((entity) -> {
                Map<String,String> entityMap = new LinkedHashMap<>();
                entityMap.put("name", entity.getName());
                entityMap.put("label", entity.getLabel());
                entityList.add(entityMap);
            });
            jsonGenerator.writeObject(entityList);
        } else {
            jsonGenerator.writeObject(null);
        }
    }


}
