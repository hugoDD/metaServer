package cn.granitech.web.enumration;

public enum TriggerWhenEnum {
    CREATE(2, "新建"),
    DELETE(4, "删除"),
    UPDATE(8, "更新"),
    ASSIGN(16, "分配"),
    SHARE(32, "共享"),
    UNSHARE(64, "取消共享"),
    APPROVED(128, "审批通过"),
    REVOKED(256, "审批撤销"),
    TIMER(512, "定时"),
    SUBMIT(1024, "审批提交"),
    REJECTED(2048, "审批驳回/撤回");

    private final String maskLabel;
    private final int maskValue;

    TriggerWhenEnum(int maskValue2, String maskLabel2) {
        this.maskValue = maskValue2;
        this.maskLabel = maskLabel2;
    }

    public boolean check(int maskValue2) {
        return (this.maskValue & maskValue2) != 0;
    }

    public int getMaskValue() {
        return this.maskValue;
    }

    public String getMaskLabel() {
        return this.maskLabel;
    }
}
