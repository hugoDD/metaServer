package cn.granitech.variantorm.metadata.fieldtype;

import cn.granitech.variantorm.exception.DataAccessException;
import cn.granitech.variantorm.metadata.impl.ActualField;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.pojo.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class BooleanField extends ActualField {
    public Object fromJson(Object jsonValue) {
        if (jsonValue == null)
            return null;
        if (jsonValue instanceof String) {
            if (Objects.equals("1",jsonValue) || Objects.equals("true",jsonValue)) {
                return Boolean.TRUE;
            }
            if ("null".equalsIgnoreCase((String)jsonValue) || "".equals(((String)jsonValue).trim()))
                return null;
        }
        if (jsonValue instanceof Integer) {
            if ((Integer) jsonValue == -1)
                return null;
            return ((Integer) jsonValue == 1);
        }
        return jsonValue;
    }

    public void setParamValue(PreparedStatement pstmt, int index, Object value, PersistenceManager pm) {
        try {
            if (value == null) {
                pstmt.setNull(index, 4);
            }
            pstmt.setInt(index, (Boolean) value ? 1 : 0);
        } catch (SQLException a) {
            throw new DataAccessException("Set boolean to PreparedStatement error", a);
        }
    }

    public BooleanField() {
        super(Boolean.class);
    }

    public String getName() {
        return "Boolean";
    }

    public Object readDBValue(PersistenceManager pm, Field field, ResultSet rs, int index) {
        try {
            int a = rs.getInt(index);
            return rs.wasNull() ? null : (a != 0);
        } catch (SQLException sQLException) {
            throw new DataAccessException("Get boolean from ResultSet error", sQLException);
        }
    }
}
