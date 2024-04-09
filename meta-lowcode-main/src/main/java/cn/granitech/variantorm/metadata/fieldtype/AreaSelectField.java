package cn.granitech.variantorm.metadata.fieldtype;


import cn.granitech.variantorm.exception.DataAccessException;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.pojo.Field;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class AreaSelectField extends TextField {
    public Object fromJson(Object jsonValue) {
        if (jsonValue == null) {
            return null;
        } else if (jsonValue instanceof List) {
            List<String> list = (List<String>) jsonValue;


            return String.join(",", list) + ",";
        } else if (jsonValue instanceof String) {

            return jsonValue;

        } else {
            throw new IllegalArgumentException((new StringBuilder()).insert(0, "invalid data format: [").append(jsonValue).append("]").toString());

        }
    }

    public Object readDBValue(PersistenceManager pm, Field field, ResultSet rs, int index) {
        try {
            String str = rs.getString(index);
            if (rs.wasNull()) {
                return null;
            } else {
                return Arrays.asList(str.split(","));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Get integer from ResultSet error", e);
        }
    }

    public String getName() {
        return "AreaSelect";
    }
}

