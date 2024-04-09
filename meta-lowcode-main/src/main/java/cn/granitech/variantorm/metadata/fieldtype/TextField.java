package cn.granitech.variantorm.metadata.fieldtype;


import cn.granitech.variantorm.exception.DataAccessException;
import cn.granitech.variantorm.metadata.impl.ActualField;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.pojo.Field;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class TextField extends ActualField {
    public void setParamValue(PreparedStatement pstmt, int index, Object value, PersistenceManager pm) {
        try {
            if (value == null) {
                pstmt.setNull(index, Types.VARCHAR);
            } else {
                pstmt.setString(index, (String)value);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Set string to PreparedStatement error", e);
        }
    }

    public Object readDBValue(PersistenceManager pm, Field field, ResultSet rs, int index) {
        try {
            String a = rs.getString(index);
            return rs.wasNull() ? null : a;
        } catch (SQLException var6) {
            throw new DataAccessException("Get string from ResultSet error", var6);
        }
    }

    public TextField() {
        super(String.class);
    }

    public String getName() {
        return "Text";
    }
}
