package cn.granitech.variantorm.serializer;

import cn.granitech.variantorm.metadata.ID;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class IDDeserializer extends JsonDeserializer<ID> {
    public ID deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String text=jsonParser.getText();
        return StringUtils.isNotBlank(text) && text.length() == 40 ? ID.valueOf(text) : null;
    }

}
