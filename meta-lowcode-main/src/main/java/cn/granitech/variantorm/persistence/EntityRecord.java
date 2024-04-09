package cn.granitech.variantorm.persistence;


import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.pojo.Entity;

import java.util.Map;
import java.util.Set;

public interface EntityRecord {
    void setFieldValue(String fieldName, Object value);

    boolean isModified();

    void setFieldLabel(String fieldName, String labelValue);

    Set<String> getModifiedFieldSet();

    boolean labelValueExists(String fieldName);

    ID id();

    Map<String, String> getLabelsMap();

    Entity getEntity();

    void setValuesMap(Map<String, Object> valuesMap);

    void copyFrom(EntityRecord record);

    Map<String, Object> getValuesMap();

    EntityRecord fromJson(String json);

    boolean isModified(String fieldName);

    void setNotModified();

    boolean isNull(String field);

    <T> T getFieldValue(String fieldName);

    String asJson();

    String getFieldLabel(String fieldName);

    String getName();
}
