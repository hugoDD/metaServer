package cn.granitech.variantorm.metadata.impl;


import cn.granitech.variantorm.metadata.FieldType;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.pojo.Field;

public abstract class ActualField implements FieldType {
    private final Class<?> valueClass;

    public final void setParamValue(PersistenceManager pm, Field field, ID objectId, Object value) {
        throw new UnsupportedOperationException();
    }

    protected ActualField(Class<?> valueClass) {
        if (valueClass == null) {
            throw new IllegalArgumentException("valueClass");
        } else {
            this.valueClass = valueClass;
        }
    }

    public boolean hasLabelColumn() {
        return false;
    }

    public void formatFieldValueOfRecord(EntityRecord record, String fieldName, Object valueObj) {
        record.setFieldValue(fieldName, valueObj);
    }

    public Object fromJson(Object jsonValue) {
        return jsonValue;
    }

    public Class<?> getValueClass() {
        return this.valueClass;
    }

    public boolean hasLabel() {
        return false;
    }

    public final Object readDBValue(PersistenceManager pm, Field field, ID objectId) {
        throw new UnsupportedOperationException();
    }

    public boolean isVirtual() {
        return false;
    }
}
