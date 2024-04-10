package cn.granitech.variantorm.persistence.impl;


import cn.granitech.variantorm.exception.InvalidFieldException;
import cn.granitech.variantorm.metadata.FieldType;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.metadata.fieldtype.FieldTypes;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.variantorm.pojo.Field;
import cn.granitech.variantorm.serializer.EntityRecordSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@JsonSerialize(
        using = EntityRecordSerializer.class
)
public class EntityRecordImpl implements EntityRecord {
    private final Set<String> fields = new HashSet<>();
    private final Map<String, String> labelsMap = new HashMap<>();
    private Map<String, Object> valueMap = new HashMap<>();
    private final Entity entity;

    public Map<String, String> getLabelsMap() {
        return this.labelsMap;
    }

    public boolean labelValueExists(String fieldName) {
        if (this.entity.getField(fieldName).getType() == FieldTypes.REFERENCE) {
            String name = this.entity.getField(fieldName).getReferTo().stream().map(Entity::getNameField).map(Field::getName).findFirst().orElse("");
            if (this.valueMap.containsKey( fieldName+"."+name)) {
                return true;
            }
        }

        return this.labelsMap.containsKey(fieldName);
    }

    public void setFieldLabel(String fieldName, String labelValue) {
        this.labelsMap.put(fieldName, labelValue);
    }

    @JsonIgnore
    public boolean isModified(String fieldName) {
        if (fieldName == null) {
            throw new NullPointerException("field");
        } else if (!this.getEntity().containsField(fieldName)) {
            throw new IllegalArgumentException("No such field: "+fieldName);
        } else {
            return this.fields.contains(fieldName);
        }
    }

    public Map<String, Object> getValuesMap() {
        return this.valueMap;
    }

    public String asJson() {
        return null;
    }

    public void setNotModified() {
        this.fields.clear();
    }

    public void setValuesMap(Map<String, Object> valuesMap) {
        this.valueMap = valuesMap;
    }

    public String getName() {
        if (this.getEntity().getNameField() == null) {
            return null;
        } else {
            String name = this.getEntity().getNameField().getName();
            return this.getValueByFieldName(name) == null ? "" : this.getValueByFieldName(name).toString();
        }
    }

    public String getFieldLabel(String fieldName) {
        FieldType fieldType= this.entity.getField(fieldName).getType();
        if (fieldType  != FieldTypes.REFERENCE && fieldType != FieldTypes.ANYREFERENCE && fieldType != FieldTypes.REFERENCELIST) {
            throw new InvalidFieldException("Invalid field type!");
        } else if (this.labelsMap.containsKey(fieldName)) {
            return this.labelsMap.get(fieldName);
        } else {
            if (fieldType == FieldTypes.REFERENCE) {

                String name = this.entity.getField(fieldName).getReferTo().stream().map(Entity::getNameField).map(Field::getName).findFirst().orElse(null);
                String key =  fieldName+"."+name;
                if (this.valueMap.containsKey(key)) {
                    return (String)this.valueMap.get(key);
                }
            }

            return null;
        }
    }

    void getValueByFieldName(String fieldName, Object value) {
        if (fieldName == null) {
            throw new NullPointerException("field");
        } else if (!this.getEntity().containsField(fieldName)) {
            throw new IllegalArgumentException( "No such field: "+fieldName);
        } else {
            Field field=this.getEntity().getField(fieldName);
            FieldType fieldType= field.getType();
            if ((fieldType == FieldTypes.DECIMAL || fieldType == FieldTypes.MONEY) && value != null) {
                int precision = field.getFieldViewModel() != null && field.getFieldViewModel().getPrecision() != null ? field.getFieldViewModel().getPrecision() : 2;
                value = ((BigDecimal)value).setScale(precision, RoundingMode.HALF_UP);
            }

            if ((this.valueMap.get(fieldName) == null
                    || !this.valueMap.get(fieldName).equals(value))
                    && (!this.valueMap.containsKey(fieldName)
                    || this.valueMap.get(fieldName) != null
                    || value != null)) {
                this.valueMap.put(fieldName, value);
                this.fields.add(fieldName);
            } else {
                this.valueMap.put(fieldName, value);
            }
        }
    }

    public Entity getEntity() {
        return this.entity;
    }

    public <T> T getFieldValue(String fieldName) {
        return (T) this.getValueByFieldName(fieldName);
    }

    public boolean isNull(String field) {
        return this.getValueByFieldName(field) == null;
    }

    public boolean isModified() {
        if (this.fields.size() != 1 || !this.fields.contains("modifiedOn") && !this.fields.contains("modifiedBy")) {
            if (this.fields.size() == 2 && this.fields.contains("modifiedOn") && this.fields.contains("modifiedBy")) {
                return false;
            } else {
                return !this.fields.isEmpty();
            }
        } else {
            return false;
        }
    }

    public EntityRecordImpl(PersistenceManager pm, String entityName) {
        Assert.isTrue(pm != null, "PM must be not null.");
        this.entity = pm.getMetadataManager().getEntity(entityName);
    }

    Object getValueByFieldName(String fieldName) {
        if (fieldName == null) {
            throw new NullPointerException("field");
        } else if (!this.getEntity().containsField(fieldName)) {
            throw new IllegalArgumentException( "No such field: "+fieldName);
        } else {
            return this.valueMap.getOrDefault(fieldName, null);
        }
    }

    public ID id() {
        Object a=this.getValueByFieldName(this.getEntity().getIdField().getName());
        return a  instanceof String ? new ID((String)a) : (ID)a;
    }

    public EntityRecord fromJson(String json) {
        return null;
    }

    public void copyFrom(EntityRecord record) {
        record.getValuesMap().keySet().stream().filter(k->this.getEntity().containsField(k))
                .filter(a->this.getEntity().getField(a).getType() != FieldTypes.PRIMARYKEY)
                .forEach(a->this.setFieldValue(a,record.getFieldValue(a)));

//        for (String a : record.getValuesMap().keySet()) {
//            if (!this.getEntity().containsField(a)) {
//                continue;
//            }
//            if (this.getEntity().getField(a).getType() == FieldTypes.PRIMARYKEY) {
//                continue;
//            }
//            this.setFieldValue(a, record.getFieldValue(a));
//        }

    }

    @JsonIgnore
    public Set<String> getModifiedFieldSet() {
        return this.fields;
    }

    public void setFieldValue(String fieldName, Object value) {
        this.getValueByFieldName(fieldName, value);
    }
}

