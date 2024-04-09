package cn.granitech.web.pojo.approval;

import cn.granitech.variantorm.metadata.ID;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class ApprovalFlow {
    private ID approvalConfigId;
    private ID approvalFlowId;
    private ID createdBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdOn;
    private String flowDefinition;

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

    public String getFlowDefinition() {
        return this.flowDefinition;
    }

    public void setFlowDefinition(String flowDefinition2) {
        this.flowDefinition = flowDefinition2;
    }

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
}
