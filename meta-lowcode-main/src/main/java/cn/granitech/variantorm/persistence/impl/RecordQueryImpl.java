package cn.granitech.variantorm.persistence.impl;


import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.persistence.RecordQuery;
import cn.granitech.variantorm.persistence.cache.QueryCache;
import cn.granitech.variantorm.pojo.Field;
import cn.granitech.variantorm.pojo.IDName;
import cn.granitech.variantorm.pojo.Pagination;
import cn.granitech.variantorm.pojo.QuerySchema;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RecordQueryImpl implements RecordQuery {
    private PersistenceManager persistenceManager;

    private  String idNamesJoining(List<IDName> idNameList) {

        return idNameList.stream().map(IDName::getName).collect(Collectors.joining(","));
    }

    private  QuerySchema newQuerySchema(String entityName, String filter, String sort, List<String> queryFields, String ... fieldNames) {
        QuerySchema querySchema = new QuerySchema();
        List<String> nameList;
        if (fieldNames != null && fieldNames.length > 0) {
            nameList = Stream.of(fieldNames).filter(StringUtils::isNotEmpty).collect(Collectors.toList());
        } else {
            nameList = this.persistenceManager.getMetadataManager().getEntity(entityName).getFieldSet()
                      .stream().map(Field::getName).filter(StringUtils::isNotEmpty).collect(Collectors.toList());

        }
        queryFields.addAll(nameList);
        String selectFields = String.join(",",nameList);

        querySchema.setSelectFields(selectFields);
        querySchema.setMainEntity(entityName);
        querySchema.setFilter(filter);
        querySchema.setSort(sort);
        return querySchema;
    }


    @Override
    public List<EntityRecord> query(String entityName, String filter, Map<String, Object> paramMap, String sort, Pagination pagination, String ... fieldNames) {
        List<String> queryFields = new ArrayList<>();
        QuerySchema querySchema = this.newQuerySchema(entityName, filter, sort, queryFields, fieldNames);
        List<Map<String, Object>> recordList = this.persistenceManager.createDataQuery().query(querySchema, pagination, paramMap);
        List<EntityRecord> entityRecords = new ArrayList<>();
        for (Map<String, Object> record : recordList) {
            EntityRecord entityRecord = this.persistenceManager.newRecord(entityName);
            for (String fieldName : queryFields) {

                this.persistenceManager.getMetadataManager()
                        .getEntity(entityName)
                        .getField(fieldName)
                        .getType()
                        .formatFieldValueOfRecord(entityRecord, fieldName, record.get(fieldName));
            }
            entityRecord.setNotModified();
            entityRecords.add(entityRecord);
        }
        return entityRecords;
    }

    @Override
    public EntityRecord queryOne(String entityName, String filter, Map<String, Object> paramMap, String sort, String ... fieldNames) {
        Pagination a = new Pagination(1, 10);
        List<EntityRecord> entityRecords = this.query(entityName, filter, paramMap, sort, a, fieldNames);
        if (entityRecords == null || entityRecords.size() <= 0) {
            return null;
        }
        return entityRecords.get(0);
    }

    public RecordQueryImpl(PersistenceManager persistenceManager, QueryCache queryCache) {
        this.persistenceManager = persistenceManager;
    }

    private  ID[] IdNamesToArray(List<IDName> idNameList) {

        return idNameList.stream().map(IDName::getId).toArray(ID[]::new);
    }
}
