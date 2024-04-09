package cn.granitech.web.pojo.approval.vo;

import cn.granitech.variantorm.metadata.ID;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @author ly-dourx
 */
public class ApprovalStepsVO {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdOn;
    private Integer currentNode;
    private Boolean isBacked;
    private Integer nodeType;
    private Integer operationState;
    private ID recordId;
    private String remark;
    private Integer signType;
    private Integer state;
    private String stepName;
    private String stepUserName;

    public ID getRecordId() {
        return this.recordId;
    }

    public void setRecordId(ID recordId2) {
        this.recordId = recordId2;
    }

    public String getStepUserName() {
        return this.stepUserName;
    }

    public void setStepUserName(String stepUserName2) {
        this.stepUserName = stepUserName2;
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

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark2) {
        this.remark = remark2;
    }

    public Boolean getIsBacked() {
        return this.isBacked;
    }

    public void setIsBacked(Boolean isBacked2) {
        this.isBacked = isBacked2;
    }

    public Integer getState() {
        return this.state;
    }

    public void setState(Integer state2) {
        this.state = state2;
    }

    public Date getCreatedOn() {
        return this.createdOn;
    }

    public void setCreatedOn(Date createdOn2) {
        this.createdOn = createdOn2;
    }

    public Integer getOperationState() {
        return this.operationState;
    }

    public void setOperationState(Integer operationState2) {
        this.operationState = operationState2;
    }

    public Integer getSignType() {
        return this.signType;
    }

    public void setSignType(Integer signType2) {
        this.signType = signType2;
    }

    public Integer getNodeType() {
        return this.nodeType;
    }

    public void setNodeType(Integer nodeType2) {
        this.nodeType = nodeType2;
    }
}
