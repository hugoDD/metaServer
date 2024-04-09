package cn.granitech.web.pojo;

import cn.granitech.variantorm.metadata.ID;
import cn.granitech.web.pojo.approval.node.NodeRole;

import java.util.List;

public class AddTeamOrRoleUserBody {
    private ID id;
    private List<NodeRole> nodeRoleList;

    public ID getId() {
        return this.id;
    }

    public void setId(ID id2) {
        this.id = id2;
    }

    public List<NodeRole> getNodeRoleList() {
        return this.nodeRoleList;
    }

    public void setNodeRoleList(List<NodeRole> nodeRoleList2) {
        this.nodeRoleList = nodeRoleList2;
    }
}
