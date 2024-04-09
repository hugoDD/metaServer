package cn.granitech.web.pojo.approval.dto;

import cn.granitech.variantorm.metadata.ID;
import cn.granitech.web.pojo.approval.node.NodeRole;

import java.util.List;

public class ApprovalTaskOperationDTO {
    private ID entityId;
    private List<NodeRole> nodeRoleList;
    private int type;

    public int getType() {
        return this.type;
    }

    public void setType(int type2) {
        this.type = type2;
    }

    public ID getEntityId() {
        return this.entityId;
    }

    public void setEntityId(ID entityId2) {
        this.entityId = entityId2;
    }

    public List<NodeRole> getNodeRoleList() {
        return this.nodeRoleList;
    }

    public void setNodeRoleList(List<NodeRole> nodeRoleList2) {
        this.nodeRoleList = nodeRoleList2;
    }
}
