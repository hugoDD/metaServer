package cn.granitech.variantorm.metadata.fieldtype;


import cn.granitech.variantorm.exception.DataAccessException;
import cn.granitech.variantorm.metadata.impl.ActualField;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.pojo.Field;
import cn.granitech.variantorm.util.DateHelper;

import java.sql.*;
import java.util.Date;

public class DateTimeField extends ActualField {
    public void setParamValue(PreparedStatement pstmt, int index, Object value, PersistenceManager pm) {
        try {
            if (value == null) {
                pstmt.setNull(index, Types.TIMESTAMP);
            } else {
                pstmt.setTimestamp(index, new Timestamp(((Date)value).getTime()));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Set datetime to PreparedStatement error", e);
        }
    }

    public DateTimeField() {
        super(Date.class);
    }

    public Object readDBValue(PersistenceManager pm, Field field, ResultSet rs, int index) {
        try {
            Timestamp a = rs.getTimestamp(index);
            return rs.wasNull() ? null : new Date(a.getTime());
        } catch (SQLException var6) {
            throw new DataAccessException("Get datatime from ResultSet error", var6);
        }
    }

    public Object fromJson(Object jsonValue) {
        if (jsonValue == null) {
            return null;
        } else if (jsonValue instanceof java.sql.Date) {
            return jsonValue;
        } else {
            Date a = DateHelper.parseDateTime((String)jsonValue);
            return new java.sql.Date(a.getTime());
        }
    }

    public String getName() {
        return "DateTime";
    }
}
