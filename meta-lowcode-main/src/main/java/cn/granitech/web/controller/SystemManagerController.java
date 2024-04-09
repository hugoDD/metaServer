package cn.granitech.web.controller;

import cn.granitech.aop.annotation.SystemRight;
import cn.granitech.business.service.EntityManagerService;
import cn.granitech.business.service.OptionManagerService;
import cn.granitech.business.service.TagManagerService;
import cn.granitech.business.service.database.DatabaseService;
import cn.granitech.exception.ServiceException;
import cn.granitech.util.EntityHelper;
import cn.granitech.util.MysqlKeywordHelper;
import cn.granitech.util.ObjectHelper;
import cn.granitech.util.ResponseHelper;
import cn.granitech.variantorm.constant.SystemEntities;
import cn.granitech.variantorm.metadata.MetadataManager;
import cn.granitech.variantorm.metadata.fieldtype.FieldTypes;
import cn.granitech.variantorm.pojo.*;
import cn.granitech.variantorm.util.MDHelper;
import cn.granitech.web.enumration.SystemRightEnum;
import cn.granitech.web.pojo.*;
import cn.granitech.web.pojo.application.CloudStorageSetting;
import cn.granitech.web.pojo.application.SystemSetting;
import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

@RestController
@RequestMapping({"/systemManager"})
public class SystemManagerController extends BaseController {
    private static final List<String> SHIELD_FIELD_OF_FILTER = Arrays.asList("User.loginPwd", "User.dingTalkUserId", "Department.dingDepartmentId", "ApprovalConfig.entityCode", "ApprovalConfig.runningTotal", "ApprovalConfig.completeTotal");
    @Autowired
    EntityManagerService entityManagerService;
    @Autowired
    OptionManagerService optionManagerService;
    @Autowired
    TagManagerService tagManagerService;
    @Autowired
    DatabaseService databaseService;
    @Resource
    SystemSetting systemSetting;

    public SystemManagerController() {
    }

    private static void addExtraPropertiesOfField(Field field, Map<String, String> fieldMap) {
        FieldViewModel fieldViewModel = field.getFieldViewModel();
        if ((field.getType().equals(FieldTypes.DECIMAL) || field.getType().equals(FieldTypes.INTEGER) && field.getType().equals(FieldTypes.PERCENT) || field.getType().equals(FieldTypes.MONEY)) && fieldViewModel != null) {
            fieldMap.put("precision", fieldViewModel.getPrecision() == null ? "2" : fieldViewModel.getPrecision().toString());
            fieldMap.put("min", fieldViewModel.getMinValue() == null ? null : fieldViewModel.getMinValue().toString());
            fieldMap.put("max", fieldViewModel.getMaxValue() == null ? null : fieldViewModel.getMaxValue().toString());
        }

        if (field.getType().equals(FieldTypes.AREASELECT) && fieldViewModel != null) {
            fieldMap.put("areaDataType", fieldViewModel.getAreaDataType() == null ? null : fieldViewModel.getAreaDataType().toString());
        }

        if (field.getType().equals(FieldTypes.REFERENCE)) {
            if (fieldViewModel != null) {
                fieldMap.put("searchDialogWidth", fieldViewModel.getSearchDialogWidth() == null ? "520px" : fieldViewModel.getSearchDialogWidth().toString() + "px");
            } else {
                fieldMap.put("searchDialogWidth", "520px");
            }
        }

    }

    @RequestMapping({"/getEntitySet"})
    public ResponseBean<List<Map<String, Object>>> getEntitySet() {
        List<Map<String, Object>> resultList = new ArrayList<>();
        this.entityManagerService.getMetadataManager().getEntitySet().forEach((entity) -> {
            if (!SystemEntities.isInternalEntity(entity.getName())) {
                Map<String, Object> entityMap = new LinkedHashMap<>();
                entityMap.put("name", entity.getName());
                entityMap.put("label", entity.getLabel());
                entityMap.put("entityCode", entity.getEntityCode());
                entityMap.put("systemEntityFlag", SystemEntities.isSystemEntity(entity.getName()));
                entityMap.put("detailEntityFlag", entity.isDetailEntityFlag());
                entityMap.put("layoutable", entity.isLayoutable());
                entityMap.put("listable", entity.isListable());
                entityMap.put("idFieldName", entity.getIdField().getName());
                entityMap.put("internalEntityFlag", SystemEntities.isInternalEntity(entity.getName()));
                entityMap.put("tags", entity.getTags());
                resultList.add(entityMap);
            }
        });
        return ResponseHelper.ok(resultList, "success");
    }

