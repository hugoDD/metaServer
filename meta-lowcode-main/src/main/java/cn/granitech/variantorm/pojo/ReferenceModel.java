package cn.granitech.variantorm.pojo;

import java.util.List;

public class ReferenceModel {
    private String entityName;

    private List<String> fieldList;

    public String getEntityName() {
        return this.entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public List<String> getFieldList() {
        return this.fieldList;
    }

    public void setFieldList(List<String> fieldList) {
        this.fieldList = fieldList;
    }
}
