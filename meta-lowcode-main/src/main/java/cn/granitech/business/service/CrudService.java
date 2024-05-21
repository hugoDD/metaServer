//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cn.granitech.business.service;

import cn.granitech.business.plugins.trigger.TriggerLock;
import cn.granitech.business.plugins.trigger.TriggerService;
import cn.granitech.exception.EntityRecordNotFoundException;
import cn.granitech.exception.ServiceException;
import cn.granitech.util.EntityHelper;
import cn.granitech.util.FilterHelper;
import cn.granitech.util.JsonHelper;
import cn.granitech.util.MetadataHelper;
import cn.granitech.variantorm.constant.ReservedFields;
import cn.granitech.variantorm.metadata.FieldType;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.metadata.fieldtype.FieldTypes;
import cn.granitech.variantorm.persistence.DataQuery;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.persistence.RecordQuery;
import cn.granitech.variantorm.pojo.*;
import cn.granitech.web.enumration.TriggerWhenEnum;
import cn.granitech.web.pojo.*;
import cn.granitech.web.pojo.layout.FieldProps;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CrudService extends BaseService {
    private static final String RECORD_APPROVAL_STATE = "recordApprovalState";
    @Autowired
    PersistenceManager pm;
    @Autowired
    UserService userService;
    @Autowired
    OptionManagerService optionManagerService;
    @Autowired
    TagManagerService tagManagerService;
    @Resource
    RevisionHistoryService revisionHistoryService;
    @Resource
    RightManager rightManager;
    @Resource
    ShareAccessService shareAccessService;
    @Resource
    NotificationService notificationService;
    @Resource
    ApprovalService approvalService;
    @Resource
    PluginService pluginService;

    public CrudService() {
    }

    private static Map<String, String> buildColumnMap(Field columnFld) {
        FieldType fldType = columnFld.getType();
        Map<String, String> columnMap = new HashMap<>();
        columnMap.put("prop", columnFld.getName());
        columnMap.put("label", columnFld.getLabel());
        columnMap.put("width", "160");
        if (fldType != FieldTypes.INTEGER && fldType != FieldTypes.DECIMAL && fldType != FieldTypes.MONEY && fldType != FieldTypes.PERCENT && fldType != FieldTypes.DATE && fldType != FieldTypes.DATETIME) {
            columnMap.put("align", "center");
        } else {
            columnMap.put("align", "right");
        }

        columnMap.put("type", fldType.getName());
        return columnMap;
    }

    public <T extends BasePojo> T newPojo(String entityName, Class<T> pojoClass) throws IllegalAccessException, InstantiationException {
        T pojo = pojoClass.newInstance();
        EntityRecord entityRecord = this.newRecord(entityName);
        pojo.setEntityRecord(entityRecord);
        return pojo;
    }

    public <T extends BasePojo> ID create(T pojo) {
        return this.create(pojo.getEntityRecord());
    }

    @Transactional(
            propagation = Propagation.NESTED
    )
    public ID create(EntityRecord record) {
        if (!this.callerContext.checkCreateRight(record)) {
            throw new ServiceException("当前登录用户没有权限！");
        } else {
            super.createRecord(record);
            TriggerService triggerService = this.pluginService.getTriggerService();
            if (triggerService != null) {
                triggerService.executeTrigger(TriggerWhenEnum.CREATE, record.id(), triggerService.getTriggerLock());
            }

            return record.id();
        }
    }

    public <T extends BasePojo> T update(T pojo) {
        this.update(pojo.getEntityRecord());
        return pojo;
    }

    @Transactional(
            propagation = Propagation.NESTED
    )
    public EntityRecord update(EntityRecord record) {
        EntityRecord entityRecord = this.queryById(record.id());
        if (entityRecord == null) {
            throw new EntityRecordNotFoundException("实体记录不存在，可能已被删除: " + record.id().toString());
        } else if (!this.callerContext.checkUpdateRight(entityRecord) && !this.approvalService.containsApprover(record.id())) {
            throw new ServiceException("当前登录用户没有权限！");
        } else {
            Map<String, Object> updateMap = EntityHelper.getUpdateMap(entityRecord.getEntity(), record.getValuesMap(), entityRecord.getValuesMap());
            this.approvalService.checkFieldsApprovalRight(record);
            super.updateRecord(record);
            this.revisionHistoryService.recordHistory(record.id(), 1, entityRecord.getValuesMap(), updateMap);
            TriggerService triggerService = this.pluginService.getTriggerService();
            if (triggerService != null) {
                TriggerLock triggerLock = triggerService.getTriggerLock();
                triggerLock.setUpdateDataCache(updateMap);
                triggerService.executeTrigger(TriggerWhenEnum.UPDATE, record.id(), triggerLock);
            }

            return entityRecord;
        }
    }

    public int delete(List<String> recordIds, List<Cascade> cascades, String deletedWith) {
        if (recordIds != null && recordIds.size() != 0) {
            int successCount = 0;
            int failCount = 0;

            for (String id : recordIds) {
                ID recordId = ID.valueOf(id);
                List<EntityRecord> entityRecords = this.queryCascadeRecordList(recordId, cascades);
                int status = this.delete(recordId, deletedWith);
                successCount += status == 1 ? 1 : 0;
                failCount += status == -1 ? 1 : 0;

                for (Iterator<EntityRecord> var11 = entityRecords.iterator(); var11.hasNext(); successCount += status == 1 ? 1 : 0) {
                    EntityRecord entityRecord = var11.next();
                    status = this.delete(entityRecord.id(), "级联删除");
                }
            }

            if (recordIds.size() == failCount) {
                throw new ServiceException("当前登录用户权限不足！");
            } else {
                return successCount;
            }
        } else {
            throw new ServiceException("参数异常！");
        }
    }

    public boolean delete(ID recordId) {
        return this.delete(recordId, "手动删除") == 1;
    }

    @Transactional(
            propagation = Propagation.NESTED
    )
    public int delete(ID recordId, String deletedWith) {
        EntityRecord entityRecord = this.queryById(recordId);
        if (!this.callerContext.checkDeleteRight(entityRecord)) {
            return -1;
        } else {
            Entity entity = entityRecord.getEntity();
            this.approvalService.checkApprovalDelRight(entityRecord);
            TriggerService triggerService = this.pluginService.getTriggerService();
            if (triggerService != null) {
                TriggerLock triggerLock = triggerService.getTriggerLock();
                triggerLock.setDeleteRecordIdList(triggerService.getDeleteRecordIdList());
                triggerService.executeTrigger(TriggerWhenEnum.DELETE, recordId, triggerLock);
            }

            super.deleteRecord(recordId);
            List<String> deletedIdList = new ArrayList<>();
            if (entity.getDetailEntitySet() != null) {
                entity.getDetailEntitySet().forEach((detailEntity) -> {
                    String filter = String.format("%s = '%s'", detailEntity.getMainDetailField().getName(), recordId.getId());
                    List<String> deleteList = super.batchDeleteRecord(detailEntity.getName(), filter);
                    deletedIdList.addAll(deleteList);
                });
            }

            this.insertRecycleBin(recordId, entityRecord.getName() == null ? recordId.getId() : entityRecord.getName(), deletedIdList, deletedWith);
            return 1;
        }
    }

    public List<Map<String, Object>> queryEntityFields(int entityCode, boolean queryReference, boolean queryReserved, boolean firstReference) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        Entity entity = this.pm.getMetadataManager().getEntity(entityCode);
        ArrayList<Field> referenceFields = new ArrayList<>();
        entity.getSortedFieldSet().forEach((field) -> {
            if (field.getType() != FieldTypes.REFERENCELIST && field.getType() != FieldTypes.ANYREFERENCE && field.getType() != FieldTypes.PRIMARYKEY) {
                if (!queryReserved && ReservedFields.isReservedField(entity.getName(), field.getName())) {
                    if (queryReference) {
                        resultList.add(EntityHelper.field2Map(field));
                    }

                } else if (field.getType() == FieldTypes.REFERENCE) {
                    if (firstReference) {
                        resultList.add(EntityHelper.field2Map(field));
                    }

                    if (queryReference) {
                        referenceFields.add(field);
                        resultList.add(EntityHelper.field2Map(field));
                    }

                } else {
                    resultList.add(EntityHelper.field2Map(field));
                }
            }
        });
        if (queryReference) {
            referenceFields.forEach((field) -> {
                Entity referEntity = field.getReferTo().iterator().next();
                referEntity.getFieldSet().forEach((referEntityField) -> {
                    if (referEntityField.getType() != FieldTypes.REFERENCELIST && referEntityField.getType() != FieldTypes.ANYREFERENCE && referEntityField.getType() != FieldTypes.PRIMARYKEY && referEntityField.getType() != FieldTypes.REFERENCE) {
                        if (queryReserved || !ReservedFields.isReservedField(referEntity.getName(), referEntityField.getName())) {
                            resultList.add(EntityHelper.field2Map(referEntityField, field));
                        }
                    }
                });
            });
        }

        return resultList;
    }

    public FormQueryResult saveRecord(String entityName, String recordId, Object data) {
        return this.saveRecord(entityName, recordId, JsonHelper.writeObjectAsMap(data));
    }

    @Transactional(
            propagation = Propagation.NESTED
    )
    public FormQueryResult saveRecord(String entityName, String recordId, Map<String, Object> dataMap) {
        Entity entity = this.pm.getMetadataManager().getEntity(entityName);
        boolean isCreated = StringUtils.isBlank(recordId);
        entity.getFieldSet().forEach((field) -> {
            if ((isCreated && !field.isCreatable()) || (!isCreated && !field.isUpdatable())) {
                dataMap.remove(field.getName());
            }

        });
        EntityRecord entityRecord = this.newRecord(entityName);
        EntityHelper.formatFieldValue(entityRecord, dataMap);
        if (!isCreated) {
            entityRecord.setFieldValue(entity.getIdField().getName(), ID.valueOf(recordId));
            this.update(entityRecord);
        } else {
            recordId = this.create(entityRecord).toString();
        }

        Iterator<Entity> var7 = entity.getDetailEntitySet().iterator();

        while (true) {
            Entity detailEntity;
            do {
                if (!var7.hasNext()) {
                    return new FormQueryResult(null, null, this.queryById(ID.valueOf(recordId)), null, null);
                }

                detailEntity = var7.next();
            } while (!dataMap.containsKey(detailEntity.getName()));

            String idFieldName = detailEntity.getIdField().getName();
            String filter = String.format("%s = '%s'", detailEntity.getMainDetailField().getName(), recordId);
            List<EntityRecord> recordList = this.queryListRecord(detailEntity.getName(), filter, null, null, null, idFieldName);
            List<Map<String, Object>> detailList = (List) dataMap.get(detailEntity.getName());

            for (Map<String, Object> map : detailList) {
                map.put(detailEntity.getMainDetailField().getName(), recordId);
                this.saveRecord(detailEntity.getName(), (String) map.get(idFieldName), map);
            }

            List<String> idList = detailList.stream().map((detailx) -> (String) detailx.get(idFieldName)).collect(Collectors.toList());

            for (EntityRecord record : recordList) {
                if (!idList.contains(record.getFieldValue(idFieldName))) {
                    this.delete(record.getFieldValue(idFieldName));
                }
            }
        }
    }

    public int assignRecord(ID toUserId, List<String> recordIds, List<Cascade> cascades) {
        if (recordIds != null && recordIds.size() != 0 && toUserId != null) {
            EntityRecord toUser = this.queryRecordById(toUserId, "departmentId");
            ID toDepartmentId = toUser.getFieldValue("departmentId");
            int successCount = 0;
            int failCount = 0;

            for (String id : recordIds) {
                ID recordId = ID.valueOf(id);
                int status = this.assignRecord(recordId, toUserId, toDepartmentId);
                successCount += status == 1 ? 1 : 0;
                failCount += status == -1 ? 1 : 0;
                List<EntityRecord> entityRecords = this.queryCascadeRecordList(recordId, cascades);

                for (Iterator<EntityRecord> var13 = entityRecords.iterator(); var13.hasNext(); successCount += status == 1 ? 1 : 0) {
                    EntityRecord entityRecord = var13.next();
                    status = this.assignRecord(entityRecord.id(), toUserId, toDepartmentId);
                }
            }

            if (recordIds.size() == failCount) {
                throw new ServiceException("当前登录用户权限不足！");
            } else {
                return successCount;
            }
        } else {
            throw new ServiceException("参数异常！");
        }
    }

    @Transactional(
            propagation = Propagation.NESTED
    )
    public int assignRecord(ID recordId, ID toUserId, ID toDepartment) {
        Entity entity = this.pm.getMetadataManager().getEntity(recordId.getEntityCode());
        if (!entity.containsField("ownerUser")) {
            throw new ServiceException("当前实体没有负责人字段！");
        } else {
            EntityRecord entityRecord = this.queryById(recordId, "ownerUser", "ownerDepartment");
            Map<String, Object> oldValuesMap = entityRecord.getValuesMap();
            ID ownerUser = entityRecord.getFieldValue("ownerUser");
            if (!this.callerContext.checkAssignRight(entityRecord)) {
                return -1;
            } else if (!ownerUser.equals(toUserId)) {
                entityRecord.setFieldValue(entity.getIdField().getName(), recordId);
                entityRecord.setFieldValue("ownerUser", toUserId);
                entityRecord.setFieldValue("ownerDepartment", toDepartment);
                this.pm.update(entityRecord);
                this.notificationService.addAssignNotification(recordId, this.callerContext.getCallerID(), ownerUser, toUserId);
                this.revisionHistoryService.recordHistory(recordId, 5, oldValuesMap, entityRecord.getValuesMap());
                TriggerService triggerService = this.pluginService.getTriggerService();
                if (triggerService != null) {
                    triggerService.executeTrigger(TriggerWhenEnum.ASSIGN, recordId, triggerService.getTriggerLock());
                }

                return 1;
            } else {
                return 0;
            }
        }
    }

    public List<EntityRecord> queryCascadeRecordList(ID recordId, List<Cascade> cascades) {
        List<EntityRecord> recordList = new ArrayList<>();
        ID recordID = ID.valueOf(recordId);
        int entityCode = recordID.getEntityCode();
        Entity entity = this.pm.getMetadataManager().getEntity(entityCode);
        cascades.forEach((cascade) -> {
            EntityRecord entityRecord;
            if (StringUtils.isBlank(cascade.getFieldName())) {
                entityRecord = this.newRecord(entity.getName());
                entityRecord.setFieldValue(entity.getIdField().getName(), recordID);
                recordList.add(entityRecord);
            } else if (!cascade.isReferenced()) {
                entityRecord = this.queryById(recordID, cascade.getFieldName());
                ID cascadeRecordId = entityRecord.getFieldValue(cascade.getFieldName());
                Entity cascadeEntity = this.pm.getMetadataManager().getEntity(cascadeRecordId.getEntityCode());
                EntityRecord cascadeRecord = this.newRecord(cascadeEntity.getName());
                cascadeRecord.setFieldValue(cascadeEntity.getIdField().getName(), cascadeRecordId);
                recordList.add(cascadeRecord);
            } else {
                Entity cascadeEntityx = this.pm.getMetadataManager().getEntity(cascade.getEntityName());
                String filter = String.format("%s = '%s'", cascade.getFieldName(), recordId);
                List<EntityRecord> entityRecords = this.queryListRecord(cascade.getEntityName(), filter, null, null, null, cascadeEntityx.getIdField().getName());
                recordList.addAll(entityRecords);
            }

        });
        return recordList;
    }

    public EntityRecord queryById(ID recordId, String... fieldNames) {
        Entity entity = this.pm.getMetadataManager().getEntity(recordId.getEntityCode());
        RecordQuery recordQuery = this.pm.createRecordQuery();
        String filter = String.format(" [%s] = '%s' ", entity.getIdField().getName(), recordId);
        if (entity.isAuthorizable() && ArrayUtil.isNotEmpty(fieldNames)) {
            fieldNames = MetadataHelper.addOwnerFields(fieldNames);
        }

        EntityRecord entityRecord = recordQuery.queryOne(entity.getName(), filter, null, null, fieldNames);
        if (entityRecord == null) {
            return null;
        } else {
            entityRecord.setFieldValue(entity.getIdField().getName(), recordId);
            if (!this.callerContext.checkQueryRight(entityRecord)) {
                throw new ServiceException("当前登录用户没有权限！");
            } else {
                return entityRecord;
            }
        }
    }

    public Map<String, Object> queryMapById(ID recordId, String... fieldNames) {
        Entity entity = this.pm.getMetadataManager().getEntity(recordId.getEntityCode());
        QuerySchema querySchema = new QuerySchema();
        querySchema.setMainEntity(entity.getName());
        if (fieldNames != null) {
            querySchema.setSelectFields(String.join(",", fieldNames));
            Field idField = this.pm.getMetadataManager().getEntity(querySchema.getMainEntity()).getIdField();
            if (!MetadataHelper.containsIdField(querySchema, idField)) {
                querySchema.setSelectFields(idField.getName() + ", " + querySchema.getSelectFields());
            }
        } else {
            querySchema.setSelectFields(entity.getFieldSet().stream().map(Field::getName).collect(Collectors.joining(",")));
        }
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("recordId",recordId);
        String filter = String.format(" [%s] = pn_recordId ", entity.getIdField().getName());
        querySchema.setFilter(filter);
        DataQuery dataQuery = this.pm.createDataQuery();
        List<Map<String, Object>> query = dataQuery.query(querySchema, new Pagination(1, 1), paramMap);
        if (query != null && !query.isEmpty()) {
            Map<String, Object> resultMap = query.get(0);
            EntityRecord entityRecord = this.newRecord(entity.getName());
            if (entity.isAuthorizable()) {
                entityRecord.setFieldValue("ownerUser", this.callerContext.getCallerID());
                entityRecord.setFieldValue("ownerDepartment", this.callerContext.getDepartmentID());
            }

            entityRecord.setFieldValue(entity.getIdField().getName(), recordId);
            if (!this.callerContext.checkQueryRight(entityRecord)) {
                throw new ServiceException("当前登录用户没有权限！");
            } else {
                Set<Entity> detailEntitySet = entity.getDetailEntitySet();
                if (detailEntitySet != null && detailEntitySet.size() > 0) {
                    detailEntitySet.forEach((detailEntity) -> {
                        QuerySchema detailQuerySchema = new QuerySchema();
                        detailQuerySchema.setMainEntity(detailEntity.getName());
                        detailQuerySchema.setSelectFields(String.join(",", detailEntity.getFieldNames()));
                        detailQuerySchema.setFilter(String.format("%s = pn_recordId", detailEntity.getMainDetailField().getName()));
                        resultMap.put(detailEntity.getName(), dataQuery.query(detailQuerySchema, null,paramMap));
                    });
                }

                resultMap.put("recordApprovalState", this.approvalService.recordApprovalState(recordId));
                return resultMap;
            }
        } else {
            return null;
        }
    }

    @Transactional
    public <T extends BasePojo> List<T> queryListPojo(Class<T> pojoClass, String entityName, String filter, Map<String, Object> paramMap, String sort, Pagination pagination, String... fieldNames) throws IllegalAccessException, InstantiationException {
        RecordQuery recordQuery = this.pm.createRecordQuery();
        List<EntityRecord> recordList = recordQuery.query(entityName, filter, paramMap, sort, pagination, fieldNames);
        List<T> pojoList = new ArrayList<>();

        for (EntityRecord record : recordList) {
            T pojo = pojoClass.newInstance();
            pojo.setEntityRecord(record);
            pojoList.add(pojo);
        }

        return pojoList;
    }

    public List<Map<String, Object>> queryListMap(QuerySchema querySchema, Pagination pagination) {
        Field idField = this.pm.getMetadataManager().getEntity(querySchema.getMainEntity()).getIdField();
        if (!MetadataHelper.containsIdField(querySchema, idField)) {
            querySchema.setSelectFields(idField.getName() + ", " + querySchema.getSelectFields());
        }

        String existingFilter = querySchema.getFilter();
        String readRightFilter = this.rightManager.getReadRightFilter(idField.getEntityCode());
        String shareRightFilter = this.shareAccessService.getShareReadRightFilter(idField.getEntityCode(), idField);
        String rightFilter = FilterHelper.joinFilters("OR", readRightFilter, shareRightFilter);
        String joinFilter = FilterHelper.joinFilters("AND", rightFilter, existingFilter);
        querySchema.setFilter(joinFilter);
        DataQuery dataQuery = this.pm.createDataQuery();
        return dataQuery.query(querySchema, pagination);
    }

    public ListQueryResult getDataLiveViewAndData(String entityName) throws InstantiationException, IllegalAccessException {
        Entity dataEntity = this.pm.getMetadataManager().getEntity(entityName);
        int entityCode = dataEntity.getEntityCode();
        String filter = String.format("([entityCode] = %d) and ([viewName] = '默认视图')", entityCode);
        List<DataListView> pojoList = this.queryListPojo(DataListView.class, "DataListView", filter, null, null, null);
        List<TableHeaderColumn> columnList = new ArrayList<>();
        Pagination pagination = null;
        StringBuilder selectFieldsSB = new StringBuilder();
        boolean hasSelectFieldsFlag = false;
        if (pojoList.size() > 0) {
            String headerJson = pojoList.get(0).getHeaderJson();
            if (StringUtils.isNotBlank(headerJson)) {
                columnList = JsonHelper.readJsonValue(headerJson, new TypeReference<List<TableHeaderColumn>>() {
                });
                if (columnList != null && (columnList).size() > 0) {
                    hasSelectFieldsFlag = true;
                }
            }

            String paginationJson = pojoList.get(0).getPaginationJson();
            if (StringUtils.isNotBlank(paginationJson)) {
                pagination = JsonHelper.readJsonValue(paginationJson, new TypeReference<Pagination>() {
                });
            }
        }

        if (pagination == null) {
            pagination = new Pagination(1, 20);
        }

        boolean firstSelectFieldFlag = true;
        if (!hasSelectFieldsFlag) {
            Collection<Field> fieldCollection = dataEntity.getFieldSet();
            columnList = new ArrayList<>();

            for (Field field : fieldCollection) {
                if (field.isDefaultMemberOfListFlag()) {
                    if (firstSelectFieldFlag) {
                        selectFieldsSB.append(field.getName());
                        firstSelectFieldFlag = false;
                    } else {
                        selectFieldsSB.append(", ").append(field.getName());
                    }

                    TableHeaderColumn thColumn = new TableHeaderColumn();
                    thColumn.setProp(field.getName());
                    thColumn.setLabel(field.getLabel());
                    thColumn.setType(field.getType().getName());
                    TableHeaderMapping.setHeaderColumnDefaultProps(field.getType().getName(), thColumn);
                    columnList.add(thColumn);
                }
            }
        } else {

            for (TableHeaderColumn thc : columnList) {
                if (firstSelectFieldFlag) {
                    selectFieldsSB.append(thc.getProp());
                    firstSelectFieldFlag = false;
                } else {
                    selectFieldsSB.append(", ").append(thc.getProp());
                }
            }
        }

        QuerySchema querySchema = new QuerySchema();
        querySchema.setMainEntity(entityName);
        querySchema.setSelectFields(selectFieldsSB.toString());
        querySchema.setFilter(null);
        Field idField = this.pm.getMetadataManager().getEntity(querySchema.getMainEntity()).getIdField();
        if (!MetadataHelper.containsIdField(querySchema, idField)) {
            querySchema.setSelectFields(idField.getName() + ", " + querySchema.getSelectFields());
        }

        String nameFieldName = dataEntity.getNameField() == null ? null : dataEntity.getNameField().getName();
        EntityBasicInfo entityBasicInfo = new EntityBasicInfo(dataEntity.getName(), dataEntity.getLabel(), dataEntity.getIdField().getName(), nameFieldName);
        List<Map<String, Object>> dataList = this.pm.createDataQuery().query(querySchema, pagination);
        return new ListQueryResult(dataList, pagination, columnList, entityBasicInfo);
    }

    public ReferenceQueryResult queryReferenceFieldRecord(String entityName, String refFieldName, Integer pageNo, Integer pageSize, String queryText, String extraFilter) {
        Entity entity = this.pm.getMetadataManager().getEntity(entityName);
        Field refField = entity.getField(refFieldName);
        List<ReferenceModel> referenceSetting = refField.getReferenceSetting();
        if (referenceSetting != null && !referenceSetting.isEmpty()) {
            ReferenceModel referenceModel = referenceSetting.get(0);
            String refEntityName = referenceModel.getEntityName();
            Entity refEntity = this.pm.getMetadataManager().getEntity(refEntityName);
            List<Map<String, String>> columnList = new ArrayList<>();

            Map<String, String> nameFieldNameMap;
            for (String fldName : referenceModel.getFieldList()) {
                Field columnFld = refEntity.getField(fldName);
                nameFieldNameMap = buildColumnMap(columnFld);
                columnList.add(nameFieldNameMap);
            }

            QuerySchema querySchema = new QuerySchema();
            querySchema.setMainEntity(refEntityName);
            StringBuilder fieldsSb = new StringBuilder();
            fieldsSb.append(refEntity.getIdField().getName()).append(",");
            referenceModel.getFieldList().forEach((fName) -> fieldsSb.append(fName).append(","));
            String idFieldName = refEntity.getIdField().getName();
            String nameFieldName;
            if (refEntity.getNameField() != null) {
                nameFieldName = refEntity.getNameField().getName();
                if (!referenceModel.getFieldList().contains(nameFieldName)) {
                    fieldsSb.append(nameFieldName).append(",");
                }
            } else {
                nameFieldName = idFieldName;
            }

            String extraCondition = StringUtils.isBlank(extraFilter) ? "" : " AND (" + extraFilter + ") ";
            querySchema.setSelectFields(fieldsSb.toString());
            if (StringUtils.isBlank(queryText)) {
                querySchema.setFilter("(1=1) " + extraCondition);
            } else {
                querySchema.setFilter(String.format(" (%s like '%%%s%%') %s ", nameFieldName, queryText, extraCondition));
            }

            Pagination pagination = new Pagination(pageNo, pageSize);
            List<Map<String, Object>> dataList = this.queryListMap(querySchema, pagination);
            return new ReferenceQueryResult(columnList, dataList, pagination, idFieldName, nameFieldName);
        } else {
            return this.createDefaultReferenceQueryResult(refField, pageNo, pageSize);
        }
    }

    private ReferenceQueryResult createDefaultReferenceQueryResult(Field refField, Integer pageNo, Integer pageSize) {
        List<Map<String, String>> columnList = new ArrayList<>();
        Entity refEntity = refField.getReferTo().iterator().next();
        Map<String, String> columnMap;
        if (refEntity.getNameField() != null) {
            columnMap = buildColumnMap(refEntity.getNameField());
            columnList.add(columnMap);
        } else {
            columnMap = buildColumnMap(refEntity.getIdField());
            columnList.add(columnMap);
        }

        QuerySchema querySchema = new QuerySchema();
        querySchema.setMainEntity(refEntity.getName());
        StringBuilder fieldsSb = new StringBuilder();
        fieldsSb.append(refEntity.getIdField().getName()).append(",");
        String idFieldName = refEntity.getIdField().getName();
        String nameFieldName;
        if (refEntity.getNameField() != null) {
            nameFieldName = refEntity.getNameField().getName();
            fieldsSb.append(nameFieldName).append(",");
        } else {
            nameFieldName = idFieldName;
        }

        querySchema.setSelectFields(fieldsSb.toString());
        querySchema.setFilter("(1=1)");
        Pagination pagination = new Pagination(pageNo, pageSize);
        List<Map<String, Object>> dataList = this.queryListMap(querySchema, pagination);
        return new ReferenceQueryResult(columnList, dataList, pagination, idFieldName, nameFieldName);
    }

    public FormLayout getFormLayout(String entityName) {
        Entity flEntity = this.pm.getMetadataManager().getEntity(entityName);
        int entityCode = flEntity.getEntityCode();
        String filter = String.format("([entityCode] = %d) and ([layoutName] = '默认表单布局')", entityCode);
        List pojoList;

        try {
            pojoList = this.queryListPojo(FormLayout.class, "FormLayout", filter, null, null, null);
        } catch (Exception var7) {
            var7.printStackTrace();
            throw new ServiceException("数据异常");
        }

        return !pojoList.isEmpty() ? (FormLayout) pojoList.get(0) : null;
    }

    public FormQueryResult queryFormDataForCreate(String entityName) {
        FormLayout formLayout = this.getFormLayout(entityName);
        if (formLayout == null) {
            throw new IllegalArgumentException(entityName + ": 尚未设计表单");
        } else {
            String layoutJson = formLayout.getLayoutJson();
            EntityRecord entityRecord = this.pm.newRecord(entityName);
            Map<String, FieldProps> fieldPropsMap = new HashMap<>();
            Entity entity = this.pm.getMetadataManager().getEntity(entityName);
            this.setCreatorInfo(entity, entityRecord);
            Map<String, String> labelsMap = entityRecord.getLabelsMap();
            List<String> deletedFields = new ArrayList<>();
            return new FormQueryResult(layoutJson, fieldPropsMap, entityRecord, labelsMap, deletedFields);
        }
    }

    private ID insertRecycleBin(ID recordId, String entityName, List<String> deletedIdList, String deletedWith) {
        EntityRecord recycleBin = this.newRecord("RecycleBin");
        recycleBin.setFieldValue("entityCode", recordId.getEntityCode());
        recycleBin.setFieldValue("entityId", recordId.getId());
        recycleBin.setFieldValue("entityName", entityName);
        recycleBin.setFieldValue("deletedBy", ID.valueOf(this.callerContext.getCallerId()));
        recycleBin.setFieldValue("deletedOn", new Date());
        recycleBin.setFieldValue("detailEntityIds", String.join(",", deletedIdList));
        recycleBin.setFieldValue("deletedWith", deletedWith);
        return this.pm.insert(recycleBin);
    }

    private void setCreatorInfo(Entity entity, EntityRecord record) {
        ID callerId = ID.valueOf(this.callerContext.getCallerId());
        if (entity.containsField("createdBy")) {
            record.setFieldValue("createdBy", callerId);
            record.setFieldLabel("createdBy", this.userService.getUserName(callerId));
        }

        if (entity.containsField("ownerUser")) {
            record.setFieldValue("ownerUser", callerId);
            record.setFieldLabel("ownerUser", this.userService.getUserName(callerId));
        }

        ID departmentId = this.userService.getDepartmentIdOfUser(callerId);
        if (entity.containsField("ownerDepartment")) {
            record.setFieldValue("ownerDepartment", departmentId);
            record.setFieldLabel("ownerDepartment", this.userService.getDepartmentNameOfUser(callerId));
        }

    }

    public FormQueryResult queryFormDataForUpdate(String entityName, String recordId) {
        FormLayout formLayout = this.getFormLayout(entityName);
        if (formLayout == null) {
            throw new IllegalArgumentException(entityName + ": 尚未设计表单");
        } else {
            String layoutJson = formLayout.getLayoutJson();
            Map<String, FieldProps> fieldPropsMap = new HashMap<>();
            EntityRecord entityRecord = this.pm.newRecord(entityName);
            Map<String, Object> recordDataMap = this.queryMapById(ID.valueOf(recordId), (String[]) null);
            entityRecord.setValuesMap(recordDataMap);
            Map<String, String> labelsMap = entityRecord.getLabelsMap();
            List<String> deletedFields = new ArrayList<>();
            return new FormQueryResult(layoutJson, fieldPropsMap, entityRecord, labelsMap, deletedFields);
        }
    }

    public List<Map<String, Object>> queryEntityCodeListByEntity(String entityName, String fieldName) {
        QuerySchema querySchema = new QuerySchema();
        querySchema.setMainEntity(entityName);
        querySchema.setSelectFields(fieldName);
        querySchema.setGroupBy(fieldName);
        List<Map<String, Object>> query = this.pm.createDataQuery().query(querySchema, null);

        for (Map<String, Object> map : query) {
            int entityCode = (Integer) map.get(fieldName);
            map.put("entityCode", entityCode);
            map.put("entityName", this.pm.getMetadataManager().getEntity(entityCode).getName());
        }

        return query;
    }

    public ID queryReferenceIdByName(Entity refEntity, String nameKey) {
        QuerySchema querySchema = new QuerySchema();
        querySchema.setMainEntity(refEntity.getName());
        querySchema.setSelectFields(refEntity.getIdField().getName());
        String filter = String.format("[%s] = '%s' ", refEntity.getNameField().getName(), nameKey);
        querySchema.setFilter(filter);
        List<Map<String, Object>> dataList = this.queryListMap(querySchema, new Pagination(1, 1));
        return CollectionUtil.isNotEmpty(dataList) ? (ID) dataList.get(0).get(refEntity.getIdField().getName()) : null;
    }
}
