package cn.granitech.web.pojo.approval.vo;

import cn.granitech.variantorm.metadata.ID;

import java.io.Serializable;

public class RecordApprovalState implements Serializable {
    private Integer approvalStatus;
    private ID approvalTaskId;
    private boolean imApproval;
    private boolean startApproval;


    public Integer getApprovalStatus() {
        return this.approvalStatus;
    }

    public void setApprovalStatus(Integer approvalStatus2) {
        this.approvalStatus = approvalStatus2;
    }

    public ID getApprovalTaskId() {
        return this.approvalTaskId;
    }

    public void setApprovalTaskId(ID approvalTaskId2) {
        this.approvalTaskId = approvalTaskId2;
    }

    public boolean isImApproval() {
        return this.imApproval;
    }

    public void setImApproval(boolean imApproval2) {
        this.imApproval = imApproval2;
    }

    public boolean isStartApproval() {
        return this.startApproval;
    }

    public void setStartApproval(boolean startApproval2) {
        this.startApproval = startApproval2;
    }
}
