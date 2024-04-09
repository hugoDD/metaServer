package cn.granitech.business.service;

import cn.granitech.interceptor.CallerContext;
import cn.granitech.util.EntityHelper;
import cn.granitech.util.JsonHelper;
import cn.granitech.util.MetadataHelper;
import cn.granitech.util.ObjectHelper;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.persistence.RecordQuery;
import cn.granitech.variantorm.pojo.*;
import cn.granitech.web.pojo.ListQueryRequestBody;
import cn.granitech.web.pojo.ListQueryResult;
import cn.hutool.core.util.StrUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class BaseService {
    @Resource
    protected CallerContext callerContext;
    @Resource
    protected PersistenceManager pm;

    @Transactional
    public List<String> batchDeleteRecord(String entityName, String filter) {
        RecordQuery recordQuery = this.pm.createRecordQuery();
        Entity entity = this.pm.getMetadataManager().getEntity(entityName);
        List<EntityRecord> deleteList = recordQuery.query(entityName, filter, null, null, null, entity.getIdField().getName());
        this.pm.batchDelete(entityName, filter);
        return deleteList.stream().map(EntityRecord::id).map(ID::getId).collect(Collectors.toList());
    }

    public EntityRecord newRecord(String entityName) {
        return this.pm.newRecord(entityName);
    }

    public EntityRecord newRecord(int entityCode) {
        return newRecord(this.pm.getMetadataManager().getEntity(entityCode).getName());
    }

    public IDName getIDName(String referenceId) {
        if (StrUtil.isBlank(referenceId)) {
            return null;
        }
        return this.pm.getQueryCache().getIDName(referenceId);
    }

    public List<IDName> getIDNameByArrays(String referenceIds) {
        List<IDName> idNameList = new ArrayList<>();
        if (!StringUtils.isBlank(referenceIds)) {
            for (String referenceId : referenceIds.split(",")) {
                idNameList.add(getIDName(referenceId));
            }
        }
        return idNameList;
    }

    public EntityRecord queryOneRecord(String entityName, String filter, Map<String, Object> paramMap, String sort, String... fieldNames) {
        return this.pm.createRecordQuery().queryOne(entityName, filter, paramMap, sort, fieldNames);
    }

    public <T> T queryOneCustomPojo(Class<T> pojoClass, String entityName, String filter, Map<String, Object> paramMap, String sort, String... fieldNames) {
        List<T> resultList = queryListCustomPojo(pojoClass, entityName, filter, paramMap, sort, null, fieldNames);
        if (resultList.size() == 0) {
            return null;
        }
        return resultList.get(0);
    }

    @Transactional
    public <T> List<T> queryListCustomPojo(Class<T> pojoClass, String entityName, String filter, Map<String, Object> paramMap, String sort, Pagination pagination, String... fieldNames) {
        List<EntityRecord> recordList = this.pm.createRecordQuery().query(entityName, filter, paramMap, sort, pagination, fieldNames);
        List<T> pojoList = new ArrayList<>();
        for (EntityRecord record : recordList) {
            T pojo = null;
            try {
                pojo = ObjectHelper.entityToObj(record, pojoClass.newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
            pojoList.add(pojo);
        }
        return pojoList;
    }

    @Transactional
    public List<EntityRecord> queryListRecord(String entityName, String filter, Map<String, Object> paramMap, String sort, Pagination pagination, String... fieldNames) {
        return this.pm.createRecordQuery().query(entityName, filter, paramMap, sort, pagination, fieldNames);
    }

    @Transactional
    public List<Map<String, Object>> queryListMap(QuerySchema querySchema, Pagination pagination) {
        Field idField = this.pm.getMetadataManager().getEntity(querySchema.getMainEntity()).getIdField();
        if (!MetadataHelper.containsIdField(querySchema, idField)) {
            querySchema.setSelectFields(idField.getName() + ", " + querySchema.getSelectFields());
        }
        return this.pm.createDataQuery().query(querySchema, pagination);
    }

    @Transactional
    public ListQueryResult queryListMap(ListQueryRequestBody requestBody) {
        QuerySchema querySchema = requestBody.querySchema();
        Pagination pagination = requestBody.pagination();
        List<Map<String, Object>> resultList = queryListMap(querySchema, pagination);
        ListQueryResult queryResult = new ListQueryResult();
        queryResult.setDataList(resultList);
        queryResult.setPagination(pagination);
        return queryResult;
    }

    public EntityRecord queryRecordById(ID recordId, String... fieldNames) {
        Entity entity = this.pm.getMetadataManager().getEntity(recordId.getEntityCode());
        return this.pm.createRecordQuery().queryOne(entity.getName(), String.format(" [%s] = '%s' ", entity.getIdField().getName(), recordId), null, null, fieldNames);
    }

    public Map<String, Object> queryMapBySql(String sql) {
        try {
            return this.pm.getJdbcTemplate().queryForMap(sql);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Map<String, Object>> queryListBySql(String sql) {
        try {
            return this.pm.getJdbcTemplate().queryForList(sql);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public <T> T queryOneCustomPojoById(Class<T> pojoClass, ID entityId) {
        try {
            return ObjectHelper.entityToObj(queryRecordById(entityId), pojoClass.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ID saveOrUpdateRecord(String entityName, ID recordId, Object data) {
        EntityRecord entityRecord;
        if (recordId != null) {
            entityRecord = queryRecordById(recordId);
        } else {
            entityRecord = newRecord(entityName);
        }
        EntityHelper.formatFieldValue(entityRecord, JsonHelper.writeObjectAsMap(data));
        return saveOrUpdateRecord(recordId, entityRecord);
    }

    @Transactional
    public ID saveOrUpdateRecord(ID recordId, EntityRecord entityRecord) {
        Entity entity = entityRecord.getEntity();
        if (recordId == null) {
            return createRecord(entityRecord);
        }
        entityRecord.setFieldValue(entity.getIdField().getName(), recordId);
        updateRecord(entityRecord);
        return recordId;
    }

    @Transactional
    public ID createRecord(EntityRecord record) {
        Entity entity = record.getEntity();
        ID callerId = this.callerContext.getCallerID();
        if (entity.containsField("createdOn")) {
            record.setFieldValue("createdOn", new Date());
        }
        if (entity.containsField("createdBy")) {
            record.setFieldValue("createdBy", callerId);
        }
        if (entity.containsField("modifiedOn")) {
            record.setFieldValue("modifiedOn", new Date());
        }
        if (entity.containsField("modifiedBy")) {
            record.setFieldValue("modifiedBy", callerId);
        }
        if (entity.containsField("ownerUser")) {
            record.setFieldValue("ownerUser", callerId);
        }
        if (entity.containsField("ownerDepartment")) {
            record.setFieldValue("ownerDepartment", this.callerContext.getDepartmentID());
        }
        if (entity.containsField("approvalStatus") && entity.getField("approvalStatus") == null) {
            record.setFieldValue("approvalStatus", 0);
        }
        return this.pm.insert(record);
    }

    @Transactional
    public Boolean updateRecord(EntityRecord record) {
        Entity entity = record.getEntity();
        if (entity.containsField("modifiedOn")) {
            record.setFieldValue("modifiedOn", new Date());
        }
        if (entity.containsField("modifiedBy")) {
            record.setFieldValue("modifiedBy", this.callerContext.getCallerID());
        }
        if (!record.isModified()) {
            return false;
        }
        return this.pm.update(record);
    }

    @Transactional
    public Boolean deleteRecord(ID recordId) {
        Entity entity = this.pm.getMetadataManager().getEntity(recordId.getEntityCode());
        return this.pm.delete(recordId, entity.getName());
    }
}
