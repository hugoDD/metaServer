package cn.granitech.variantorm.metadata.impl;


import cn.granitech.variantorm.metadata.FieldType;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.persistence.PersistenceManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public abstract class VirtualField implements FieldType {
    private final Class<?> valueClass;

    public final boolean hasLabelColumn() {
        return false;
    }

    public boolean isVirtual() {
        return true;
    }

    protected VirtualField(Class<?> valueClass) {
        if (valueClass == null) {
            throw new IllegalArgumentException("valueClass");
        } else {
            this.valueClass = valueClass;
        }
    }

    public Object fromJson(Object jsonValue) {
        throw new UnsupportedOperationException();
    }

    public Object getValue(PersistenceManager pm, String entityName, String fieldName, ResultSet rs, int index) {
        throw new UnsupportedOperationException();
    }

    public final boolean hasLabel() {
        return true;
    }

    public final void setParamValue(PreparedStatement stmt, int index, Object value, PersistenceManager pm) {
        throw new UnsupportedOperationException();
    }

    public Class<?> getValueClass() {
        return this.valueClass;
    }

    public void formatFieldValueOfRecord(EntityRecord record, String fieldName, Object valueObj) {
        throw new UnsupportedOperationException();
    }
}
