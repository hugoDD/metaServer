package cn.granitech.web.pojo.approval.node;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class TaskChildNode implements Serializable {
    private boolean addSignaturesApproval;
    private boolean automaticSharing;
    private List<ConditionNodes> conditionNodes;
    private int deptLevel;
    private int entityCode;
    private List<Object> externalUserList;
    private List<Map<String, Object>> modifiableFields;
    private int multiPersonApproval;
    private String nodeName;
    private List<NodeRole> nodeRoleList;
    private int nodeRoleType;
    private List<NodeRole> nodeUserList;
    private List<NodeRole> referralUserList;
    private List<NodeRole> signUserList;
    private boolean transferApproval;
    private int type;
    private boolean userSelectFlag;

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

    public boolean isUserSelectFlag() {
        return this.userSelectFlag;
    }

    public void setUserSelectFlag(boolean userSelectFlag2) {
        this.userSelectFlag = userSelectFlag2;
    }

    public boolean isTransferApproval() {
        return this.transferApproval;
    }

    public void setTransferApproval(boolean transferApproval2) {
        this.transferApproval = transferApproval2;
    }

    public boolean isAddSignaturesApproval() {
        return this.addSignaturesApproval;
    }

    public void setAddSignaturesApproval(boolean addSignaturesApproval2) {
        this.addSignaturesApproval = addSignaturesApproval2;
    }

    public int getDeptLevel() {
        return this.deptLevel;
    }

    public void setDeptLevel(int deptLevel2) {
        this.deptLevel = deptLevel2;
    }

    public int getMultiPersonApproval() {
        return this.multiPersonApproval;
    }

    public void setMultiPersonApproval(int multiPersonApproval2) {
        this.multiPersonApproval = multiPersonApproval2;
    }

    public List<Map<String, Object>> getModifiableFields() {
        return this.modifiableFields;
    }

    public void setModifiableFields(List<Map<String, Object>> modifiableFields2) {
        this.modifiableFields = modifiableFields2;
    }

    public boolean isAutomaticSharing() {
        return this.automaticSharing;
    }

    public void setAutomaticSharing(boolean automaticSharing2) {
        this.automaticSharing = automaticSharing2;
    }

    public List<NodeRole> getNodeUserList() {
        return this.nodeUserList;
    }

    public void setNodeUserList(List<NodeRole> nodeUserList2) {
        this.nodeUserList = nodeUserList2;
    }

    public List<Object> getExternalUserList() {
        return this.externalUserList;
    }

    public void setExternalUserList(List<Object> externalUserList2) {
        this.externalUserList = externalUserList2;
    }

    public List<ConditionNodes> getConditionNodes() {
        return this.conditionNodes;
    }

    public void setConditionNodes(List<ConditionNodes> conditionNodes2) {
        this.conditionNodes = conditionNodes2;
    }

    public List<NodeRole> getReferralUserList() {
        return this.referralUserList;
    }

    public void setReferralUserList(List<NodeRole> referralUserList2) {
        this.referralUserList = referralUserList2;
    }

    public List<NodeRole> getSignUserList() {
        return this.signUserList;
    }

    public void setSignUserList(List<NodeRole> signUserList2) {
        this.signUserList = signUserList2;
    }

    public int getEntityCode() {
        return this.entityCode;
    }

    public void setEntityCode(int entityCode2) {
        this.entityCode = entityCode2;
    }
}
