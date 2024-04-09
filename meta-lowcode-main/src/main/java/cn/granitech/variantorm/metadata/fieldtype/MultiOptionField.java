package cn.granitech.variantorm.metadata.fieldtype;


import cn.granitech.variantorm.exception.DataAccessException;
import cn.granitech.variantorm.metadata.impl.ActualField;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.pojo.Field;
import cn.granitech.variantorm.pojo.OptionModel;
import org.apache.commons.lang3.StringUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MultiOptionField extends ActualField {
    public void formatFieldValueOfRecord(EntityRecord record, String fieldName, Object valueObj) {
        if (valueObj == null) {
            record.setFieldValue(fieldName, null);
            record.setFieldLabel(fieldName, null);
        } else if (valueObj.getClass().isArray()) {
            List<Integer> list = new ArrayList<>();

            List<String> labList = new ArrayList<>();
            for (OptionModel optionModel : (OptionModel[]) valueObj) {
                labList.add(optionModel.getLabel());
                list.add(optionModel.getValue());
            }

            record.setFieldValue(fieldName, list);
            record.setFieldLabel(fieldName, String.join(",", labList));
        } else {
            throw new IllegalArgumentException("invalid data format: [" + valueObj + "]");
        }
    }

    public String getName() {
        return "MultiOption";
    }

    public Object fromJson(Object jsonValue) {
        if (jsonValue == null) {
            return null;
        } else if (jsonValue instanceof String){
            return jsonValue;
        }else if (jsonValue instanceof List) {
            return String.join(",", (List<String>) jsonValue);

        } else {
            throw new IllegalArgumentException((new StringBuilder()).insert(0, "invalid data format: [").append(jsonValue).append("]").toString());
        }
    }

    public MultiOptionField() {
        super(String.class);
    }

    public Object readDBValue(PersistenceManager pm, Field field, ResultSet rs, int index) {
        try {
            String value = rs.getString(index);
            if (rs.wasNull()) {
                return null;
            } else {
                return Stream.of(value.split(","))
                        .filter(StringUtils::isNotBlank)
                        .filter(StringUtils::isNumeric)
                        .map(v -> pm.getQueryCache()
                                .getOption(field.getOwner().getName(), field.getName(), Integer.parseInt(v)))
                        .filter(Objects::nonNull).collect(Collectors.toList());

            }
        } catch (SQLException e) {
            throw new DataAccessException("Get integer from ResultSet error", e);
        }
    }

    public void setParamValue(PreparedStatement pstmt, int index, Object value, PersistenceManager pm) {
        try {
            if (value == null) {
                pstmt.setNull(index, Types.VARCHAR);
            } else {
                pstmt.setString(index, (String) value);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Set string to PreparedStatement error", e);
        }
    }
}
