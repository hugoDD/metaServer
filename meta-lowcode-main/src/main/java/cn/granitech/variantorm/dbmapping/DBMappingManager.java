package cn.granitech.variantorm.dbmapping;


import cn.granitech.variantorm.constant.ReservedFields;
import cn.granitech.variantorm.constant.SystemEntities;
import cn.granitech.variantorm.constant.SystemEntityCodes;
import cn.granitech.variantorm.dbmapping.ddl.DBDDL;
import cn.granitech.variantorm.dbmapping.ddl.MySQLDDL;
import cn.granitech.variantorm.exception.DuplicateEntityException;
import cn.granitech.variantorm.exception.DuplicateFieldException;
import cn.granitech.variantorm.metadata.MetadataManager;
import cn.granitech.variantorm.metadata.fieldtype.FieldTypes;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.variantorm.pojo.Field;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Collections;

import static cn.granitech.variantorm.constant.SystemEntityCodes.USER_CODE;

public class DBMappingManager {
    private final MetadataManager metadataManager;
    private final DBDDL db;
    private final JdbcTemplate jdbcTemplate;
    private final PersistenceManager pm;

    public void createDateTimeField(int entityCode, Field field) {
        if (DBMappingHelper.checkFieldExists(this.jdbcTemplate, field.getOwner().getEntityCode(), field.getName())) {
            throw new DuplicateFieldException("字段" + field.getName() + "已存在");
        }
        if (field.getType() != FieldTypes.REFERENCELIST) {
            this.db.createFieldColumn(field);
        }
        DBMappingHelper.insertMetaFieldRecord(this.jdbcTemplate, entityCode, field);
        this.metadataManager.addField(field.getOwner().getName(), field);
    }

    public void deleteEntity(String entityName) {
        if (SystemEntities.isInternalEntity(entityName) || SystemEntities.isSystemEntity(entityName)) {
            throw new IllegalStateException("系统实体、内部实体不可删除!");
        }
        Entity a = this.metadataManager.getEntity(entityName);
        this.db.deleteEntityTable(a);
        DBMappingHelper.deleteMetaEntityRecord(this.jdbcTemplate, entityName, a.getEntityCode());
        this.metadataManager.removeEntity(entityName);
    }


    private void createDateTimeField(Entity entity, MetadataManager mdm, String fieldName, String fieldLabel, Boolean nullable, Boolean creatable, Boolean updatable) {

        Field field = new Field();

        field.setName(fieldName);
        field.setLabel(fieldLabel);
        field.setPhysicalName(fieldName);
        field.setType(FieldTypes.DATETIME);
        field.setNullable(nullable);
        field.setCreatable(creatable);
        field.setUpdatable(updatable);
        field.setOwner(entity);
        DBMappingHelper.insertMetaFieldRecord(this.jdbcTemplate, entity.getEntityCode(), field);
        mdm.addField(entity.getName(), field);
    }

    public void updateEntity(Entity entity) {
        DBMappingHelper.updateMetaEntityRecord(this.jdbcTemplate, entity);
        this.metadataManager.updateEntity(entity);
    }

