package cn.granitech.web.pojo.approval.vo;

import java.io.Serializable;

public class ApprovalConfigEntityVO implements Serializable {
    private Integer entityCode;
    private String entityName;
    private String label;

    public Integer getEntityCode() {
        return this.entityCode;
    }

    public void setEntityCode(Integer entityCode2) {
        this.entityCode = entityCode2;
    }

    public String getEntityName() {
        return this.entityName;
    }

    public void setEntityName(String entityName2) {
        this.entityName = entityName2;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label2) {
        this.label = label2;
    }
}
