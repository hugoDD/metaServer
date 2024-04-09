package cn.granitech.variantorm.metadata.fieldtype;

import cn.granitech.variantorm.exception.DataAccessException;
import cn.granitech.variantorm.metadata.impl.ActualField;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.pojo.Field;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class IntegerField extends ActualField {
    public IntegerField() {
        super(Integer.class);
    }

    public Object readDBValue(PersistenceManager pm, Field field, ResultSet rs, int index) {
        try {
            int value = rs.getInt(index);
            return rs.wasNull() ? null : value;
        } catch (SQLException sQLException) {
            throw new DataAccessException("Get integer from ResultSet error", sQLException);
        }
    }

    public Object fromJson(Object jsonValue) {
        if (jsonValue == null)
            return null;
        if (jsonValue instanceof BigDecimal)
            return ((BigDecimal) jsonValue).setScale(0, RoundingMode.HALF_UP).intValue();
        if (jsonValue instanceof Long)
            return ((Long) jsonValue).intValue();
        if (jsonValue instanceof String) {
            if (StringUtils.isBlank((String)jsonValue))
                return null;
            return new Integer(((String)jsonValue).replaceAll(",", ""));
        }
        if (jsonValue instanceof Integer)
            return jsonValue;
        throw new IllegalArgumentException("invalid data format: ["+jsonValue+"]");
    }

    public void setParamValue(PreparedStatement pstmt, int index, Object value, PersistenceManager pm) {
        try {
            if (value == null) {
                pstmt.setNull(index, Types.INTEGER);
                return;
            }
            pstmt.setInt(index, (Integer) value);
        } catch (SQLException a) {
            throw new DataAccessException("Set integer to PreparedStatement error", a);
        }
    }

    public String getName() {
        return "Integer";
    }
}
