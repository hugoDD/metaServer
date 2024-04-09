package cn.granitech.variantorm.metadata.fieldtype;

import java.util.List;

public class TagField extends TextField {
    public String getName() {
        return "Tag";
    }

    public Object fromJson(Object jsonValue) {
        if (jsonValue == null) {
            return null;
        } else if (jsonValue instanceof List) {
            return String.join (",",(List<String>)jsonValue);
        }else if (jsonValue instanceof String) {
            return jsonValue;
        }
        throw new IllegalArgumentException("invalid data format: ["+jsonValue+"]");
    }
}
