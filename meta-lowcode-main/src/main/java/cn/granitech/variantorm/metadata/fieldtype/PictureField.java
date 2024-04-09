package cn.granitech.variantorm.metadata.fieldtype;

import cn.granitech.variantorm.exception.DataAccessException;
import cn.granitech.variantorm.metadata.impl.ActualField;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.pojo.Field;
import cn.granitech.variantorm.pojo.UploadItem;
import cn.granitech.variantorm.util.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class PictureField extends ActualField {
    public String getName() {
        return "Picture";
    }

    public Object fromJson(Object jsonValue) {
        if (jsonValue == null)
            return null;
        if (jsonValue instanceof String)
            return jsonValue;
        if (jsonValue instanceof List)
            return JsonUtils.writeObjectAsString(jsonValue);
        throw new IllegalArgumentException("invalid data format: ["+jsonValue+"]");
    }

    public PictureField() {
        super(ArrayList.class);
    }

    public Object readDBValue(PersistenceManager pm, Field field, ResultSet rs, int index) {
        try {
            String content = rs.getString(index);
            return rs.wasNull() ? null : JsonUtils.readJsonValue(content, new TypeReference<List<UploadItem>>(){
            });
        } catch (SQLException sQLException) {
            throw new DataAccessException("Get string from ResultSet error", sQLException);
        }
    }

    public void setParamValue(PreparedStatement pstmt, int index, Object value, PersistenceManager pm) {
        try {
            if (value == null) {
                pstmt.setNull(index, Types.VARCHAR);
            }
            pstmt.setString(index, (String)value);
        } catch (SQLException e) {
            throw new DataAccessException("Set string to PreparedStatement error", e);
        }
    }
}
