package cn.granitech.variantorm.metadata.fieldtype;


import cn.granitech.variantorm.exception.DataAccessException;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.pojo.Field;
import cn.granitech.variantorm.pojo.OptionModel;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StatusField extends IntegerField {
    public boolean hasLabelColumn() {
        return true;
    }

    public Object readDBValue(PersistenceManager pm, Field field, ResultSet rs, int index) {
        try {
            if (rs.wasNull()) {
                return null;
            }
            return pm.getQueryCache().getStatus(field.getOwner().getName(), field.getName(), rs.getInt(index));
        } catch (SQLException sQLException) {
            throw new DataAccessException("Get integer from ResultSet error", sQLException);
        }
    }

    public String getName() {
        return "Status";
    }

    public boolean hasLabel() {
        return true;
    }

    public void formatFieldValueOfRecord(EntityRecord record, String fieldName, Object valueObj) {
        if (valueObj == null) {
            record.setFieldValue(fieldName, null);
            record.setFieldLabel(fieldName, null);
            return;
        }
        if (valueObj instanceof OptionModel) {
            record.setFieldValue(fieldName, ((OptionModel)valueObj).getValue());
            record.setFieldLabel(fieldName, ((OptionModel)valueObj).getLabel());
            return;
        }
        throw new IllegalArgumentException("invalid data format: ["+valueObj+"]");
    }
}
