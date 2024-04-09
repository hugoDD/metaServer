package cn.granitech.variantorm.util;


import cn.granitech.variantorm.constant.CommonFields;
import cn.granitech.variantorm.exception.InvalidFieldException;
import cn.granitech.variantorm.metadata.FieldType;
import cn.granitech.variantorm.metadata.MetadataManager;
import cn.granitech.variantorm.metadata.fieldtype.FieldTypes;
import cn.granitech.variantorm.metadata.fieldtype.ReferenceField;
import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.variantorm.pojo.Field;

import java.util.Set;

public class MDHelper {

    public static String getFPName(MetadataManager mdm, String entityName, String fieldName) {
        return mdm.getEntity(entityName).getField(fieldName).getPhysicalName();
    }

    public static boolean isReservedField(Field field) {
        if (CommonFields.containField(field.getName())) {
            return true;
        } else if (field.getType() == FieldTypes.PRIMARYKEY) {
            return true;
        } else {
            return field.isMainDetailFieldFlag();
        }
    }

    public MDHelper() {
    }

    public static String getEPName(MetadataManager mdm, String entityName) {
        return mdm.getEntity(entityName).getPhysicalName();
    }

    public static String getEntityOfCascadeField(MetadataManager mdm, String rootEntity, String cascadeField) {
        if (!cascadeField.contains("."))
            return rootEntity;
        String a = cascadeField.substring(0, cascadeField.indexOf("."));
        String str1 = cascadeField.substring(cascadeField.indexOf(".") + 1);
        Field field = mdm.getEntity(rootEntity).getField(a);
        if (!(field.getType() instanceof ReferenceField)) {
            throw new InvalidFieldException("Only reference field can use cascade symbol!");
        }
        Set<Entity> set = field.getReferTo();
        return getEntityOfCascadeField(mdm, set.iterator().next().getName(), str1);
    }
    public static FieldType getFieldTypeOfCascadeField(MetadataManager mdm, String rootEntity, String cascadeField) {
        String a = getLastFieldOfCascadeField(cascadeField);
        return mdm.getEntity(getEntityOfCascadeField(mdm, rootEntity, cascadeField)).getField(a).getType();
    }

    public static String getLastFieldOfCascadeField(String cascadeField) {
        return cascadeField.substring(cascadeField.lastIndexOf(".") + 1);
    }
}
