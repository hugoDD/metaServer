package cn.granitech.web.enumration;

public enum MessageEnum {
    ASSIGN_MSG_TO_OWNER("您负责的 %s(%s) 被 %s 分配给 %s。"),
    ASSIGN_MSG_TO_USER("%s 分配了一条新的 %s(%s) 给您。"),
    APPROVAL_MSG_REVIEW("有一条 %s(%s) 审核任务请你审批。"),
    APPROVAL_MSG_SEND("有一条 %s(%s) 审核任务抄送给你。"),
    APPROVAL_MSG_REJECTED("你负责一条 %s(%s) 审核任务被驳回。"),
    APPROVAL_MSG_COMPLETED("你负责一条 %s(%s) 审核任务已完成。"),
    APPROVAL_MSG_REVOCATION("你负责一条 %s(%s) 审核任务被撤销。"),
    APPROVAL_MSG_SUBMIT("你提交一条 %s(%s) 审核任务已结束。"),
    APPROVAL_MSG_REFERRAL("有一条 %s(%s) 审核任务转审给你。"),
    APPROVAL_MSG_SIGN("有一条 %s(%s) 审核任务加签给你。");

    private final String message;

    MessageEnum(String message) {
        this.message = message;
    }

    public String getMessage(Object... args) {
        return String.format(this.message, args);
    }
}
