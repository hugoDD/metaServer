package cn.granitech.web.pojo.approval.node;

import cn.granitech.web.pojo.filter.Filter;

import java.util.List;

public class NodeConfig {
    private ChildNode childNode;
    private Filter filter;
    private String nodeName;
    private List<NodeRole> nodeRoleList;
    private int nodeRoleType;
    private int type;

    public String getNodeName() {
        return this.nodeName;
    }

    public void setNodeName(String nodeName2) {
        this.nodeName = nodeName2;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type2) {
        this.type = type2;
    }

    public int getNodeRoleType() {
        return this.nodeRoleType;
    }

    public void setNodeRoleType(int nodeRoleType2) {
        this.nodeRoleType = nodeRoleType2;
    }

    public List<NodeRole> getNodeRoleList() {
        return this.nodeRoleList;
    }

    public void setNodeRoleList(List<NodeRole> nodeRoleList2) {
        this.nodeRoleList = nodeRoleList2;
    }

    public Filter getFilter() {
        return this.filter;
    }

    public void setFilter(Filter filter2) {
        this.filter = filter2;
    }

    public ChildNode getChildNode() {
        return this.childNode;
    }

    public void setChildNode(ChildNode childNode2) {
        this.childNode = childNode2;
    }
}
