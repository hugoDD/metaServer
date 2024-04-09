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

public class FileField extends ActualField {
    public Object readDBValue(PersistenceManager pm, Field field, ResultSet rs, int index) {
        try {
            String json = rs.getString(index);
            return rs.wasNull() ? null : JsonUtils.readJsonValue(json, new TypeReference<List<UploadItem>>(){});
        } catch (SQLException e) {
            throw new DataAccessException("Get string from ResultSet error", e);
        }
    }

    public String getName() {
        return "File";
    }

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

    protected FileField() {
        super(ArrayList.class);
    }

    public Object fromJson(Object jsonValue) {
        if (jsonValue == null) {
            return null;
        } else if (jsonValue instanceof String) {
            return jsonValue;
        } else if (jsonValue instanceof List) {
            return JsonUtils.writeObjectAsString(jsonValue);
        } else {
            throw new IllegalArgumentException("invalid data format: ["+jsonValue+"]");
        }
    }
}
