package cn.granitech.variantorm.metadata.fieldtype;


import cn.granitech.variantorm.exception.DataAccessException;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.metadata.impl.ActualField;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.pojo.Field;
import cn.granitech.variantorm.pojo.IDName;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

public class ReferenceField extends ActualField {
    public void setParamValue(PreparedStatement pstmt, int index, Object value, PersistenceManager pm) {
        try {
            if (value == null) {
                pstmt.setNull(index, Types.VARCHAR);
                return;
            }
            if (value instanceof String) {
                pstmt.setString(index, (String)value);
                return;
            }
            pstmt.setString(index, value.toString());
        } catch (SQLException a) {
            throw new DataAccessException("Set id to PreparedStatement error", a);
        }
    }

    public String getName() {
        return "Reference";
    }

    public boolean hasLabel() {
        return true;
    }

    public boolean hasLabelColumn() {
        return true;
    }

    public Object fromJson(Object jsonValue) {
        if (jsonValue == null)
            return null;
        if (jsonValue instanceof ID)
            return jsonValue;
        if (jsonValue instanceof IDName)
            return ((IDName)jsonValue).getId();
        if (jsonValue instanceof HashMap)
            return new ID((String)((HashMap)jsonValue).get("id"));
        return new ID((String)jsonValue);
    }

    public void formatFieldValueOfRecord(EntityRecord record, String fieldName, Object valueObj) {
        if (valueObj == null) {
            record.setFieldValue(fieldName, null);
            record.setFieldLabel(fieldName, null);
            return;
        }
        if (valueObj instanceof IDName) {
            record.setFieldValue(fieldName, ((IDName)valueObj).getId());
            record.setFieldLabel(fieldName, ((IDName)valueObj).getName());
            return;
        }
        throw new IllegalArgumentException("invalid data format: ["+valueObj+"]");
    }

    public Object readDBValue(PersistenceManager pm, Field field, ResultSet rs, int index) {
        try {
            if (rs.wasNull())
                return null;
            String str = rs.getString(index + 1);
            return new IDName(ID.valueOf(rs.getString(index)), str);
        } catch (SQLException sQLException) {
            throw new DataAccessException("Get id from ResultSet error", sQLException);
        }
    }

    public ReferenceField() {
        super(ID.class);
    }
}
