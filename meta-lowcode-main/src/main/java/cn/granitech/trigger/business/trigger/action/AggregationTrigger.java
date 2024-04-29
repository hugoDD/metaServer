package cn.granitech.trigger.business.trigger.action;

import cn.granitech.business.query.QueryHelper;
import cn.granitech.business.query.SelectStatement;
import cn.granitech.business.service.CrudService;
import cn.granitech.trigger.business.trigger.BaseTrigger;
import cn.granitech.util.FilterHelper;
import cn.granitech.util.JsonHelper;
import cn.granitech.util.RegexHelper;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.web.enumration.CalcModeEnum;
import cn.granitech.web.pojo.filter.Filter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;


@Component
public class AggregationTrigger implements BaseTrigger {
    @Resource
    PersistenceManager pm;
    @Resource
    CrudService crudService;

    public boolean trigger(ID entityId, String actionContent) {
        Aggregation aggregation = JsonHelper.readJsonValue(actionContent, Aggregation.class);
        Entity mainEntity = this.pm.getMetadataManager().getEntity(entityId.getEntityCode());
        assert aggregation != null;
        EntityRecord entityRecord = this.crudService.queryById(entityId, aggregation.getFieldName());
        ID referenceId = entityRecord.getFieldValue(aggregation.getFieldName());

        Filter filter = null;
        if (StringUtils.isNotBlank(aggregation.getFilter())) {
            filter = JsonHelper.readJsonValue(aggregation.getFilter(), Filter.class);
        }

        StringBuffer easySql = getAggregationSql(aggregation.getItems(), filter, mainEntity,
                String.format(" %s = '%s' ", aggregation.getFieldName(), referenceId), aggregation.getFieldName());

        SelectStatement selectStatement = (new QueryHelper()).compileEasySql(this.pm.getMetadataManager(), easySql.toString());
        Map<String, Object> map = this.crudService.queryMapBySql(selectStatement.toString());
        Map<String, Object> dataMap = new HashMap<>();
        aggregation.getItems().forEach(item -> dataMap.put(item.getTargetField(),
                (map == null || map.get(item.getAsName()) == null) ? Integer.valueOf(0) : map.get(item.getAsName())));

        this.crudService.saveRecord(aggregation.getEntityName(), referenceId.getId(), dataMap);

        return true;
    }


    public StringBuffer getAggregationSql(List<AggregationItem> aggregationItem, Filter filter, Entity mainEntity, String where, String... groupBy) {
        StringBuffer easySql = new StringBuffer("SELECT ");

        int nameIndex = 0;
        for (String field : groupBy) {
            easySql.append(field).append(" AS ").append(field).append(",");
        }
        for (AggregationItem item : aggregationItem) {
            item.setAsName("pm_" + nameIndex++);

            if (item.getCalcMode().equals("forCompile")) {
                String sourceField = item.getSourceField();
                Set<String> variables = RegexHelper.getVariableSet(sourceField, "\\{([a-zA-Z0-9$\\.]+)}");
                for (String variable : variables) {
                    String[] array = variable.split("\\$");
                    String parameterSql = getParameterSql(array[0], array[1]);
                    sourceField = sourceField.replace(String.format("{%s}", variable), parameterSql);
                }
                easySql.append(sourceField);
            } else {

                String parameterSql = getParameterSql(item.getSourceField(), item.getCalcMode());
                easySql.append(parameterSql);
            }
            easySql.append(" AS '").append(item.getAsName()).append("',");
        }


        easySql.delete(easySql.length() - 1, easySql.length());
        easySql.append(" FROM ").append(mainEntity.getName()).append(" WHERE ")
                .append(StringUtils.isNotBlank(where) ? where : Boolean.valueOf(true)).append(" ");


        String filterSql = FilterHelper.toFilter(filter);
        if (StringUtils.isNotBlank(filterSql)) {
            easySql.append(" AND (").append(filterSql).append(") ");
        }
        easySql.append("GROUP BY ").append(String.join(",", groupBy));
        return easySql;
    }

    private String getParameterSql(String name, String calcMode) {
        return Objects.requireNonNull(CalcModeEnum.getCalcModeEnum(calcMode)).getCalcSql(name);
    }
}



