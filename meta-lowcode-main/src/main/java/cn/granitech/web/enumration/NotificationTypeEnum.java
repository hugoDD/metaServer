package cn.granitech.web.enumration;

public enum NotificationTypeEnum {
    APPROVAL_NOTE(10),
    APPROVAL_SEND_COPY(11),
    SYSTEM_MSG(20),
    ASSIGN_MSG(30),
    SHARE_MSG(35),
    TODO_MSG(40);

    private final int type;

    NotificationTypeEnum(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }
}
