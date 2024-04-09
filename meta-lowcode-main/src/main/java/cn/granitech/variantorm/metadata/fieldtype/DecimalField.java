package cn.granitech.variantorm.metadata.fieldtype;


import cn.granitech.variantorm.exception.DataAccessException;
import cn.granitech.variantorm.metadata.impl.ActualField;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.pojo.Field;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class DecimalField extends ActualField {
    public DecimalField() {
        super(BigDecimal.class);
    }

    public void setParamValue(PreparedStatement pstmt, int index, Object value, PersistenceManager pm) {
        try {
            if (value == null) {
                pstmt.setNull(index, Types.DECIMAL);
            } else {
                pstmt.setBigDecimal(index, (BigDecimal)value);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Set decimal to PreparedStatement error", e);
        }
    }

    public String getName() {
        return "Decimal";
    }

    public Object fromJson(Object jsonValue) {
        if (jsonValue == null) {
            return null;
        } else if (jsonValue instanceof BigDecimal) {
            return jsonValue;
        } else if (jsonValue instanceof Long) {
            return BigDecimal.valueOf((Long)jsonValue);
        } else if (jsonValue instanceof Integer) {
            return new BigDecimal((Integer)jsonValue);
        } else if (jsonValue instanceof String) {
            return new BigDecimal(((String)jsonValue).replaceAll(",", ""));
        } else if (jsonValue instanceof Double) {
            return BigDecimal.valueOf((Double)jsonValue);
        } else {
            throw new IllegalArgumentException((new StringBuilder()).insert(0, "invalid data format: [").append(jsonValue).append("]").toString());
        }
    }

    public Object readDBValue(PersistenceManager pm, Field field, ResultSet rs, int index) {
        try {
            BigDecimal value = rs.getBigDecimal(index);
            if (rs.wasNull()) {
                return null;
            } else {

                int precision= field.getFieldViewModel() != null && field.getFieldViewModel().getPrecision() != null ? field.getFieldViewModel().getPrecision() : 2;
                return value.setScale(precision, RoundingMode.HALF_UP);
            }
        } catch (SQLException var7) {
            throw new DataAccessException("Get decimal from ResultSet error", var7);
        }
    }
}

