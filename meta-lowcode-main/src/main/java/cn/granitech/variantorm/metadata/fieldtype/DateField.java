package cn.granitech.variantorm.metadata.fieldtype;


import cn.granitech.variantorm.HelloWorld;
import cn.granitech.variantorm.exception.DataAccessException;
import cn.granitech.variantorm.exception.MetadataSpacesException;
import cn.granitech.variantorm.metadata.impl.ActualField;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.pojo.Field;
import cn.granitech.variantorm.util.DateHelper;

import java.sql.*;

public class DateField extends ActualField {
    public String getName() {
        return "Date";
    }

    public void setParamValue(PreparedStatement pstmt, int index, Object value, PersistenceManager pm) {
        try {
            if (value == null) {
                pstmt.setNull(index,  Types.DATE);
            } else if (value instanceof Date) {
                pstmt.setDate(index, (Date)value);
            } else if (value instanceof java.util.Date) {
                pstmt.setDate(index, new Date(((java.util.Date)value).getTime()));
            } else {
                throw new IllegalArgumentException("Value is not valid date object!");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Set date to PreparedStatement error", e);
        }
    }

    public Object readDBValue(PersistenceManager pm, Field field, ResultSet rs, int index) {
        try {
            Date date = rs.getDate(index);
            return rs.wasNull() ? null : date;
        } catch (SQLException var6) {
            throw new DataAccessException("Get data from ResultSet error", var6);
        }
    }

    public DateField() {
        super(Date.class);
    }

    public Object fromJson(Object jsonValue) {
        if (jsonValue == null) {
            return null;
        } else if (jsonValue instanceof Date) {
            return jsonValue;
        } else {
            java.util.Date a = DateHelper.parseDate((String)jsonValue);
            return new Date(a.getTime());
        }
    }
}