    public void createApprovalSystemFields(Entity entity) {
        int entityCode = entity.getEntityCode();
        Field approvalConfigField = new Field();
        approvalConfigField.setName("approvalConfigId");
        approvalConfigField.setLabel("审批流程");
        approvalConfigField.setPhysicalName("approvalConfigId");
        approvalConfigField.setType(FieldTypes.REFERENCE);
        approvalConfigField.setReferTo(Collections.singleton(metadataManager.getEntity(SystemEntityCodes.APPROVAL_CONFIG_CODE)));
        approvalConfigField.setNullable(true);
        approvalConfigField.setCreatable(false);
        approvalConfigField.setUpdatable(false);
        approvalConfigField.setOwner(entity);
        this.createDateTimeField(entityCode, approvalConfigField);
        Field approvalStatusField = new Field();
        approvalStatusField.setName("approvalStatus");
        approvalStatusField.setLabel("审批状态");
        approvalStatusField.setPhysicalName("approvalStatus");
        approvalStatusField.setType(FieldTypes.STATUS);
        approvalStatusField.setNullable(true);
        approvalStatusField.setCreatable(false);
        approvalStatusField.setUpdatable(false);
        approvalStatusField.setOwner(entity);
        this.createDateTimeField(entityCode, approvalStatusField);
        Field lastApprovedField = new Field();
        lastApprovedField.setName("lastApprovedBy");
        lastApprovedField.setLabel("最近审批人");
        lastApprovedField.setPhysicalName("lastApprovedBy");
        lastApprovedField.setType(FieldTypes.REFERENCE);
        lastApprovedField.setReferTo(Collections.singleton(metadataManager.getEntity(USER_CODE)));
        lastApprovedField.setNullable(true);
        lastApprovedField.setCreatable(false);
        lastApprovedField.setUpdatable(false);
        lastApprovedField.setOwner(entity);
        this.createDateTimeField(entityCode, lastApprovedField);
        Field lastApprovedOnField = new Field();
        lastApprovedOnField.setName("lastApprovedOn");
        lastApprovedOnField.setLabel("最近审批时间");
        lastApprovedOnField.setPhysicalName("lastApprovedOn");
        lastApprovedOnField.setType(FieldTypes.DATETIME);
        lastApprovedOnField.setNullable(true);
        lastApprovedOnField.setCreatable(false);
        lastApprovedOnField.setUpdatable(false);
        lastApprovedOnField.setOwner(entity);
        this.createDateTimeField(entityCode, lastApprovedOnField);
        Field lastApprovalRemarkField = new Field();
        lastApprovalRemarkField.setName("lastApprovalRemark");
        lastApprovalRemarkField.setLabel("最近审批批注");
        lastApprovalRemarkField.setPhysicalName("lastApprovalRemark");
        lastApprovalRemarkField.setType(FieldTypes.TEXTAREA);
        lastApprovalRemarkField.setNullable(true);
        lastApprovalRemarkField.setCreatable(false);
        lastApprovalRemarkField.setUpdatable(false);
        lastApprovalRemarkField.setOwner(entity);
        this.createDateTimeField(entityCode, lastApprovalRemarkField);
    }



    public void deleteField(String entityName, String fieldName) {
        if (ReservedFields.isReservedField(entityName, fieldName)) {
            throw new IllegalStateException("系统保留字段不可删除!");
        }
        Field field = this.metadataManager.getEntity(entityName).getField(fieldName);
        int entityCode = field.getOwner().getEntityCode();
        this.db.deleteFieldColumn(field);
        DBMappingHelper.deleteMetaFieldRecord(this.jdbcTemplate, entityCode, fieldName);
        this.metadataManager.removeField(entityName, fieldName);
    }


    private void createTextField(Entity entity, MetadataManager mdm, String fieldName, String fieldLabel, Boolean nullable, Boolean creatable, Boolean updatable) {
        Field field = new Field();

        field.setName(fieldName);
        field.setLabel(fieldLabel);
        field.setPhysicalName(fieldName);
        field.setType(FieldTypes.TEXTAREA);
        field.setNullable(nullable);
        field.setCreatable(creatable);
        field.setUpdatable(updatable);
        field.setOwner(entity);
        DBMappingHelper.insertMetaFieldRecord(this.jdbcTemplate, entity.getEntityCode(), field);
        mdm.addField(entity.getName(), field);
    }

    public void updateField(int entityCode, Field field) {
        if (entityCode != field.getEntityCode()) {
            throw new IllegalArgumentException("entity code mismatched!");
        }
        Entity entity = this.metadataManager.getEntity(entityCode);
        Field currentField = entity.getField(field.getName());
        field.setName(currentField.getName());
        field.setPhysicalName(currentField.getPhysicalName());
        field.setType(currentField.getType());
        field.setIdFieldFlag(currentField.isIdFieldFlag());
        field.setMainDetailFieldFlag(currentField.isMainDetailFieldFlag());
        if (field.getType() == FieldTypes.REFERENCE) {
            field.setReferTo(currentField.getReferTo());
        }

        DBMappingHelper.updateMetaFieldRecord(this.jdbcTemplate, entityCode, field);
        this.metadataManager.updateField(entity.getName(), field.getName(), field);
    }

