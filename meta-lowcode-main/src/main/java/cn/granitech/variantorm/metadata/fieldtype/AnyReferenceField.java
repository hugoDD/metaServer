package cn.granitech.variantorm.metadata.fieldtype;


import cn.granitech.variantorm.exception.DataAccessException;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.persistence.RecordQuery;
import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.variantorm.pojo.Field;
import cn.granitech.variantorm.pojo.IDName;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import static cn.granitech.variantorm.constant.SystemEntities.ReferenceCache;

public class AnyReferenceField extends ReferenceField {
    public AnyReferenceField() {
    }

    public boolean hasLabelColumn() {
        return false;
    }

    public void formatFieldValueOfRecord(EntityRecord record, String fieldName, Object valueObj) {
        if (valueObj == null) {
            record.setFieldValue(fieldName, null);
            record.setFieldLabel(fieldName, null);
        } else if (valueObj instanceof IDName) {
            record.setFieldValue(fieldName, ((IDName)valueObj).getId());
            record.setFieldLabel(fieldName, ((IDName)valueObj).getName());
        } else if (valueObj instanceof ID) {
            record.setFieldValue(fieldName, valueObj);
        } else if (valueObj instanceof String) {
            record.setFieldValue(fieldName, ID.valueOf(valueObj));
        } else {
            throw new IllegalArgumentException((new StringBuilder()).insert(0, "invalid data format: [").append(valueObj).append("]").toString());
        }
    }

    public String getName() {
        return "AnyReference";
    }

    public String getNameValueOfRecord(EntityRecord record, String nameField, ID referenceId) {
        return record.getFieldValue(nameField) == null ? referenceId.toString() : record.getFieldValue(nameField).toString();
    }

    public boolean hasLabel() {
        return true;
    }

    public Object readDBValue(PersistenceManager pm, Field field, ResultSet rs, int index) {
        try {
            String str = rs.getString(index);
            boolean wasNull = rs.wasNull() || str == null || str.contains("nul");
            if (!wasNull) {
                ID id = ID.valueOf(str);
                return pm.getQueryCache().getIDName(id.getId());
            } else {
                return null;
            }
        } catch (SQLException var8) {
            throw new DataAccessException("Get id from ResultSet error", var8);
        }
    }
    public void setParamValue(PreparedStatement pstmt, int index, Object value, PersistenceManager pm) {
        try {
            if (value == null) {
                pstmt.setNull(index, Types.VARCHAR);
            } else {
                ID id = ID.valueOf(value);
                if (value instanceof String) {
                    pstmt.setString(index, (String)value);
                } else {
                    pstmt.setString(index, value.toString());
                }

                Entity entity = pm.getMetadataManager().getEntity(id.getEntityCode());
                String fieldName = entity.getIdField().getName();
                if (entity.getNameField() != null) {
                    fieldName = entity.getNameField().getName();
                }

                String nameEqId = String.format(" [%s] = '%s' ", fieldName,id.getId());
                RecordQuery recordQuery = pm.createRecordQuery();
                String entityName = entity.getName();
                EntityRecord record= recordQuery.queryOne(entityName, nameEqId, null, null, fieldName);
                if (record != null) {
                    if (pm.getQueryCache().getIDName(record.id().getId()) == null) {
                        EntityRecord entityRecord = pm.newRecord(ReferenceCache);
                        entityRecord.setFieldValue("referenceId", entityRecord);
                        entityRecord.setFieldValue("recordLabel", this.getNameValueOfRecord(entityRecord, fieldName, id));
                        pm.insert(entityRecord);
                    }

                    IDName idName = new IDName(id, this.getNameValueOfRecord(record, fieldName, id));
                    pm.getQueryCache().updateIDName(idName);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Set id to PreparedStatement error", e);
        }
    }
}
