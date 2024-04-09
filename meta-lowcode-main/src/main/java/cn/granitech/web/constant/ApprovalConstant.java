package cn.granitech.web.constant;

public class ApprovalConstant {
    public static final int APPROVAL_COUNTER_SIGN = 1;
    public static final int APPROVAL_DEP_OWNER_DATA = 6;
    public static final int APPROVAL_DEP_OWNER_INITIATOR = 5;
    public static final int APPROVAL_DEP_OWNER_SPECIFIC = 4;
    public static final String APPROVAL_ENTITY_CODE_KEY = "entityCode";
    public static final String APPROVAL_ENTITY_ID_KEY = "entityId";
    public static final String APPROVAL_FLOW_DEFINITION_KEY = "flowDefinition";
    public static final String APPROVAL_FLOW_ID_KEY = "approvalFlowId";
    public static final String APPROVAL_NO_USER = "该节点无审核人，自动跳过节点";
    public static final int APPROVAL_OPERATION_REFERRAL = 1;
    public static final int APPROVAL_OPERATION_SIGN = 2;
    public static final int APPROVAL_OR_SIGN = 2;
    public static final int APPROVAL_STATUS_COMPLETED = 3;
    public static final int APPROVAL_STATUS_PENDING = 0;
    public static final int APPROVAL_STATUS_REJECTED = 2;
    public static final int APPROVAL_STATUS_REVOCATION = 4;
    public static final int APPROVAL_STATUS_UNDER_REVIEW = 1;
    public static final String APPROVAL_STR_AUTO = "系统自动审批";
    public static final String APPROVAL_STR_END = "流程已结束";
    public static final String APPROVAL_STR_SKIP = "系统自动处理";
    public static final int APPROVAL_TYPE_APPROVE = 1;
    public static final int APPROVAL_TYPE_APPROVE_END = 13;
    public static final int APPROVAL_TYPE_APPROVE_REJECTED = 11;
    public static final int APPROVAL_TYPE_APPROVE_REVOKE = 12;
    public static final int APPROVAL_TYPE_CONDITION = 3;
    public static final int APPROVAL_TYPE_END = -1;
    public static final int APPROVAL_TYPE_ROUTE = 4;
    public static final int APPROVAL_TYPE_SEND = 2;
    public static final int APPROVAL_TYPE_START = 0;
    public static final int APPROVAL_USER_ALL = 1;
    public static final int APPROVAL_USER_APPOINT = 3;
    public static final int APPROVAL_USER_SELF = 2;
    public static final String AUTO_APPROVAL_NODE_JSON = "[{\"type\":1,\"nodeName\":\"系统自动审批\",\"nodeRoleList\":[],\"nodeRoleType\":1,\"multiPersonApproval\":1}]";
    public static final String NO_APPROVAL_NODE_JSON = "[{\"type\":1,\"nodeName\":\"无审核节点,审核结束\",\"nodeRoleList\":[],\"nodeRoleType\":1,\"multiPersonApproval\":1}]";

    private ApprovalConstant() {
    }
}
