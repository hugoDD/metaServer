package cn.granitech.trigger.business.trigger.action;

import java.util.List;


public class DataOperate {
    public static final String TO_FIXED = "toFixed";
    public static final String FOR_COMPILE = "forCompile";
    public static final String TO_NULL = "toNull";
    public static final String FOR_FIELD = "forField";
    private String entityName;
    private String fieldName;
    private boolean isReferenced;
    private List<DataOperateItem> items;

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

    public boolean isReferenced() {
        return this.isReferenced;
    }

    public void setIsReferenced(boolean referenced) {
        this.isReferenced = referenced;
    }

    public List<DataOperateItem> getItems() {
        return this.items;
    }

    public void setItems(List<DataOperateItem> items) {
        this.items = items;
    }
}



