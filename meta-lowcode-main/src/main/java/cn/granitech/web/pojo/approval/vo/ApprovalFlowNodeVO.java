package cn.granitech.web.pojo.approval.vo;

import cn.granitech.web.pojo.approval.node.NodeConfig;

public class ApprovalFlowNodeVO {
    private String approvalConfigId;
    private String flowName;
    private NodeConfig nodeConfig;

    public String getFlowName() {
        return this.flowName;
    }

    public void setFlowName(String flowName2) {
        this.flowName = flowName2;
    }

    public String getApprovalConfigId() {
        return this.approvalConfigId;
    }

    public void setApprovalConfigId(String approvalConfigId2) {
        this.approvalConfigId = approvalConfigId2;
    }

    public NodeConfig getNodeConfig() {
        return this.nodeConfig;
    }

    public void setNodeConfig(NodeConfig nodeConfig2) {
        this.nodeConfig = nodeConfig2;
    }
}
