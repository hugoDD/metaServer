package cn.granitech.web.pojo.approval;

import cn.granitech.variantorm.metadata.ID;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class ApprovalHistory {
    private ID approvalFlowId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date approvalOn;
    private ID approvalTaskId;
    private ID approver;
    private ID[] currentCCTo;
    private Integer currentNode;
    private Boolean isBacked;
    private Integer nodeType;
    private String remark;
    private String stepName;

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark2) {
        this.remark = remark2;
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

    public String getStepName() {
        return this.stepName;
    }

    public void setStepName(String stepName2) {
        this.stepName = stepName2;
    }

    public Integer getCurrentNode() {
        return this.currentNode;
    }

    public void setCurrentNode(Integer currentNode2) {
        this.currentNode = currentNode2;
    }

    public ID[] getCurrentCCTo() {
        return this.currentCCTo;
    }

    public void setCurrentCCTo(ID[] currentCCTo2) {
        this.currentCCTo = currentCCTo2;
    }

    public ID getApprover() {
        return this.approver;
    }

    public void setApprover(ID approver2) {
        this.approver = approver2;
    }

    public Boolean getIsBacked() {
        return this.isBacked;
    }

    public void setIsBacked(Boolean backed) {
        this.isBacked = backed;
    }

    public Date getApprovalOn() {
        return this.approvalOn;
    }

    public void setApprovalOn(Date approvalOn2) {
        this.approvalOn = approvalOn2;
    }

    public Integer getNodeType() {
        return this.nodeType;
    }

    public void setNodeType(Integer nodeType2) {
        this.nodeType = nodeType2;
    }
}
