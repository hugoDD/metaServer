package cn.granitech.variantorm.metadata.fieldtype;


import cn.granitech.variantorm.exception.DataAccessException;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.metadata.impl.ActualField;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.pojo.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public final class PrimaryKeyField extends ActualField {
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

    public Object fromJson(Object jsonValue) {
        if (jsonValue == null)
            return null;
        return new ID((String)jsonValue);
    }

    public PrimaryKeyField() {
        super(ID.class);
    }

    public Object readDBValue(PersistenceManager pm, Field field, ResultSet rs, int index) {
        try {
            String value = rs.getString(index);
            if (rs.wasNull() || value == null || value.contains("nul"))
                return null;
            return new ID(value);
        } catch (SQLException sQLException) {
            throw new DataAccessException("Get id from ResultSet error", sQLException);
        }
    }

    public String getName() {
        return "PrimaryKey";
    }
}
