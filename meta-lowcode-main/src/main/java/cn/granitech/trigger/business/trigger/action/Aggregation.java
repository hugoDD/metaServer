package cn.granitech.trigger.business.trigger.action;

import java.util.List;


public class Aggregation {
    public static final String FOR_COMPILE = "forCompile";
    private String entityName;
    private String fieldName;
    private String filter;
    private List<AggregationItem> items;

    public String getFilter() {
        return this.filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getEntityName() {
        return this.entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public List<AggregationItem> getItems() {
        return this.items;
    }

    public void setItems(List<AggregationItem> items) {
        this.items = items;
    }
}



