package cn.granitech.variantorm.serializer;

import cn.granitech.variantorm.metadata.FieldType;
import cn.granitech.variantorm.metadata.fieldtype.FieldTypes;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class FieldTypeDeserializer extends JsonDeserializer<FieldType> {
    public FieldTypeDeserializer() {
    }

    public FieldType deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String text=jsonParser.getText();
        return StringUtils.isNotBlank(text) ? FieldTypes.getType(text) : null;
    }
}
