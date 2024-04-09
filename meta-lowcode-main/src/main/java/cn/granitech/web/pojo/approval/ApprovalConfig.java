package cn.granitech.web.pojo.approval;

import cn.granitech.variantorm.metadata.ID;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class ApprovalConfig {
    private ID approvalConfigId;
    private ID approvalFlowId;
    private Integer completeTotal;
    private ID createdBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdOn;
    private Integer entityCode;
    private String flowName;
    private Boolean isDisabled;
    private ID modifiedBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date modifiedOn;
    private Integer runningTotal;

    public ID getApprovalConfigId() {
        return this.approvalConfigId;
    }

    public void setApprovalConfigId(ID approvalConfigId2) {
        this.approvalConfigId = approvalConfigId2;
    }

    public ID getApprovalFlowId() {
        return this.approvalFlowId;
    }

    public void setApprovalFlowId(ID approvalFlowId2) {
        this.approvalFlowId = approvalFlowId2;
    }

    public Integer getEntityCode() {
        return this.entityCode;
    }

    public void setEntityCode(Integer entityCode2) {
        this.entityCode = entityCode2;
    }

    public String getFlowName() {
        return this.flowName;
    }

    public void setFlowName(String flowName2) {
        this.flowName = flowName2;
    }

    public Boolean getIsDisabled() {
        return this.isDisabled;
    }

    public void setIsDisabled(Boolean isDisabled2) {
        this.isDisabled = isDisabled2;
    }

    public ID getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(ID createdBy2) {
        this.createdBy = createdBy2;
    }

    public Date getCreatedOn() {
        return this.createdOn;
    }

    public void setCreatedOn(Date createdOn2) {
        this.createdOn = createdOn2;
    }

    public ID getModifiedBy() {
        return this.modifiedBy;
    }

    public void setModifiedBy(ID modifiedBy2) {
        this.modifiedBy = modifiedBy2;
    }

    public Date getModifiedOn() {
        return this.modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn2) {
        this.modifiedOn = modifiedOn2;
    }

    public Integer getRunningTotal() {
        return this.runningTotal;
    }

    public void setRunningTotal(Integer runningTotal2) {
        this.runningTotal = runningTotal2;
    }

    public Integer getCompleteTotal() {
        return this.completeTotal;
    }

    public void setCompleteTotal(Integer completeTotal2) {
        this.completeTotal = completeTotal2;
    }
}
