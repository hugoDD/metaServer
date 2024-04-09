package cn.granitech.web.controller;

import cn.granitech.variantorm.metadata.MetadataManager;
import cn.granitech.variantorm.metadata.fieldtype.FieldTypes;
import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.variantorm.pojo.Field;

import java.util.HashSet;

public abstract class BaseEntityManagerController extends BaseController {
    /* access modifiers changed from: protected */
    public Entity newEntity(int entityCode, String entityName, String entityLabel) {
        Entity entity = new Entity();
        entity.setEntityCode(Integer.valueOf(entityCode));
        entity.setName(entityName);
        entity.setLabel(entityLabel);
        entity.setPhysicalName("t_" + entityName.toLowerCase());
        return entity;
    }

    /* access modifiers changed from: protected */
    public Field newIdField(String fieldName, String fieldLabel) {
        Field idField = new Field();
        idField.setName(fieldName);
        idField.setLabel(fieldLabel);
        idField.setPhysicalName("c_" + fieldName);
        idField.setIdFieldFlag(true);
        idField.setType(FieldTypes.PRIMARYKEY);
        return idField;
    }

    /* access modifiers changed from: protected */
    public Field newIdField(String fieldName, String fieldLabel, String physicalName) {
        Field idField = new Field();
        idField.setName(fieldName);
        idField.setLabel(fieldLabel);
        idField.setPhysicalName(physicalName);
        idField.setIdFieldFlag(true);
        idField.setType(FieldTypes.PRIMARYKEY);
        return idField;
    }

    /* access modifiers changed from: protected */
    public Field newReferenceField(MetadataManager mdm, String fieldName, String fieldLabel, String referToEntityName) {
        Field refField = new Field();
        refField.setName(fieldName);
        refField.setLabel(fieldLabel);
        refField.setPhysicalName("c_" + fieldName);
        refField.setType(FieldTypes.REFERENCE);
        final Entity refToEntity = mdm.getEntity(referToEntityName);
        refField.setReferTo(new HashSet<Entity>() {
            {
                add(refToEntity);
            }
        });
        return refField;
    }

    /* access modifiers changed from: protected */
    public Field newReferenceField(MetadataManager mdm, String fieldName, String fieldLabel, String physicalName, String referToEntityName) {
        Field refField = new Field();
        refField.setName(fieldName);
        refField.setLabel(fieldLabel);
        refField.setPhysicalName(physicalName);
        refField.setType(FieldTypes.REFERENCE);
        final Entity refToEntity = mdm.getEntity(referToEntityName);
        refField.setReferTo(new HashSet<Entity>() {
            {
                add(refToEntity);
            }
        });
        return refField;
    }

    /* access modifiers changed from: protected */
    public Field newTextField(String fieldName, String fieldLabel) {
        Field txtField = new Field();
        txtField.setName(fieldName);
        txtField.setLabel(fieldLabel);
        txtField.setPhysicalName("c_" + fieldName);
        txtField.setType(FieldTypes.TEXT);
        return txtField;
    }

    /* access modifiers changed from: protected */
    public Field newTextField(String fieldName, String fieldLabel, String physicalName) {
        Field txtField = new Field();
        txtField.setName(fieldName);
        txtField.setLabel(fieldLabel);
        txtField.setPhysicalName(physicalName);
        txtField.setType(FieldTypes.TEXT);
        return txtField;
    }

    /* access modifiers changed from: protected */
    public Field newPasswordField(String fieldName, String fieldLabel, String physicalName) {
        Field pwdField = new Field();
        pwdField.setName(fieldName);
        pwdField.setLabel(fieldLabel);
        pwdField.setPhysicalName(physicalName);
        pwdField.setType(FieldTypes.PASSWORD);
        return pwdField;
    }

    /* access modifiers changed from: protected */
    public Field newIntegerField(String fieldName, String fieldLabel, String physicalName) {
        Field intField = new Field();
        intField.setName(fieldName);
        intField.setLabel(fieldLabel);
        intField.setPhysicalName(physicalName);
        intField.setType(FieldTypes.INTEGER);
        return intField;
    }

    /* access modifiers changed from: protected */
    public Field newTextAreaField(String fieldName, String fieldLabel, String physicalName) {
        Field taField = new Field();
        taField.setName(fieldName);
        taField.setLabel(fieldLabel);
        taField.setPhysicalName(physicalName);
        taField.setType(FieldTypes.TEXTAREA);
        return taField;
    }
}
