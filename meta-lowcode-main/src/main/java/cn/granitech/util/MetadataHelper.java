package cn.granitech.util;

import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.metadata.fieldtype.FieldTypes;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.variantorm.pojo.Field;
import cn.granitech.variantorm.pojo.QuerySchema;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class MetadataHelper {
    private static final String FIELD_SP = ",";
    private static final String REFERENCE_SP = "\\.";

    public MetadataHelper() {
    }

    public static String entityFieldFilter(Entity entity, String fields) {
        if (StringUtils.isBlank(fields)) {
            return fields;
        } else {
            List<String> fieldList = new ArrayList<>();
            Arrays.asList(fields.split(FIELD_SP)).forEach((field) -> {
                field = field.trim();
                if (!field.contains(".")) {
                    if (entity.containsField(field)) {
                        fieldList.add(field);
                    }
                } else {
                    String[] array = field.split(REFERENCE_SP);
                    Entity referTo = entity;

                    for (int i = 0; i < array.length - 1; ++i) {
                        if (!referTo.containsField(array[i])) {
                            return;
                        }

                        referTo = referTo.getField(array[i]).getReferTo().iterator().next();
                    }

                    if (referTo.containsField(array[array.length - 1])) {
                        fieldList.add(field);
                    }
                }

            });
            return String.join(",", fieldList);
        }
    }

    public static Boolean containsIdField(QuerySchema querySchema, Field idField) {
        String[] fieldArray = querySchema.getSelectFields().replaceAll(" ", "").split(",");

        for (String fldExp : fieldArray) {
            if (fldExp.equals(idField.getName())) {
                return true;
            }
        }

        return false;
    }

    public static String[] addOwnerFields(String[] fieldNames) {
        Set<String> fieldSet = new HashSet<>(Arrays.asList(fieldNames));
        fieldSet.add("ownerUser");

        fieldSet.add("ownerDepartment");

        return fieldSet.toArray(new String[0]);
    }

    public static <T> T getFieldValue(EntityRecord entityRecord, String fieldName) {
        if (!entityRecord.getEntity().containsField(fieldName)) {
            return null;
        } else {
            T fieldValue = entityRecord.getFieldValue(fieldName);
            if (fieldValue instanceof ID) {
                return (T) ((ID) fieldValue).getId();
            }

            return fieldValue;
        }
    }

    public static Field[] getReferenceToFields(Entity sourceEntity, Entity referenceEntity, boolean includeN2N) {
        List<Field> fields = new ArrayList<>();

        for (Field field : referenceEntity.getFieldSet()) {
            boolean isRef = field.getType() == FieldTypes.REFERENCE || includeN2N && field.getType() == FieldTypes.REFERENCELIST;
            if (isRef && field.getOwner().equals(sourceEntity)) {
                fields.add(field);
            }
        }

        return fields.toArray(new Field[0]);
    }

    public static boolean isIgnoreField(Field field) {
        String fieldName = field.getName();
        return "createdOn".equalsIgnoreCase(fieldName) || "createdBy".equalsIgnoreCase(fieldName) || "modifiedOn".equalsIgnoreCase(fieldName) || "modifiedBy".equalsIgnoreCase(fieldName) || "ownerDepartment".equalsIgnoreCase(fieldName) || "ownerUser".equalsIgnoreCase(fieldName) || FieldTypes.PRIMARYKEY == field.getType() || isApprovalField(fieldName);
    }

    public static boolean isIgnoreFieldByName(String fieldName) {
        return "createdOn".equalsIgnoreCase(fieldName) || "createdBy".equalsIgnoreCase(fieldName) || "modifiedOn".equalsIgnoreCase(fieldName) || "modifiedBy".equalsIgnoreCase(fieldName) || isApprovalField(fieldName);
    }

    public static boolean isApprovalField(String fieldName) {
        return "approvalStatus".equalsIgnoreCase(fieldName) || "lastApprovalRemark".equalsIgnoreCase(fieldName) || "lastApprovedOn".equalsIgnoreCase(fieldName) || "approvalConfigId".equalsIgnoreCase(fieldName) || "lastApprovedBy".equalsIgnoreCase(fieldName);
    }

    public static boolean hasApprovalField(Entity entity) {
        return entity.containsField("approvalStatus") && entity.containsField("approvalConfigId");
    }
}
