package cn.granitech.web.pojo.application;

import cn.granitech.web.pojo.approval.node.NodeRole;

import java.util.List;

public class DingTalkSetting {
    private String dingTalkAgentId;
    private String dingTalkAppKey;
    private String dingTalkAppSecret;
    private List<NodeRole> nodeRole;
    private boolean openStatus;

    public DingTalkSetting() {
    }

    public DingTalkSetting(boolean openStatus2, String dingTalkAppKey2, String dingTalkAppSecret2, String dingTalkAgentId2, List<NodeRole> nodeRole2) {
        this.openStatus = openStatus2;
        this.dingTalkAppKey = dingTalkAppKey2;
        this.dingTalkAppSecret = dingTalkAppSecret2;
        this.dingTalkAgentId = dingTalkAgentId2;
        this.nodeRole = nodeRole2;
    }

    public boolean getOpenStatus() {
        return this.openStatus;
    }

    public void setOpenStatus(boolean openStatus2) {
        this.openStatus = openStatus2;
    }

    public String getDingTalkAppKey() {
        return this.dingTalkAppKey;
    }

    public void setDingTalkAppKey(String dingTalkAppKey2) {
        this.dingTalkAppKey = dingTalkAppKey2;
    }

    public String getDingTalkAppSecret() {
        return this.dingTalkAppSecret;
    }

    public void setDingTalkAppSecret(String dingTalkAppSecret2) {
        this.dingTalkAppSecret = dingTalkAppSecret2;
    }

    public String getDingTalkAgentId() {
        return this.dingTalkAgentId;
    }

    public void setDingTalkAgentId(String dingTalkAgentId2) {
        this.dingTalkAgentId = dingTalkAgentId2;
    }

    public List<NodeRole> getNodeRole() {
        return this.nodeRole;
    }

    public void setNodeRole(List<NodeRole> nodeRole2) {
        this.nodeRole = nodeRole2;
    }
}
