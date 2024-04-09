package cn.granitech.variantorm.metadata;


import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.pojo.Field;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public interface FieldType {
    boolean hasLabel();

    String getName();

    Class<? extends Object> getValueClass();

    void formatFieldValueOfRecord(EntityRecord paramEntityRecord, String paramString, Object paramObject);

    void setParamValue(PersistenceManager paramPersistenceManager, Field paramField, ID paramID, Object paramObject);

    Object fromJson(Object paramObject);

    void setParamValue(PreparedStatement paramPreparedStatement, int paramInt, Object paramObject, PersistenceManager paramPersistenceManager);

    Object readDBValue(PersistenceManager paramPersistenceManager, Field paramField, ID paramID);

    Object readDBValue(PersistenceManager paramPersistenceManager, Field paramField, ResultSet paramResultSet, int paramInt);

    boolean hasLabelColumn();

    boolean isVirtual();
}