    public void createEntity(Entity entity) {
        //@todo license验证
        /*if (this.pm != null && this.pm.getLicenseInfo().getEntityLimit() != null) {
            Integer entityLimit = this.pm.getLicenseInfo().getEntityLimit();
            JdbcTemplate jdbcTemplate = new JdbcTemplate(this.dataSource);
            Integer count = jdbcTemplate.queryForObject(" SELECT count(*) FROM `t_meta_entity` where entityCode > 1000 ", Integer.class);
            if (count != null && count >= entityLimit) {
                String entityLimitSql = "当前系统版本最多支持%d个自定义实体，已超出此限制！";
                throw new LicenseException(String.format(entityLimitSql, entityLimit));
            }
        }*/

        if (DBMappingHelper.checkEntityExists(this.jdbcTemplate, entity.getName())) {
            throw new DuplicateEntityException("实体" + entity.getName() + "已存在!");
        }
        if (DBMappingHelper.checkTableExists(this.jdbcTemplate, entity.getPhysicalName())) {
            throw new IllegalStateException("数据库表" + entity.getPhysicalName() + "已存在!");
        }
        this.db.createEntityTable(entity);
        DBMappingHelper.insertMetaEntityRecord(this.jdbcTemplate, entity);
        this.metadataManager.addEntity(entity);
        Field idField = entity.getIdField();
        DBMappingHelper.insertMetaFieldRecord(jdbcTemplate, entity.getEntityCode(), idField);
        metadataManager.addField(entity.getName(), idField);

        this.createDateTimeField(entity, metadataManager, "createdOn", "创建时间", false, false, false);
        this.createReferenceField(entity, metadataManager, "createdBy", "创建用户", "User", false, false, false);
        this.createDateTimeField(entity, metadataManager, "modifiedOn", "最近修改时间", true, false, false);
        this.createReferenceField(entity, metadataManager, "modifiedBy", "修改用户", "User", true, false, false);
        if (entity.isAuthorizable() || entity.isAssignable()) {
            this.createReferenceField(entity, this.metadataManager, "ownerUser", "所属用户", "User", false, false, false);
            this.createReferenceField(entity, this.metadataManager, "ownerDepartment", "所属部门", "Department", false, false, false);
        }
    }


    private void createIntegerField(Entity entity, MetadataManager mdm, String fieldName, String fieldLabel, Boolean nullable, Boolean creatable, Boolean updatable) {
        Field field = new Field();
        field.setName(fieldName);
        field.setLabel(fieldLabel);
        field.setPhysicalName(fieldName);
        field.setType(FieldTypes.INTEGER);
        field.setNullable(nullable);
        field.setCreatable(creatable);
        field.setUpdatable(updatable);
        field.setOwner(entity);
        DBMappingHelper.insertMetaFieldRecord(this.jdbcTemplate, entity.getEntityCode(), field);
        mdm.addField(entity.getName(),field);
    }


    private  void createReferenceField(Entity entity, MetadataManager mdm, String fieldName, String fieldLabel, String refToEntityName, Boolean nullable, Boolean creatable, Boolean updatable) {

        Field  field = new Field();

        field.setName(fieldName);
        field.setLabel(fieldLabel);
        field.setPhysicalName(fieldName);
        field.setType(FieldTypes.REFERENCE);
        field.setNullable(nullable);
        field.setCreatable(creatable);
        field.setUpdatable(updatable);
        Entity refEntity = mdm.getEntity(refToEntityName);
        field.setReferTo(Collections.singleton(refEntity));
        field.setOwner(entity);
        DBMappingHelper.insertMetaFieldRecord(this.jdbcTemplate, entity.getEntityCode(), field);
        mdm.addField(entity.getName(), field);
    }

    public DBMappingManager(JdbcTemplate jdbcTemplate, MetadataManager metadataManager, PersistenceManager persistenceManager) {

        this.jdbcTemplate = jdbcTemplate;
        this.metadataManager = metadataManager;
        this.pm = persistenceManager;
        this.db = new MySQLDDL(this.jdbcTemplate);
        DBMappingHelper.loadMetadataFromDB(this.jdbcTemplate, this.metadataManager);
    }

    public DBMappingManager(JdbcTemplate jdbcTemplate, MetadataManager metadataManager) {

        this.jdbcTemplate = jdbcTemplate;
        this.metadataManager = metadataManager;
        this.pm = null;
        this.db = new MySQLDDL(jdbcTemplate);
        DBMappingHelper.loadMetadataFromDB(jdbcTemplate, this.metadataManager);
    }
}
