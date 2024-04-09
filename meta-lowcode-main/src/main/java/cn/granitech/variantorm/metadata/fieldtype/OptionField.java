package cn.granitech.variantorm.metadata.fieldtype;


import cn.granitech.variantorm.exception.DataAccessException;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.pojo.Field;
import cn.granitech.variantorm.pojo.OptionModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class OptionField extends IntegerField {
    public boolean hasLabelColumn() {
        return true;
    }

    public OptionField() {
    }

    public Object readDBValue(PersistenceManager pm, Field field, ResultSet rs, int index) {
        try {
            return rs.wasNull() ? null : pm.getQueryCache().getOption(field.getOwner().getName(), field.getName(), rs.getInt(index));
        } catch (SQLException e) {
            throw new DataAccessException("Get integer from ResultSet error", e);
        }
    }

    public String getName() {
        return "Option";
    }

    public Object fromJson(Object jsonValue) {
        if (jsonValue == null) {
            return null;
        } else if (jsonValue instanceof Integer) {
            return jsonValue;
        } else if (jsonValue instanceof Map) {
            return ((Map)jsonValue).get("value");
        } else if (jsonValue instanceof String) {
            return "".equals(jsonValue) ? null : Integer.valueOf((String)jsonValue);
        } else {
            throw new IllegalArgumentException("invalid data format: ["+jsonValue+"]");
        }
    }

    public void formatFieldValueOfRecord(EntityRecord record, String fieldName, Object valueObj) {
        if (valueObj == null) {
            record.setFieldValue(fieldName, null);
            record.setFieldLabel(fieldName, null);
        } else if (valueObj instanceof OptionModel) {
            record.setFieldValue(fieldName, ((OptionModel)valueObj).getValue());
            record.setFieldLabel(fieldName, ((OptionModel)valueObj).getLabel());
        } else {
            throw new IllegalArgumentException("invalid data format: ["+valueObj+"]");
        }
    }

    public boolean hasLabel() {
        return true;
    }
}
