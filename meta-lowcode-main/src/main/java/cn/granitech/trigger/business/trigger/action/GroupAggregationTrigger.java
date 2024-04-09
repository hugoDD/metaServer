package cn.granitech.trigger.business.trigger.action;

import cn.granitech.business.query.QueryHelper;
import cn.granitech.business.query.SelectStatement;
import cn.granitech.business.service.CrudService;
import cn.granitech.trigger.business.trigger.BaseTrigger;
import cn.granitech.util.FilterHelper;
import cn.granitech.util.JsonHelper;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.metadata.fieldtype.FieldTypes;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.variantorm.pojo.Field;
import cn.granitech.web.pojo.FormQueryResult;
import cn.hutool.core.collection.CollUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class GroupAggregationTrigger
        implements BaseTrigger {
    @Resource
    PersistenceManager pm;
    @Resource
    CrudService crudService;
    @Resource
    AggregationTrigger aggregationTrigger;

    public boolean trigger(ID entityId, String actionContent) {
        GroupAggregation groupAggregation = JsonHelper.readJsonValue(actionContent, GroupAggregation.class);
        Entity mainEntity = this.pm.getMetadataManager().getEntity(entityId.getEntityCode());
        assert groupAggregation != null;
        Entity targetEntity = this.pm.getMetadataManager().getEntity(groupAggregation.getEntityName());

        String[] sourceFields = groupAggregation.getGroupItem().stream().map(GroupItem::getSourceField).toArray(String[]::new);

        StringBuffer easySql = this.aggregationTrigger.getAggregationSql(groupAggregation.getItems(), groupAggregation.getFilter(), mainEntity, null, sourceFields);


        SelectStatement selectStatement = (new QueryHelper()).compileEasySql(this.pm.getMetadataManager(), easySql.toString());
        List<Map<String, Object>> dataList = this.crudService.queryListBySql(selectStatement.toString());
        if (CollUtil.isEmpty(dataList)) {
            return true;
        }


        List<String> updateIdList = new ArrayList<>();

        for (Map<String, Object> map : dataList) {
            StringBuffer filter = new StringBuffer(FilterHelper.toFilter(groupAggregation.getTargetFilter()));
            if (StringUtils.isBlank(filter)) {
                filter.append(" true ");
            }
            for (GroupItem item : groupAggregation.getGroupItem()) {
                if (!targetEntity.containsField(item.getTargetField()) || !mainEntity.containsField(item.getSourceField())) {
                    continue;
                }
                if (map.get(item.getSourceField()) == null) {
                    filter.append(" AND ").append(item.getTargetField()).append(" IS NULL ");
                    continue;
                }
                Field field = mainEntity.getField(item.getSourceField());
                filter.append(" AND ").append(item.getTargetField()).append(" = :").append((field.getType() == FieldTypes.REFERENCE) ? field.getPhysicalName() : field.getName());
            }
            List<EntityRecord> entityRecords = this.crudService.queryListRecord(groupAggregation.getEntityName(), filter.toString(), map, null, null, targetEntity.getIdField().getName());
            for (EntityRecord entityRecord : entityRecords) {
                for (AggregationItem aggregationItem : groupAggregation.getItems()) {
                    if (!targetEntity.containsField(aggregationItem.getTargetField())) {
                        continue;
                    }
                    Field field = targetEntity.getField(aggregationItem.getTargetField());
                    entityRecord.setFieldValue(aggregationItem.getTargetField(), field.getType().fromJson(map.get(aggregationItem.getAsName())));
                }
                this.crudService.update(entityRecord);
                updateIdList.add(entityRecord.getFieldValue(entityRecord.getEntity().getIdField().getName()).toString());
            }
            if (entityRecords.size() == 0 && groupAggregation.isAutoCreate()) {
                Map<String, Object> dataMap = new HashMap<>();
                for (GroupItem groupItem : groupAggregation.getGroupItem()) {
                    if (!targetEntity.containsField(groupItem.getTargetField()) || !mainEntity.containsField(groupItem.getSourceField())) {
                        continue;
                    }
                    Field field = mainEntity.getField(groupItem.getSourceField());
                    System.out.println(groupItem.getTargetField());
                    dataMap.put(groupItem.getTargetField(), map.get((field.getType() == FieldTypes.REFERENCE) ? field.getPhysicalName() : field.getName()));
                }
                for (AggregationItem aggregationItem : groupAggregation.getItems()) {
                    dataMap.put(aggregationItem.getTargetField(), map.get(aggregationItem.getAsName()));
                }
                FormQueryResult formQueryResult = this.crudService.saveRecord(groupAggregation.getEntityName(), null, dataMap);
                updateIdList.add(formQueryResult.getFormData().getFieldValue(formQueryResult.getFormData().getEntity().getIdField().getName()).toString());
            }
        }

        String otherFilter = String.format(" %s NOT IN ('%s')", targetEntity.getIdField().getName(), String.join("','", updateIdList));
        List<EntityRecord> otherRecords = this.crudService.queryListRecord(groupAggregation.getEntityName(), otherFilter, null, null, null, targetEntity.getIdField().getName());
        for (EntityRecord entityRecord : otherRecords) {
            for (AggregationItem item : groupAggregation.getItems()) {
                if (!targetEntity.containsField(item.getTargetField())) {
                    continue;
                }
                entityRecord.setFieldValue(item.getTargetField(), targetEntity.getField(item.getTargetField()).getType().fromJson(0));
            }
            this.crudService.update(entityRecord);
        }


        if (StringUtils.isNotBlank(groupAggregation.getCallbackField()) && mainEntity.containsField(groupAggregation.getCallbackField())) {
            EntityRecord mainRecord = this.crudService.queryById(entityId, sourceFields);
            if (mainRecord == null) {
                return true;
            }
            StringBuffer filter = new StringBuffer(FilterHelper.toFilter(groupAggregation.getTargetFilter()));
            if (StringUtils.isBlank(filter)) {
                filter.append(" true ");
            }
            for (GroupItem item : groupAggregation.getGroupItem()) {
                filter.append(" AND ").append(item.getTargetField()).append(" = :").append(item.getSourceField());
            }
            Entity entity = this.pm.getMetadataManager().getEntity(groupAggregation.getEntityName());
            List<EntityRecord> entityRecords = this.crudService.queryListRecord(groupAggregation.getEntityName(), filter.toString(), mainRecord.getValuesMap(), null, null, entity.getIdField().getName());


            if (CollUtil.isNotEmpty(entityRecords) && entityRecords.size() > 0) {
                EntityRecord entityRecord = this.crudService.newRecord(mainEntity.getName());
                entityRecord.setFieldValue(entityRecord.getEntity().getIdField().getName(), entityId);
                entityRecord.setFieldValue(groupAggregation.getCallbackField(), entityRecords.get(0).getFieldValue(entity.getIdField().getName()));
                this.crudService.updateRecord(entityRecord);
            }
        }

        return true;
    }
}



