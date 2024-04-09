package cn.granitech.business.service;

import cn.granitech.exception.ServiceException;
import cn.granitech.util.CopyEntityHelper;
import cn.granitech.util.EntityHelper;
import cn.granitech.util.RedisUtil;
import cn.granitech.variantorm.constant.CommonFields;
import cn.granitech.variantorm.constant.ReservedFields;
import cn.granitech.variantorm.constant.SystemEntities;
import cn.granitech.variantorm.dbmapping.DBMappingManager;
import cn.granitech.variantorm.exception.DuplicateEntityException;
import cn.granitech.variantorm.metadata.MetadataManager;
import cn.granitech.variantorm.metadata.fieldtype.FieldTypes;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.variantorm.pojo.Field;
import cn.granitech.variantorm.util.MDHelper;
import cn.granitech.web.enumration.RedisKeyEnum;
import cn.granitech.web.pojo.CopyEntityBody;
import cn.granitech.web.pojo.KeyValueEntry;
import cn.hutool.core.comparator.PinyinComparator;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class EntityManagerService extends BaseService {
    @Autowired
    DBMappingManager dbMappingManager;
    @Autowired
    PersistenceManager persistenceManager;
    @Autowired
    OptionManagerService optionManagerService;
    @Autowired
    TagManagerService tagManagerService;
    @Autowired
    FormLayoutManager formLayoutManager;
    @Autowired
    RoleService roleService;
    @Resource
    RedisUtil redisUtil;

    public EntityManagerService() {
    }

    public MetadataManager getMetadataManager() {
        return this.persistenceManager.getMetadataManager();
    }

    @Transactional
    public void createEntity(Entity entity, String mainEntityName) {
        if (this.getMetadataManager().containsEntity(entity.getName())) {
            throw new DuplicateEntityException("实体已存在!");
        } else if (entity.getEntityCode() != null && this.getMetadataManager().containsEntity(entity.getEntityCode())) {
            throw new DuplicateEntityException("entityCode[" + entity.getEntityCode() + "]已存在!");
        } else {
            if (StringUtils.isBlank(entity.getPhysicalName())) {
                entity.setPhysicalName("t_" + entity.getName().toLowerCase());
            }

            if (entity.isDetailEntityFlag()) {
                Entity mainEntity = this.getMetadataManager().getEntity(mainEntityName);
                entity.setMainEntity(mainEntity);
            }

            Field idField = new Field();
            String idFieldName = entity.getName().substring(0, 1).toLowerCase() + entity.getName().substring(1) + "Id";
            idField.setName(idFieldName);
            idField.setLabel("id主键");
            idField.setPhysicalName(idField.getName());
            idField.setType(FieldTypes.PRIMARYKEY);
            idField.setIdFieldFlag(true);
            entity.setIdField(idField);
            this.dbMappingManager.createEntity(entity);
            if (entity.isDetailEntityFlag()) {
                Field mainDetailField = new Field();
                mainDetailField.setName("md" + mainEntityName + "Id");
                mainDetailField.setLabel("主从关联Id");
                mainDetailField.setPhysicalName(mainDetailField.getName());
                mainDetailField.setType(FieldTypes.REFERENCE);
                mainDetailField.setNullable(false);
                mainDetailField.setCreatable(true);
                mainDetailField.setUpdatable(false);
                mainDetailField.setIdFieldFlag(false);
                mainDetailField.setDefaultMemberOfListFlag(true);
                mainDetailField.setMainDetailFieldFlag(true);
                final Entity mainEntity = this.getMetadataManager().getEntity(mainEntityName);
                Set<Entity> referTo = new HashSet<Entity>() {
                    {
                        this.add(mainEntity);
                    }
                };
                mainDetailField.setReferTo(referTo);
                mainDetailField.setOwner(entity);
                this.dbMappingManager.createDateTimeField(entity.getEntityCode(), mainDetailField);
            }

            this.roleService.loadRoleCache();
        }
    }

    public List<String> getAllTagsOfEntity() {
        List<String> tagsList = this.getMetadataManager().getAllTagsOfEntity();
        tagsList.sort(new PinyinComparator());
        return tagsList;
    }

    @Transactional
    public void updateEntity(Entity entity) {
        this.dbMappingManager.updateEntity(entity);
    }

    @Transactional
    public void updateEntityNameField(Entity entity, Field newNameField) {
        Field oldNameField = entity.getNameField();
        if (oldNameField != null) {
            oldNameField.setNameFieldFlag(false);
            this.dbMappingManager.updateField(entity.getEntityCode(), oldNameField);
        }

        entity.setNameField(newNameField);
        this.dbMappingManager.updateEntity(entity);
        this.dbMappingManager.updateField(entity.getEntityCode(), newNameField);
    }

    public Boolean entityCanBeDeleted(String entityName) {
        return !SystemEntities.isInternalEntity(entityName) && !SystemEntities.isSystemEntity(entityName);
    }

    @Transactional
    public void deleteEntity(String entityName) {
        Entity deleteEntity = this.getMetadataManager().getEntity(entityName);
        if (!ObjectUtil.isNull(deleteEntity)) {
            List<String> referFieldList = new ArrayList<>();
            this.pm.getMetadataManager().getEntitySet().forEach((entity) -> {
                if (!entity.getName().equals(entityName)) {
                    entity.getFieldSet().forEach((field) -> {
                        if (field.getType() == FieldTypes.REFERENCE) {
                            field.getReferTo().forEach((referEntity) -> {
                                if (referEntity.getName().equals(entityName)) {
                                    referFieldList.add(String.format("%s.%s", entity.getLabel(), field.getLabel()));
                                }

                            });
                        }

                    });
                }
            });
            if (referFieldList.size() > 0) {
                throw new ServiceException("实体已被其他实体引用（%s）,请先删除相关字段", String.join(",", referFieldList));
            } else {
                this.dbMappingManager.deleteEntity(entityName);
                this.formLayoutManager.deleteEntityLayout(deleteEntity.getEntityCode());
                this.roleService.deleteEntityRight(deleteEntity.getEntityCode());
                this.roleService.loadRoleCache();
                this.clearRelatedEntityData(deleteEntity);
            }
        }
    }

    @Transactional
    public void createPlainField(int entityCode, Field field) {
        this.dbMappingManager.createDateTimeField(entityCode, field);
    }

    @Transactional
    public void createOptionField(int entityCode, Field field, List<KeyValueEntry<String, Integer>> optionList) {
        this.dbMappingManager.createDateTimeField(entityCode, field);
        if (optionList.size() > 0) {
            this.optionManagerService.saveOptionList(field.getOwner().getName(), field.getName(), optionList);
        }

    }

    @Transactional
    public void updateOptionField(int entityCode, Field field, List<KeyValueEntry<String, Integer>> optionList) {
        this.updatePlainField(entityCode, field);
        this.optionManagerService.saveOptionList(field.getOwner().getName(), field.getName(), optionList);
    }

    @Transactional
    public void createTagField(int entityCode, Field field, List<String> tagList) {
        this.dbMappingManager.createDateTimeField(entityCode, field);
        if (tagList.size() > 0) {
            this.tagManagerService.saveTagList(field.getOwner().getName(), field.getName(), tagList);
        }

    }

    @Transactional
    public void updateTagField(int entityCode, Field field, List<String> tagList) {
        this.updatePlainField(entityCode, field);
        this.tagManagerService.saveTagList(field.getOwner().getName(), field.getName(), tagList);
    }

    @Transactional
    public void updatePlainField(int entityCode, Field field) {
        Entity entity = this.getMetadataManager().getEntity(entityCode);
        if (!entity.containsField(field.getName())) {
            throw new IllegalArgumentException("invalid field name: [" + field.getName() + "]");
        } else {
            field.setOwner(entity);
            field.setEntityCode(entityCode);
            Field oldField = entity.getField(field.getName());
            field.setName(oldField.getName());
            field.setPhysicalName(oldField.getPhysicalName());
            field.setType(oldField.getType());
            field.setDisplayOrder(oldField.getDisplayOrder());
            field.setDescription(oldField.getDescription());
            field.setIdFieldFlag(oldField.isIdFieldFlag());
            field.setNameFieldFlag(oldField.isNameFieldFlag());
            field.setMainDetailFieldFlag(oldField.isMainDetailFieldFlag());
            this.dbMappingManager.updateField(entityCode, field);
        }
    }

    public boolean isReservedField(String fieldName, String entityName) {
        Field field = this.getMetadataManager().getEntity(entityName).getField(fieldName);
        return MDHelper.isReservedField(field);
    }

    public Boolean fieldCanBeDeleted(String entityName, String fieldName) {
        return !ReservedFields.isReservedField(entityName, fieldName);
    }

    public Boolean fieldCanBeEdited(String entityName, String fieldName) {
        return (!"User".equals(entityName) || !"roles".equals(fieldName)) && !CommonFields.containField(fieldName);
    }

    @Transactional
    public void deleteField(String entityName, String fieldName) throws IllegalAccessException {
        if (ReservedFields.isReservedField(entityName, fieldName)) {
            throw new IllegalAccessException("System field or reserved field can not be deleted!");
        } else {
            this.dbMappingManager.deleteField(entityName, fieldName);
        }
    }

    @Transactional
    public void createApprovalSystemFields(int entityCode) {
        Entity entity = this.getMetadataManager().getEntity(entityCode);
        this.dbMappingManager.createApprovalSystemFields(entity);
    }

    @Transactional
    public Set<Entity> getReferToEntitySet(String entityName, Boolean internalEntitiesIncluded, Boolean systemEntitiesIncluded) {
        Set<Entity> resultSet = new LinkedHashSet<>();
        Entity originalEntity = getMetadataManager().getEntity(entityName);
        for (Entity entity : getMetadataManager().getEntitySet()) {
            for (Field field : entity.getFieldSet()) {
                if (field.getType() == FieldTypes.REFERENCE && field.getReferTo().contains(originalEntity) && !entity.getName().equals(entityName)) {
                    if (SystemEntities.isInternalEntity(entity.getName()) && internalEntitiesIncluded) {
                        resultSet.add(entity);
                    } else if (SystemEntities.isSystemEntity(entity.getName()) && systemEntitiesIncluded) {
                        resultSet.add(entity);
                    } else if (!SystemEntities.isInternalEntity(entity.getName()) && !SystemEntities.isSystemEntity(entity.getName())) {
                        resultSet.add(entity);
                    }
                }
            }
        }
        return resultSet;
    }

    public List<Map<String, Object>> queryEntityList(int entityCode, boolean queryMain, boolean queryReference, boolean queryReferenced, boolean querySystem, boolean queryBuiltIn) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        MetadataManager metadataManager = this.getMetadataManager();
        Entity mainEntity = metadataManager.getEntity(entityCode);
        if (queryReference) {
            mainEntity.getFieldSet().forEach((field) -> {
                if (field.getType() == FieldTypes.REFERENCE) {
                    Entity reference = field.getReferTo().iterator().next();
                    if (!querySystem && SystemEntities.isSystemEntity(reference.getName())) {
                        return;
                    }

                    Map<String, Object> entityMap = new HashMap<>();
                    entityMap.put("entityName", reference.getName());
                    entityMap.put("entityCode", reference.getEntityCode());
                    entityMap.put("label", String.format("%s(%s)", reference.getLabel(), field.getLabel()));
                    entityMap.put("entityLabel", reference.getLabel());
                    entityMap.put("fieldName", field.getName());
                    entityMap.put("isReferenced", false);
                    resultList.add(entityMap);
                }

            });
        }

        if (queryReferenced) {
            metadataManager.getEntitySet().forEach((entity) -> {
                if (!entity.isDetailEntityFlag()) {
                    entity.getFieldSet().forEach((field) -> {
                        if (field.getType() == FieldTypes.REFERENCE) {
                            if (field.getReferTo().size() != 0) {
                                Entity reference = field.getReferTo().iterator().next();
                                if (reference.equals(mainEntity)) {
                                    if (querySystem || !SystemEntities.isSystemEntity(reference.getName())) {
                                        Map<String, Object> entityMap = new HashMap<>();
                                        entityMap.put("entityName", entity.getName());
                                        entityMap.put("entityCode", entity.getEntityCode());
                                        entityMap.put("label", String.format("%s(%s)(N)", entity.getLabel(), field.getLabel()));
                                        entityMap.put("entityLabel", entity.getLabel());
                                        entityMap.put("fieldName", field.getName());
                                        entityMap.put("isReferenced", true);
                                        resultList.add(entityMap);
                                    }
                                }
                            }
                        }
                    });
                }
            });
        }

        if (queryMain) {
            Map<String, Object> entityMap = new HashMap<>();
            entityMap.put("entityCode", mainEntity.getEntityCode());
            entityMap.put("entityName", mainEntity.getName());
            entityMap.put("label", mainEntity.getLabel());
            entityMap.put("isReferenced", false);
            resultList.add(entityMap);
        }

        if (queryBuiltIn) {

            for (Entity builtInEntity : EntityHelper.getBuiltInEntityList()) {
                Map<String, Object> entityMap = new HashMap<>();
                entityMap.put("entityName", builtInEntity.getName());
                entityMap.put("entityCode", builtInEntity.getEntityCode());
                entityMap.put("entityLabel", builtInEntity.getLabel());
                entityMap.put("fieldName", "recordId");
                entityMap.put("isReferenced", false);
                resultList.add(entityMap);
            }
        }

        return resultList;
    }

    private void clearRelatedEntityData(Entity entity) {
        Integer entityCode = entity.getEntityCode();
        String entityNameFilter = String.format("entityName = '%s'", entity.getName());
        String entityCodeFilter = String.format("entityCode = '%s'", entityCode);
        this.batchDeleteRecord("ApprovalConfig", entityCodeFilter);
        this.batchDeleteRecord("TriggerConfig", entityCodeFilter);
        this.batchDeleteRecord("TriggerLog", String.format(" recordId LIKE '%%%s-%%' ", entityCode));
        this.batchDeleteRecord("ReportConfig", entityCodeFilter);
        this.batchDeleteRecord("RevisionHistory", entityCodeFilter);
        this.batchDeleteRecord("RecycleBin", entityCodeFilter);
        this.batchDeleteRecord("Notification", String.format(" relatedRecord LIKE '%%%s-%%' ", entityCode));
        this.batchDeleteRecord("LayoutConfig", entityCodeFilter);
        this.batchDeleteRecord("FormLayout", entityCodeFilter);
        this.batchDeleteRecord("OptionItem", entityNameFilter);
        this.batchDeleteRecord("TagItem", entityNameFilter);
        this.batchDeleteRecord("ReferenceListMap", entityNameFilter);
        String entityCodePre = StrUtil.padPre(String.valueOf(entityCode), 7, '0');
        this.redisUtil.removePattern(RedisKeyEnum.REFERENCE_LIST_MAP.getKey(entity.getName()) + "*");
        this.redisUtil.removePattern(RedisKeyEnum.QUICK_FILTER.getKey(entity.getName()) + "*");
        this.redisUtil.removePattern(RedisKeyEnum.REFERENCE_CACHE.getKey(entityCodePre) + "*");
        this.redisUtil.removePattern(RedisKeyEnum.USER_LAYOUT_CACHE.getKey("FILTER:" + entity.getName()) + "*");
        this.redisUtil.removePattern(RedisKeyEnum.USER_LAYOUT_CACHE.getKey("LIST:" + entity.getName()) + "*");
        this.redisUtil.removePattern(RedisKeyEnum.USER_LAYOUT_CACHE.getKey("NAV:" + entityCodePre) + "*");
    }

    public void putFieldOptionData(Field field, Map<String, Object> fieldMap) {
        if (field.getType() == FieldTypes.OPTION || field.getType() == FieldTypes.TAG || field.getType() == FieldTypes.STATUS) {
            Entity refOrEntity = EntityHelper.getEntity(field.getEntityCode());
            String realFileIdName = field.getName();
            if (realFileIdName.contains(".")) {
                realFileIdName = realFileIdName.substring(realFileIdName.lastIndexOf(".") + 1);
            }

            List<?> dataList = null;
            if (field.getType() == FieldTypes.OPTION) {
                dataList = this.optionManagerService.getOptionList(refOrEntity.getName(), realFileIdName);
            } else if (field.getType() == FieldTypes.TAG) {
                dataList = this.tagManagerService.getTagList(refOrEntity.getName(), realFileIdName);
            } else if (field.getType() == FieldTypes.STATUS) {
                dataList = this.optionManagerService.getStatusList(realFileIdName);
            }

            if (dataList != null) {
                fieldMap.put("optionData", dataList);
            }
        }

    }

    @Transactional(
            propagation = Propagation.NESTED
    )
    public void copyEntity(CopyEntityBody copyEntityBody) {
        Entity sourceEntity = copyEntityBody.getSourceEntity();
        Entity oldEntity = EntityHelper.getEntity(sourceEntity.getEntityCode());
        CopyEntityHelper.copyEntityData(sourceEntity, oldEntity, copyEntityBody.getMainEntityName());
        Entity newEntity = EntityHelper.getEntity(sourceEntity.getName());
        CopyEntityHelper.copyPlainFields(oldEntity, newEntity);
        if (oldEntity.getNameField() != null) {
            this.updateEntityNameField(newEntity, oldEntity.getNameField());
        }

        int operations = copyEntityBody.getOperations();
        if ((operations & 1) != 0) {
            CopyEntityHelper.handleFormDesign(oldEntity, newEntity);
        }

        if ((operations & 2) != 0) {
            CopyEntityHelper.handleListDesign(oldEntity, newEntity);
        }

        if ((operations & 4) != 0) {
            CopyEntityHelper.handleApprovalProcess(oldEntity, newEntity);
        }

    }
}
