package cn.granitech.trigger.business.trigger.action;

import cn.granitech.web.pojo.filter.Filter;

import java.util.List;


public class GroupAggregation {
    public static final String FOR_COMPILE = "forCompile";
    private String entityName;
    private Filter filter;
    private List<AggregationItem> items;
    private boolean autoCreate;
    private List<GroupItem> groupItem;
    private Filter targetFilter;
    private String callbackField;

    public String getEntityName() {
        return this.entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Filter getFilter() {
        return this.filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public List<AggregationItem> getItems() {
        return this.items;
    }

    public void setItems(List<AggregationItem> items) {
        this.items = items;
    }

    public boolean isAutoCreate() {
        return this.autoCreate;
    }

    public void setAutoCreate(boolean autoCreate) {
        this.autoCreate = autoCreate;
    }

    public List<GroupItem> getGroupItem() {
        return this.groupItem;
    }

    public void setGroupItem(List<GroupItem> groupItem) {
        this.groupItem = groupItem;
    }

    public Filter getTargetFilter() {
        return this.targetFilter;
    }

    public void setTargetFilter(Filter targetFilter) {
        this.targetFilter = targetFilter;
    }

    public String getCallbackField() {
        return this.callbackField;
    }

    public void setCallbackField(String callbackField) {
        this.callbackField = callbackField;
    }
}