    @RequestMapping({"/filterEntitySet"})
    public ResponseBean<List<Map<String, Object>>> filterEntitySet(@RequestParam("keyword") String keyword) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        this.entityManagerService.getMetadataManager().getEntitySet().forEach((entity) -> {
            if (!SystemEntities.isInternalEntity(entity.getName())) {
                if (!StringUtils.isNotBlank(keyword) || entity.getName().contains(keyword) || entity.getLabel().contains(keyword) || entity.getEntityCode().toString().contains(keyword)) {
                    Map<String, Object> entityMap = new LinkedHashMap<>();
                    entityMap.put("name", entity.getName());
                    entityMap.put("label", entity.getLabel());
                    entityMap.put("entityCode", entity.getEntityCode());
                    entityMap.put("systemEntityFlag", SystemEntities.isSystemEntity(entity.getName()));
                    entityMap.put("detailEntityFlag", entity.isDetailEntityFlag());
                    entityMap.put("layoutable", entity.isLayoutable());
                    entityMap.put("listable", entity.isListable());
                    entityMap.put("idFieldName", entity.getIdField().getName());
                    entityMap.put("internalEntityFlag", SystemEntities.isInternalEntity(entity.getName()));
                    entityMap.put("tags", entity.getTags());
                    resultList.add(entityMap);
                }
            }
        });
        return ResponseHelper.ok(resultList, "success");
    }

    @RequestMapping({"/getFieldSet"})
    public ResponseBean<List<Map<String, Object>>> getFieldSet(@RequestParam("entity") String entityName) {
        Entity entity = this.entityManagerService.getMetadataManager().getEntity(entityName);
        List<Map<String, Object>> resultList = new ArrayList<>();
        entity.getFieldSet().forEach((field) -> {
            Map<String, Object> fieldMap = new HashMap<>();
            fieldMap.put("name", field.getName());
            fieldMap.put("label", field.getLabel());
            fieldMap.put("type", field.getType().getName());
            fieldMap.put("reserved", MDHelper.isReservedField(field));
            resultList.add(fieldMap);
        });
        return ResponseHelper.ok(resultList, "success");
    }

    @RequestMapping({"/getFieldListOfEntity"})
    public ResponseBean<List<Map<String, Object>>> getFieldListOfEntity(@RequestParam("entity") String entityName) {
        Entity entity = this.entityManagerService.getMetadataManager().getEntity(entityName);
        List<Map<String, Object>> resultList = new ArrayList<>();
        entity.getFieldSet().forEach((field) -> {
            Map<String, Object> fieldMap = new HashMap<>();
            fieldMap.put("name", field.getName());
            fieldMap.put("label", field.getLabel());
            fieldMap.put("creatable", field.isCreatable());
            fieldMap.put("updatable", field.isUpdatable());
            fieldMap.put("physicalName", field.getPhysicalName());
            fieldMap.put("type", field.getType().getName());
            fieldMap.put("idFieldFlag", field.isIdFieldFlag());
            fieldMap.put("nameFieldFlag", field.isNameFieldFlag());
            fieldMap.put("mainDetailFieldFlag", field.isMainDetailFieldFlag());
            if (field.getReferTo() != null && field.getReferTo().size() > 0) {
                StringBuilder referTos = new StringBuilder();
                field.getReferTo().forEach((ref) -> referTos.append(ref.getName()).append(","));
                fieldMap.put("referTo", referTos.toString());
            } else {
                fieldMap.put("referTo", null);
            }

            fieldMap.put("reserved", MDHelper.isReservedField(field));
            resultList.add(fieldMap);
        });
        return ResponseHelper.ok(resultList, "success");
    }

    @RequestMapping({"/getMDFieldList"})
    public ResponseBean<Map<String, List<Map<String, String>>>> getMDFieldList(@RequestParam("entity") String entityName) {
        Entity entity = this.entityManagerService.getMetadataManager().getEntity(entityName);
        List<Map<String, String>> fieldMapList = new ArrayList<>();
        entity.getFieldSet().forEach((field) -> {
            Map<String, String> fieldMap = new HashMap<>();
            fieldMap.put("name", field.getName());
            fieldMap.put("label", field.getLabel());
            fieldMap.put("type", field.getType().getName());
            fieldMap.put("required", field.isNullable() ? "0" : "1");
            addExtraPropertiesOfField(field, fieldMap);
            fieldMapList.add(fieldMap);
        });
        if (entity.getDetailEntitySet() != null) {
            entity.getDetailEntitySet().forEach((detailEntity) -> detailEntity.getFieldSet().forEach((field) -> {
                Map<String, String> fieldMap = new HashMap<>();
                String detailEntityLabel = detailEntity.getLabel();
                fieldMap.put("name", field.getName());
                fieldMap.put("label", detailEntityLabel + "." + field.getLabel());
                fieldMap.put("type", field.getType().getName());
                fieldMap.put("required", field.isNullable() ? "0" : "1");
                addExtraPropertiesOfField(field, fieldMap);
                fieldMap.put("detailEntity", detailEntity.getName());
                fieldMapList.add(fieldMap);
            }));
        }

        List<Map<String, String>> sfMapList = new ArrayList<>();
        if (entity.getDetailEntitySet() != null) {
            entity.getDetailEntitySet().forEach((detailEntity) -> {
                Map<String, String> entityMap = new HashMap<>();
                entityMap.put("name", detailEntity.getName());
                entityMap.put("label", detailEntity.getLabel());
                sfMapList.add(entityMap);
            });
        }

        Map<String, List<Map<String, String>>> resultMap = new HashMap<>();
        resultMap.put("fieldList", fieldMapList);
        resultMap.put("subFormList", sfMapList);
        resultMap.put("storageSetting", this.getStorageSetting());
        return ResponseHelper.ok(resultMap, "success");
    }

    public List<Map<String, String>> getStorageSetting() {
        List<Map<String, String>> resultList = new ArrayList<>();
        Map<String, String> paramMap = new HashMap<>();
        CloudStorageSetting cloudStorageSetting = this.systemSetting.getCloudStorageSetting();
        boolean cloudStorage = ObjectUtil.isNotNull(cloudStorageSetting) && cloudStorageSetting.isOpenStatus() && StringUtils.isNotBlank(cloudStorageSetting.getHost()) && StringUtils.isNotBlank(cloudStorageSetting.getBucket()) && StringUtils.isNotBlank(cloudStorageSetting.getSecretKey()) && StringUtils.isNotBlank(cloudStorageSetting.getAccessKey());
        paramMap.put("cloudStorage", cloudStorage ? "true" : "false");
        if (cloudStorage) {
            paramMap.put("cloudStorageType", "QiNiu");
        }

        System.err.println(paramMap);
        resultList.add(paramMap);
        return resultList;
    }

    @RequestMapping({"/getDetailEntityList"})
    public ResponseBean<List<Map<String, String>>> getDetailEntityList(@RequestParam("entity") String entityName) {
        List<Map<String, String>> resultList = new ArrayList<>();
        Entity entity = this.entityManagerService.getMetadataManager().getEntity(entityName);
        if (entity.getDetailEntitySet() != null) {
            entity.getDetailEntitySet().forEach((detailEntity) -> {
                Map<String, String> entityMap = new HashMap<>();
                entityMap.put("name", detailEntity.getName());
                entityMap.put("label", detailEntity.getLabel());
                resultList.add(entityMap);
            });
        }

        return ResponseHelper.ok(resultList, "success");
    }

    @RequestMapping({"/getFieldListOfFilter"})
    public ResponseBean<List<Map<String, Object>>> getFieldListOfAssociated(@RequestParam("entity") String entityName, @RequestParam(defaultValue = "true") boolean queryReference) {
        MetadataManager md = this.entityManagerService.getMetadataManager();
        Entity entity = entityName.matches("[0-9]*") ? md.getEntity(new Integer(entityName)) : md.getEntity(entityName);
        List<Map<String, Object>> resultList = new ArrayList<>();
        List<Field> resultFields = new ArrayList<>();
        List<Field> associatedFields = new ArrayList<>();
        entity.getSortedFieldSet().forEach((field) -> {
            resultFields.add(field);
            if (field.getType().getName().equals("Reference") && queryReference) {
                associatedFields.add(field);
            }

        });
        associatedFields.forEach((assField) -> {
            Entity assEntity = assField.getReferTo().iterator().next();
            assEntity.getFieldSet().forEach((field) -> {
                if (field.getType().getName().equals("Reference")) {
                    Entity fieldEntity = field.getReferTo().iterator().next();
                    if (!fieldEntity.getName().equals("User") && !fieldEntity.getName().equals("Department")) {
                        return;
                    }
                }

                if (!SHIELD_FIELD_OF_FILTER.contains(assEntity.getName() + "." + field.getName())) {
                    field = ObjectHelper.clone(field);
                    assert field != null;
                    field.setName(assField.getName() + "." + field.getName());
                    field.setLabel(assField.getLabel() + "." + field.getLabel());
                    resultFields.add(field);
                }
            });
        });
        resultFields.forEach((field) -> {
            String fieldType = field.getType().getName();
            if (!fieldType.equals("PrimaryKey") && !fieldType.equals("File") && !fieldType.equals("Picture") && !fieldType.equals("ReferenceList") && !fieldType.equals("AnyReference")) {
                Map<String, Object> fieldMap = new HashMap<>();
                fieldMap.put("name", field.getName());
                fieldMap.put("label", field.getLabel());
                fieldMap.put("type", field.getType().getName());
                if (field.getReferTo() != null) {
                    Entity fieldEntity = field.getReferTo().iterator().next();
                    fieldMap.put("referTo", fieldEntity.getName());
                }

                this.entityManagerService.putFieldOptionData(field, fieldMap);
                resultList.add(fieldMap);
            }
        });
        return ResponseHelper.ok(resultList, "success");
    }

    @RequestMapping({"/getEntityProps"})
    public ResponseBean<Map<String, Object>> getEntityProps(@RequestParam("entity") String entityName) {
        Map<String, Object> resultMap = new HashMap<>();
        Entity entity = this.entityManagerService.getMetadataManager().getEntity(entityName);
        resultMap.put("name", entity.getName());
        resultMap.put("label", entity.getLabel());
        resultMap.put("entityCode", entity.getEntityCode());
        resultMap.put("physicalName", entity.getPhysicalName());
        if (entity.getNameField() == null) {
            resultMap.put("nameField", null);
        } else {
            resultMap.put("nameField", entity.getNameField().getLabel());
        }

        resultMap.put("detailEntityFlag", entity.isDetailEntityFlag());
        resultMap.put("mainEntity", entity.getMainEntity());
        resultMap.put("layoutable", entity.isLayoutable());
        resultMap.put("listable", entity.isListable());
        resultMap.put("authorizable", entity.isAuthorizable());
        resultMap.put("assignable", entity.isAssignable());
        resultMap.put("shareable", entity.isShareable());
        resultMap.put("tags", entity.getTags());
        return ResponseHelper.ok(resultMap, "success");
    }

    @RequestMapping({"/getAllTagsOfEntity"})
    public ResponseBean<List<String>> getAllTagsOfEntity() {
        List<String> tagsList = this.entityManagerService.getAllTagsOfEntity();
        return ResponseHelper.ok(tagsList, "success");
    }

    @RequestMapping({"/createEntity"})
    @SystemRight(SystemRightEnum.ENTITY_MANAGE)
    public ResponseBean<Entity> createEntity(@RequestBody Entity entity, @RequestParam("mainEntity") String mainEntityName) {
        if (!StringUtils.isBlank(entity.getName()) && !StringUtils.isBlank(entity.getLabel())) {
            if (MysqlKeywordHelper.containsKeyword(entity.getName())) {
                throw new IllegalArgumentException("实体名称不能使用数据库关键字！");
            } else {
                this.entityManagerService.createEntity(entity, mainEntityName);
                return ResponseHelper.ok(entity, "success");
            }
        } else {
            throw new IllegalArgumentException("name or label must be not null");
        }
    }

    @RequestMapping({"/updateEntityLabel"})
    @SystemRight(SystemRightEnum.ENTITY_MANAGE)
    public ResponseBean<Boolean> updateEntityLabel(@RequestParam("entity") String entityName, @RequestParam("entityLabel") String entityLabel) {
        Entity entity = this.entityManagerService.getMetadataManager().getEntity(entityName);
        if (!entity.getLabel().equals(entityLabel.trim())) {
            entity.setLabel(entityLabel.trim());
            this.entityManagerService.updateEntity(entity);
        }

        return ResponseHelper.ok(Boolean.TRUE, "success");
    }

    @RequestMapping({"/updateEntityTags"})
    @SystemRight(SystemRightEnum.ENTITY_MANAGE)
    public ResponseBean<Boolean> updateEntityTags(@RequestParam("entity") String entityName, @RequestParam("tags") String tags) {
        Entity entity = this.entityManagerService.getMetadataManager().getEntity(entityName);
        if (StringUtils.isNotBlank(tags)) {
            entity.setTags(tags);
        } else {
            entity.setTags(null);
        }

        this.entityManagerService.updateEntity(entity);
        return ResponseHelper.ok(Boolean.TRUE, "success");
    }

    @RequestMapping({"/entityCanBeDeleted"})
    @SystemRight(SystemRightEnum.ENTITY_MANAGE)
    public ResponseBean<Boolean> entityCanBeDeleted(@RequestParam("entity") String entityName) {
        Boolean checkResult = this.entityManagerService.entityCanBeDeleted(entityName);
        return ResponseHelper.ok(checkResult, "success");
    }

    @RequestMapping({"/deleteEntity"})
    @SystemRight(SystemRightEnum.ENTITY_MANAGE)
    public ResponseBean<Boolean> deleteEntity(@RequestParam("entity") String entityName) throws JsonProcessingException {
        boolean found = this.entityManagerService.getMetadataManager().containsEntity(entityName);
        if (!found) {
            throw new IllegalArgumentException("实体" + entityName + "不存在!");
        } else {
            this.entityManagerService.deleteEntity(entityName);
            return ResponseHelper.ok(Boolean.TRUE, "success");
        }
    }

    @RequestMapping({"/getTextFieldListOfEntity"})
    public ResponseBean<List<Map<String, Object>>> getEntityTextFieldList(@RequestParam("entity") String entityName) {
        Entity entity = this.entityManagerService.getMetadataManager().getEntity(entityName);
        List<Map<String, Object>> resultList = new ArrayList<>();
        entity.getFieldSet().forEach((field) -> {
            if (field.getType() == FieldTypes.TEXT || field.getType() == FieldTypes.TEXTAREA) {
                Map<String, Object> fieldMap = new HashMap<>();
                fieldMap.put("name", field.getName());
                fieldMap.put("label", field.getLabel());
                fieldMap.put("nameFieldFlag", field.isNameFieldFlag());
                resultList.add(fieldMap);
            }

        });
        return ResponseHelper.ok(resultList, "success");
    }

    @RequestMapping({"/updateEntityNameField"})
    @SystemRight(SystemRightEnum.ENTITY_MANAGE)
    public ResponseBean<Boolean> updateEntityNameField(@RequestParam("entity") String entityName, @RequestParam("nameField") String nameFieldName) {
        Entity entity = this.entityManagerService.getMetadataManager().getEntity(entityName);
        Field newNameField = entity.getField(nameFieldName);
        this.entityManagerService.updateEntityNameField(entity, newNameField);
        return ResponseHelper.ok(Boolean.TRUE, "success");
    }

    @RequestMapping({"/addField"})
    @SystemRight(SystemRightEnum.ENTITY_MANAGE)
    public ResponseBean<Field> addPlainField(@RequestBody Field field, @RequestParam("entity") String entityName) {
        if (!StringUtils.isBlank(field.getName()) && !StringUtils.isBlank(field.getLabel())) {
            field.setPhysicalName("c_" + field.getName());
            Entity entity = this.entityManagerService.getMetadataManager().getEntity(entityName);
            field.setOwner(entity);
            this.entityManagerService.createPlainField(entity.getEntityCode(), field);
            return ResponseHelper.ok(field, "success");
        } else {
            throw new IllegalArgumentException("name or label must be not null");
        }
    }

    @RequestMapping({"/updateField"})
    @SystemRight(SystemRightEnum.ENTITY_MANAGE)
    public ResponseBean<Field> updatePlainField(@RequestBody Field field, @RequestParam("entity") String entityName) {
        if (!StringUtils.isBlank(field.getName()) && !StringUtils.isBlank(field.getLabel())) {
            if (this.entityManagerService.isReservedField(field.getName(), entityName)) {
                throw new IllegalArgumentException("系统字段不可修改!");
            } else {
                Entity entity = this.entityManagerService.getMetadataManager().getEntity(entityName);
                this.entityManagerService.updatePlainField(entity.getEntityCode(), field);
                return ResponseHelper.ok(field, "success");
            }
        } else {
            throw new IllegalArgumentException("name or label must be not null");
        }
    }

    @RequestMapping({"/addOptionField"})
    @SystemRight(SystemRightEnum.ENTITY_MANAGE)
    public ResponseBean<Field> addOptionField(@RequestBody OptionRequestBody requestBody, @RequestParam("entity") String entityName) {
        Field field = requestBody.getField();
        if (!StringUtils.isBlank(field.getName()) && !StringUtils.isBlank(field.getLabel())) {
            field.setPhysicalName("c_" + field.getName());
            Entity entity = this.entityManagerService.getMetadataManager().getEntity(entityName);
            field.setOwner(entity);
            this.entityManagerService.createOptionField(entity.getEntityCode(), field, requestBody.getOptionList());
            return ResponseHelper.ok(field, "success");
        } else {
            throw new IllegalArgumentException("name or label must be not null");
        }
    }

    @RequestMapping({"/updateOptionField"})
    @SystemRight(SystemRightEnum.ENTITY_MANAGE)
    public ResponseBean<Field> updateOptionField(@RequestBody OptionRequestBody requestBody, @RequestParam("entity") String entityName) {
        Field field = requestBody.getField();
        if (!StringUtils.isBlank(field.getName()) && !StringUtils.isBlank(field.getLabel())) {
            Entity entity = this.entityManagerService.getMetadataManager().getEntity(entityName);
            field.setOwner(entity);
            this.entityManagerService.updateOptionField(entity.getEntityCode(), field, requestBody.getOptionList());
            return ResponseHelper.ok(field, "success");
        } else {
            throw new IllegalArgumentException("name or label must be not null");
        }
    }

    @RequestMapping({"/addTagField"})
    @SystemRight(SystemRightEnum.ENTITY_MANAGE)
    public ResponseBean<Field> addTagField(@RequestBody TagRequestBody requestBody, @RequestParam("entity") String entityName) {
        Field field = requestBody.getField();
        if (!StringUtils.isBlank(field.getName()) && !StringUtils.isBlank(field.getLabel())) {
            field.setPhysicalName("c_" + field.getName());
            Entity entity = this.entityManagerService.getMetadataManager().getEntity(entityName);
            field.setOwner(entity);
            this.entityManagerService.createTagField(entity.getEntityCode(), field, requestBody.getTagList());
            return ResponseHelper.ok(field, "success");
        } else {
            throw new IllegalArgumentException("name or label must be not null");
        }
    }

    @RequestMapping({"/updateTagField"})
    @SystemRight(SystemRightEnum.ENTITY_MANAGE)
    public ResponseBean<Field> updateTagField(@RequestBody TagRequestBody requestBody, @RequestParam("entity") String entityName) {
        Field field = requestBody.getField();
        if (!StringUtils.isBlank(field.getName()) && !StringUtils.isBlank(field.getLabel())) {
            Entity entity = this.entityManagerService.getMetadataManager().getEntity(entityName);
            field.setOwner(entity);
            this.entityManagerService.updateTagField(entity.getEntityCode(), field, requestBody.getTagList());
            return ResponseHelper.ok(field, "success");
        } else {
            throw new IllegalArgumentException("name or label must be not null");
        }
    }

    @RequestMapping({"/addRefField"})
    @SystemRight(SystemRightEnum.ENTITY_MANAGE)
    public ResponseBean<Field> addReferenceField(@RequestBody Field field, @RequestParam("entity") String entityName, @RequestParam("refEntity") String refEntityName) {
        if (!StringUtils.isBlank(field.getName()) && !StringUtils.isBlank(field.getLabel())) {
            field.setPhysicalName("c_" + field.getName());
            Entity entity = this.entityManagerService.getMetadataManager().getEntity(entityName);
            final Entity refEntity = this.entityManagerService.getMetadataManager().getEntity(refEntityName);
            Set<Entity> referTo = new LinkedHashSet<Entity>() {
                {
                    this.add(refEntity);
                }
            };
            field.setReferTo(referTo);
            field.setOwner(entity);
            this.entityManagerService.createPlainField(entity.getEntityCode(), field);
            return ResponseHelper.ok(field, "success");
        } else {
            throw new IllegalArgumentException("name or label must be not null");
        }
    }

    @RequestMapping({"/updateRefField"})
    @SystemRight(SystemRightEnum.ENTITY_MANAGE)
    public ResponseBean<Field> updateReferenceField(@RequestBody Field field, @RequestParam("entity") String entityName, @RequestParam("refEntity") String refEntityName) {
        Entity entity = this.entityManagerService.getMetadataManager().getEntity(entityName);
        this.entityManagerService.updatePlainField(entity.getEntityCode(), field);
        return ResponseHelper.ok(field, "success");
    }

    @RequestMapping({"/addAnyRefField"})
    @SystemRight(SystemRightEnum.ENTITY_MANAGE)
    public ResponseBean<Field> addAnyReferenceField(@RequestBody Field field, @RequestParam("entity") String entityName, @RequestParam("referTo") String referToEntities) {
        if (!StringUtils.isBlank(field.getName()) && !StringUtils.isBlank(field.getLabel())) {
            field.setPhysicalName("c_" + field.getName());
            Entity entity = this.entityManagerService.getMetadataManager().getEntity(entityName);
            field.setOwner(entity);
            String[] referToArray = referToEntities.split(",");
            Set<Entity> referTo = new LinkedHashSet<>();

            for (String refEntityName : referToArray) {
                if (StringUtils.isNotBlank(refEntityName)) {
                    Entity refEntity = this.entityManagerService.getMetadataManager().getEntity(refEntityName);
                    referTo.add(refEntity);
                }
            }

            field.setReferTo(referTo);
            this.entityManagerService.createPlainField(entity.getEntityCode(), field);
            return ResponseHelper.ok(field, "success");
        } else {
            throw new IllegalArgumentException("name or label must be not null");
        }
    }

    @RequestMapping({"/updateAnyRefField"})
    @SystemRight(SystemRightEnum.ENTITY_MANAGE)
    public ResponseBean<Field> updateAnyReferenceField(@RequestBody Field field, @RequestParam("entity") String entityName, @RequestParam("referTo") String referToEntities) {
        Entity entity = this.entityManagerService.getMetadataManager().getEntity(entityName);
        String[] referToArray = referToEntities.split(",");
        Set<Entity> referTo = new LinkedHashSet<>();

        for (String refEntityName : referToArray) {
            if (StringUtils.isNotBlank(refEntityName)) {
                Entity refEntity = this.entityManagerService.getMetadataManager().getEntity(refEntityName);
                referTo.add(refEntity);
            }
        }

        field.setReferTo(referTo);
        this.entityManagerService.updatePlainField(entity.getEntityCode(), field);
        return ResponseHelper.ok(field, "success");
    }

    @RequestMapping({"/fieldCanBeDeleted"})
    @SystemRight(SystemRightEnum.ENTITY_MANAGE)
    public ResponseBean<Boolean> fieldCanBeDeleted(@RequestParam("entity") String entityName, @RequestParam("field") String fieldName) {
        Boolean checkResult = this.entityManagerService.fieldCanBeDeleted(entityName, fieldName);
        return ResponseHelper.ok(checkResult, "success");
    }

    @RequestMapping({"/fieldCanBeEdited"})
    @SystemRight(SystemRightEnum.ENTITY_MANAGE)
    public ResponseBean<Boolean> fieldCanBeEdited(@RequestParam("entity") String entityName, @RequestParam("field") String fieldName) {
        Boolean checkResult = this.entityManagerService.fieldCanBeEdited(entityName, fieldName);
        return ResponseHelper.ok(checkResult, "success");
    }

    @RequestMapping({"/deleteField"})
    @SystemRight(SystemRightEnum.ENTITY_MANAGE)
    public ResponseBean<Boolean> deleteField(@RequestParam("entity") String entityName, @RequestParam("field") String fieldName) throws IllegalAccessException {
        this.entityManagerService.deleteField(entityName, fieldName);
        return ResponseHelper.ok(Boolean.TRUE, "success");
    }

    @RequestMapping({"/getRefFieldExtras"})
    public ResponseBean<Map<String, Object>> getReferenceFieldExtras(@RequestParam("entity") String entityName, @RequestParam("field") String fieldName) {
        Map<String, Object> resultMap = new HashMap<>();
        Entity entity = this.entityManagerService.getMetadataManager().getEntity(entityName);
        Field field = entity.getField(fieldName);
        if (field.getType() != FieldTypes.REFERENCE) {
            return ResponseHelper.fail(resultMap, "field type mismatch");
        } else {
            Entity refEntity = field.getReferTo().iterator().next();
            resultMap.put("refEntityName", refEntity.getName());
            resultMap.put("refEntityLabel", refEntity.getLabel());
            resultMap.put("refEntityFullName", refEntity.getLabel() + "(" + refEntity.getName() + ")");
            resultMap.put("currentRefEntity", refEntity.getName());
            ReferenceModel referenceModel = field.getReferenceSetting().get(0);
            StringBuilder refEntityAndFieldsSb = (new StringBuilder()).append(refEntity.getLabel()).append("[");
            List<Map<String, String>> selectedFieldItems = new ArrayList<>();

            for (String fldName : referenceModel.getFieldList()) {
                Map<String, String> selectedFieldMap = new HashMap<>();
                String selectedFieldLabel = refEntity.getField(fldName).getLabel();
                refEntityAndFieldsSb.append(selectedFieldLabel).append(",");
                selectedFieldMap.put("name", fldName);
                selectedFieldMap.put("label", selectedFieldLabel);
                selectedFieldItems.add(selectedFieldMap);
            }

            refEntityAndFieldsSb.append("]");
            resultMap.put("refEntityAndFields", refEntityAndFieldsSb.toString());
            resultMap.put("selectedFieldItems", selectedFieldItems);
            List<Map<String, Object>> fieldMapList = new ArrayList<>();
            refEntity.getFieldSet().forEach((fieldItem) -> {
                if (fieldItem.getType() != FieldTypes.PRIMARYKEY) {
                    Map<String, Object> fieldMap = new HashMap<>();
                    fieldMap.put("name", fieldItem.getName());
                    fieldMap.put("label", fieldItem.getLabel());
                    fieldMap.put("type", fieldItem.getType().getName());
                    fieldMap.put("reserved", MDHelper.isReservedField(field));
                    fieldMapList.add(fieldMap);
                }

            });
            resultMap.put("fieldItems", fieldMapList);
            return ResponseHelper.ok(resultMap, "success");
        }
    }

    @RequestMapping({"/getField"})
    public ResponseBean<Field> getFieldObject(@RequestParam("entity") String entityName, @RequestParam("field") String fieldName) {
        Field field = this.entityManagerService.getMetadataManager().getEntity(entityName).getField(fieldName);
        return ResponseHelper.ok(field, "success");
    }

    @RequestMapping({"/getOptionFields"})
    public ResponseBean<List<Map<String, Object>>> getOptionFields() {
        List<Map<String, Object>> resultList = new ArrayList<>();
        Collection<Entity> entityCollection = this.entityManagerService.getMetadataManager().getEntitySet();
        boolean foundFlag;

        for (Entity entity : entityCollection) {
            Collection<Field> fieldCollection = entity.getFieldSet();
            Map<String, Object> entityMap = new HashMap<>();
            entityMap.put("entityName", entity.getName());
            entityMap.put("entityLabel", entity.getLabel());
            List<Map<String, String>> fieldMapList = new ArrayList<>();
            foundFlag = false;

            for (Field field : fieldCollection) {
                if (field.getType() == FieldTypes.OPTION) {
                    foundFlag = true;
                    Map<String, String> fieldMap = new HashMap<>();
                    fieldMap.put("fieldName", field.getName());
                    fieldMap.put("fieldLabel", field.getLabel());
                    fieldMapList.add(fieldMap);
                }
            }

            if (foundFlag) {
                entityMap.put("fieldList", fieldMapList);
                resultList.add(entityMap);
            }
        }

        return ResponseHelper.ok(resultList, "success");
    }

    @RequestMapping({"/getOptionItems"})
    public ResponseBean<List<Map<String, Object>>> getOptionItems(@RequestParam("entity") String entityName, @RequestParam("field") String fieldName) {
        List<OptionModel> optionModelList = this.optionManagerService.getOptionList(entityName, fieldName);
        List<Map<String, Object>> resultList = new ArrayList<>();
        optionModelList.forEach((om) -> {
            Map<String, Object> optionMap = new HashMap<>();
            optionMap.put("label", om.getLabel());
            optionMap.put("value", om.getValue());
            optionMap.put("saved", Boolean.TRUE);
            resultList.add(optionMap);
        });
        return ResponseHelper.ok(resultList, "success");
    }

    @RequestMapping({"/saveOptionItems"})
    @SystemRight(SystemRightEnum.OPTION_MANAGE)
    public ResponseBean<Boolean> saveOptionItems(@RequestParam("entity") String entityName, @RequestParam("field") String fieldName, @RequestBody List<Map<String, Object>> optionItems) {
        List<KeyValueEntry<String, Integer>> optionList = new ArrayList<>();

        for (Map<String, Object> optionItem : optionItems) {
            KeyValueEntry<String, Integer> kv = new KeyValueEntry(optionItem.get("label"), optionItem.get("value"));
            optionList.add(kv);
        }

        boolean result = this.optionManagerService.saveOptionList(entityName, fieldName, optionList);
        return result ? ResponseHelper.ok(Boolean.TRUE, "success") : ResponseHelper.fail(Boolean.FALSE, "request failed");
    }

    @RequestMapping({"/optionCanBeDeleted"})
    @SystemRight(SystemRightEnum.OPTION_MANAGE)
    public ResponseBean<Boolean> optionCanBeDeleted(@RequestParam("entity") String entityName, @RequestParam("field") String fieldName, @RequestParam("value") String value) {
        boolean canBeDeleted = this.optionManagerService.optionCanBeDeleted(entityName, fieldName, value);
        return ResponseHelper.ok(canBeDeleted, "success");
    }

    @RequestMapping({"/getTagFields"})
    public ResponseBean<List<Map<String, Object>>> getTagFields() {
        List<Map<String, Object>> resultList = new ArrayList<>();
        Collection<Entity> entityCollection = this.entityManagerService.getMetadataManager().getEntitySet();
        boolean foundFlag;

        for (Entity entity : entityCollection) {
            Collection<Field> fieldCollection = entity.getFieldSet();
            Map<String, Object> entityMap = new HashMap<>();
            entityMap.put("entityName", entity.getName());
            entityMap.put("entityLabel", entity.getLabel());
            List<Map<String, String>> fieldMapList = new ArrayList<>();
            foundFlag = false;

            for (Field field : fieldCollection) {
                if (field.getType() == FieldTypes.TAG) {
                    foundFlag = true;
                    Map<String, String> fieldMap = new HashMap<>();
                    fieldMap.put("fieldName", field.getName());
                    fieldMap.put("fieldLabel", field.getLabel());
                    fieldMapList.add(fieldMap);
                }
            }

            if (foundFlag) {
                entityMap.put("fieldList", fieldMapList);
                resultList.add(entityMap);
            }
        }

        return ResponseHelper.ok(resultList, "success");
    }

    @RequestMapping({"/getTagItems"})
    public ResponseBean<List<Map<String, Object>>> getTagItems(@RequestParam("entity") String entityName, @RequestParam("field") String fieldName) {
        List<TagModel> tagModelList = this.tagManagerService.getTagList(entityName, fieldName);
        List<Map<String, Object>> resultList = new ArrayList<>();
        tagModelList.forEach((tm) -> {
            Map<String, Object> tagMap = new HashMap<>();
            tagMap.put("label", tm.getValue());
            tagMap.put("value", tm.getValue());
            tagMap.put("saved", Boolean.TRUE);
            resultList.add(tagMap);
        });
        return ResponseHelper.ok(resultList, "success");
    }

    @RequestMapping({"/saveTagItems"})
    @SystemRight(SystemRightEnum.TAG_MANAGE)
    public ResponseBean<Boolean> saveTagItems(@RequestParam("entity") String entityName, @RequestParam("field") String fieldName, @RequestBody List<Map<String, Object>> tagItems) {
        List<String> tagModelList = new ArrayList<>();
        tagItems.forEach((ti) -> tagModelList.add((String) ti.get("value")));
        boolean result = this.tagManagerService.saveTagList(entityName, fieldName, tagModelList);
        return result ? ResponseHelper.ok(Boolean.TRUE, "success") : ResponseHelper.fail(Boolean.FALSE, "request failed");
    }

    @RequestMapping({"/genePojo"})
    public ResponseBean<String> generatePojo(@RequestParam("entity") String entityName) {
        return null;
    }

    @RequestMapping({"/getNavMenus"})
    public ResponseBean<String> getNavMenus() {
        String menus = "{\"menu\":[{\"name\":\"home\",\"path\":\"/home\",\"meta\":{\"title\":\"首页\",\"icon\":\"el-icon-eleme-filled\",\"type\":\"menu\"},\"children\":[{\"name\":\"dashboard\",\"path\":\"/dashboard\",\"meta\":{\"title\":\"控制台\",\"icon\":\"el-icon-menu\",\"affix\":true},\"component\":\"home\"},{\"name\":\"userCenter\",\"path\":\"/usercenter\",\"meta\":{\"title\":\"个人信息\",\"icon\":\"el-icon-user\"},\"component\":\"userCenter\"}]},{\"name\":\"other\",\"path\":\"/other\",\"meta\":{\"title\":\"其他\",\"icon\":\"el-icon-more-filled\",\"type\":\"menu\"},\"children\":[{\"path\":\"/other/directive\",\"name\":\"directive\",\"meta\":{\"title\":\"指令\",\"icon\":\"el-icon-price-tag\",\"type\":\"menu\"},\"component\":\"other/directive\"},{\"path\":\"/other/viewTags\",\"name\":\"viewTags\",\"meta\":{\"title\":\"标签操作\",\"icon\":\"el-icon-files\",\"type\":\"menu\"},\"component\":\"other/viewTags\",\"children\":[{\"path\":\"/other/fullpage\",\"name\":\"fullpage\",\"meta\":{\"title\":\"整页路由\",\"icon\":\"el-icon-monitor\",\"fullpage\":true,\"hidden\":true,\"type\":\"menu\"},\"component\":\"other/fullpage\"}]}]}],\"permissions\":[\"list.add\",\"list.edit\",\"list.delete\"]}";
        return new ResponseBean<>(200, null, "success", menus);
    }

    @RequestMapping({"/backupDB"})
    @SystemRight(SystemRightEnum.ADMIN_ROLE)
    public ResponseBean<String> backupDB() {
        if (this.systemSetting.getTrialVersionFlag()) {
            throw new ServiceException("当前为演示版本，功能暂不开放！");
        } else {
            this.databaseService.backupDatabase();
            return new ResponseBean<>(200, null, "success", null);
        }
    }

    @RequestMapping({"/backup/log"})
    @SystemRight(SystemRightEnum.TRIGGER_LOG_MANAGE)
    public ResponseBean<ListQueryResult> queryListMap(@RequestBody ListQueryRequestBody requestBody) {
        requestBody.setMainEntity("BackupDatabase");
        return new ResponseBean<>(200, null, "success", this.databaseService.queryListMap(requestBody));
    }

    @RequestMapping({"/queryEntityList"})
    public ResponseBean<List<Map<String, Object>>> queryEntityList(int entityCode, boolean queryMain, boolean queryReference, boolean queryReferenced, boolean querySystem, boolean queryBuiltIn) {
        List<Map<String, Object>> resultList = this.entityManagerService.queryEntityList(entityCode, queryMain, queryReference, queryReferenced, querySystem, queryBuiltIn);
        return ResponseHelper.ok(resultList, "success");
    }

    @GetMapping({"/keyWordCheck"})
    public ResponseBean<Boolean> keyWordCheck(String keyword) {
        return ResponseHelper.ok(MysqlKeywordHelper.containsKeyword(keyword));
    }

    @GetMapping({"/hasDetailEntity"})
    public ResponseBean<Boolean> hasDetailEntity(@RequestParam("entity") String entityName) {
        Entity entity = EntityHelper.getEntity(entityName);
        if (entity == null) {
            throw new IllegalArgumentException("实体" + entityName + "不存在!");
        } else {
            Entity mainEntity = entity.getMainEntity();
            Set<Entity> detailEntitySet = entity.getDetailEntitySet();
            return mainEntity == null && (detailEntitySet == null || detailEntitySet.isEmpty()) ? ResponseHelper.ok(false) : ResponseHelper.ok(true);
        }
    }

    @PostMapping({"/copyEntity"})
    public ResponseBean<Boolean> copyEntity(@RequestBody CopyEntityBody copyEntityBody) {
        this.entityManagerService.copyEntity(copyEntityBody);
        return ResponseHelper.ok();
    }
}
