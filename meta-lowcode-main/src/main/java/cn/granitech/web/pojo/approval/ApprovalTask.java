package cn.granitech.web.pojo.approval;

import cn.granitech.variantorm.metadata.ID;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class ApprovalTask {
    private ID approvalConfigId;
    private ID approvalFlowId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date approvalOn;
    private Integer approvalStatus;
    private ID approvalTaskId;
    private ID approvalUser;
    private ID[] approver;
    private String attrMore;
    private ID[] ccTo;
    private ID createdBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdOn;
    private Integer currentNode;
    private ID entityId;
    private String remark;
    private Integer signType;
    private String stepName;

    public Date getCreatedOn() {
        return this.createdOn;
    }

    public void setCreatedOn(Date createdOn2) {
        this.createdOn = createdOn2;
    }

    public ID getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(ID createdBy2) {
        this.createdBy = createdBy2;
    }

    public ID getApprovalTaskId() {
        return this.approvalTaskId;
    }

    public void setApprovalTaskId(ID approvalTaskId2) {
        this.approvalTaskId = approvalTaskId2;
    }

    public ID getApprovalFlowId() {
        return this.approvalFlowId;
    }

    public void setApprovalFlowId(ID approvalFlowId2) {
        this.approvalFlowId = approvalFlowId2;
    }

    public ID getApprovalConfigId() {
        return this.approvalConfigId;
    }

    public void setApprovalConfigId(ID approvalConfigId2) {
        this.approvalConfigId = approvalConfigId2;
    }

    public ID getApprovalUser() {
        return this.approvalUser;
    }

    public void setApprovalUser(ID approvalUser2) {
        this.approvalUser = approvalUser2;
    }

    public ID getEntityId() {
        return this.entityId;
    }

    public void setEntityId(ID entityId2) {
        this.entityId = entityId2;
    }

    public Integer getCurrentNode() {
        return this.currentNode;
    }

    public void setCurrentNode(Integer currentNode2) {
        this.currentNode = currentNode2;
    }

    public Integer getApprovalStatus() {
        return this.approvalStatus;
    }

    public void setApprovalStatus(Integer approvalStatus2) {
        this.approvalStatus = approvalStatus2;
    }

    public Integer getSignType() {
        return this.signType;
    }

    public void setSignType(Integer signType2) {
        this.signType = signType2;
    }

    public String getStepName() {
        return this.stepName;
    }

    public void setStepName(String stepName2) {
        this.stepName = stepName2;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark2) {
        this.remark = remark2;
    }

    public ID[] getApprover() {
        return this.approver;
    }

    public void setApprover(ID[] approver2) {
        this.approver = approver2;
    }

    public ID[] getCcTo() {
        return this.ccTo;
    }

    public void setCcTo(ID[] ccTo2) {
        this.ccTo = ccTo2;
    }

    public Date getApprovalOn() {
        return this.approvalOn;
    }

    public void setApprovalOn(Date approvalOn2) {
        this.approvalOn = approvalOn2;
    }

    public String getAttrMore() {
        return this.attrMore;
    }

    public void setAttrMore(String attrMore2) {
        this.attrMore = attrMore2;
    }
}
