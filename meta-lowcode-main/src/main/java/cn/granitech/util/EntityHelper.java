package cn.granitech.util;

import cn.granitech.exception.ServiceException;
import cn.granitech.variantorm.metadata.FieldType;
import cn.granitech.variantorm.metadata.fieldtype.FieldTypes;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.variantorm.pojo.Field;
import cn.granitech.web.enumration.BuiltInEntityEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class EntityHelper {
    public static final String FIELD_LABEL = "fieldLabel";
    public static final String FIELD_NAME = "fieldName";
    public static final String FIELD_TYPE = "fieldType";
    public static final String IS_CREATABLE = "isCreatable";
    public static final String IS_NAME_FIELD = "isNameField";
    public static final String IS_NULLABLE = "isNullable";
    public static final String IS_UPDATABLE = "isUpdatable";
    public static final String REFERENCE_NAME = "referenceName";
    private static final Logger log = LoggerFactory.getLogger(EntityHelper.class);

    private EntityHelper() {
    }

    public static List<Entity> getBuiltInEntityList() {
        List<Entity> builtInEntity = new ArrayList<>();
        PersistenceManager pm = SpringHelper.getBean(PersistenceManager.class);
        for (BuiltInEntityEnum builtInEntityEnum : BuiltInEntityEnum.values()) {
            builtInEntity.add(pm.getMetadataManager().getEntity(builtInEntityEnum.getEntityCode()));
        }
        return builtInEntity;
    }

    public static Entity getEntity(String entityName) {
        return SpringHelper.getBean(PersistenceManager.class).getMetadataManager().getEntity(entityName);
    }

    public static Entity getEntity(int entityCode) {
        return SpringHelper.getBean(PersistenceManager.class).getMetadataManager().getEntity(entityCode);
    }

    public static Map<String, Object> field2Map(Field field) {
        return field2Map(field, null);
    }

    public static Map<String, Object> field2Map(Field field, Field referenceField) {
        Map<String, Object> map = new HashMap<>();
        map.put(FIELD_NAME, (referenceField != null ? referenceField.getName() + "." : "") + field.getName());
        map.put(FIELD_LABEL, (referenceField != null ? referenceField.getLabel() + "." : "") + field.getLabel());
        if (field.getType().equals(FieldTypes.REFERENCE)) {
            map.put(REFERENCE_NAME, field.getReferTo().iterator().next().getName());
        }
        map.put(FIELD_TYPE, field.getType().getName());
        map.put(IS_NULLABLE, Boolean.valueOf(field.isNullable()));
        map.put(IS_NAME_FIELD, Boolean.valueOf(field.isNameFieldFlag()));
        map.put(IS_UPDATABLE, Boolean.valueOf(field.isUpdatable()));
        map.put(IS_CREATABLE, Boolean.valueOf(field.isCreatable()));
        return map;
    }

    public static void formatFieldValue(EntityRecord entityRecord, Map<String, Object> dataMap) {
        Entity entity = entityRecord.getEntity();
        for (String dataKey : dataMap.keySet()) {
            if (entity.containsField(dataKey)) {
                Field field = entity.getField(dataKey);
                try {
                    entityRecord.setFieldValue(field.getName(), field.getType().fromJson(dataMap.get(dataKey)));
                } catch (Exception e) {
                    log.info("字段格式化错误！", e);
                    throw new ServiceException("字段转化错误，请查看日志！");
                }
            }
        }
    }

    public static Map<String, Object> getUpdateMap(Entity entity, Map<String, Object> newMap, Map<String, Object> oldMap) {
        Map<String, Object> updateMap = new HashMap<>();
        for (String key : newMap.keySet()) {
            if (!oldMap.containsKey(key)) {
                updateMap.put(key, newMap.get(key));
            } else {
                FieldType fieldType = entity.getField(key).getType();
                Object oldValue = oldMap.get(key);
                Object newValue = newMap.get(key);
                if (fieldType == FieldTypes.FILE || fieldType == FieldTypes.PICTURE) {
                    oldValue = JsonHelper.writeObjectAsString(oldValue);
                }
                if (!Objects.equals(oldValue, newValue)) {
                    updateMap.put(key, newValue);
                }
            }
        }
        return updateMap;
    }
}
