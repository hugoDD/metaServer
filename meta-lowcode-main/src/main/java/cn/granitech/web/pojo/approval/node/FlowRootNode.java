package cn.granitech.web.pojo.approval.node;

public class FlowRootNode {
    private String approvalConfigId;
    private String name;
    private NodeConfig nodeConfig;

    public String getApprovalConfigId() {
        return this.approvalConfigId;
    }

    public void setApprovalConfigId(String approvalConfigId2) {
        this.approvalConfigId = approvalConfigId2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public NodeConfig getNodeConfig() {
        return this.nodeConfig;
    }

    public void setNodeConfig(NodeConfig nodeConfig2) {
        this.nodeConfig = nodeConfig2;
    }
}
