package cn.granitech.web.pojo.approval.dto;

import cn.granitech.web.pojo.approval.node.NodeRole;

import java.util.List;

public class ApprovalProcessDTO {
    private List<NodeRole> currentCCToUserList;
    private String entityId;
    private Boolean isBacked;
    private List<NodeRole> nextApprovalUserList;
    private String remark;

    public ApprovalProcessDTO() {
    }

    public ApprovalProcessDTO(String entityId2, String remark2, Boolean isBacked2) {
        this.entityId = entityId2;
        this.remark = remark2;
        this.isBacked = isBacked2;
    }

    public String getEntityId() {
        return this.entityId;
    }

    public void setEntityId(String entityId2) {
        this.entityId = entityId2;
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

    public List<NodeRole> getNextApprovalUserList() {
        return this.nextApprovalUserList;
    }

    public void setNextApprovalUserList(List<NodeRole> nextApprovalUserList2) {
        this.nextApprovalUserList = nextApprovalUserList2;
    }

    public List<NodeRole> getCurrentCCToUserList() {
        return this.currentCCToUserList;
    }

    public void setCurrentCCToUserList(List<NodeRole> currentCCToUserList2) {
        this.currentCCToUserList = currentCCToUserList2;
    }
}
