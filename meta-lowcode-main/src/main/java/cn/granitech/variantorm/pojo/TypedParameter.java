package cn.granitech.variantorm.pojo;

import cn.granitech.variantorm.metadata.FieldType;

public class TypedParameter {
    private FieldType fieldType;

    private Object value;

    public TypedParameter(FieldType fieldType, Object value) {
        this.fieldType = fieldType;
        this.value = value;
    }

    public FieldType getFieldType() {
        return this.fieldType;
    }

    public Object getValue() {
        return this.value;
    }
}
