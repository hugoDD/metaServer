package cn.granitech.web.enumration;


/**
 * @author ly-dourx
 */

public enum SystemRightEnum {
    ADMIN_ROLE("r6000"),
    ENTITY_MANAGE("r6001"),
    ENTITY_DELETE("r6002"),
    LAYOUT_FORM_MANAGE("r6003"),
    OPTION_MANAGE("r6005"),
    TAG_MANAGE("r6006"),
    NAVIGATION_MANAGE("r6007"),
    ENTITY_LAYOUT_MANAGE("r6008"),
    RECYCLE_BIN_MANAGE("r6009"),
    RECYCLE_HISTORY_MANAGE("r6010"),
    RECYCLE_IMPORT_MANAGE("r6011"),
    APPROVAL_REVOCATION_MANAGE("r6013"),
    LOGIN_LOG_MANAGE("r6014"),
    TRIGGER_LOG_MANAGE("r6015"),
    APPROVAL_MANAGE("r6016"),
    DEVELOPMENT("r6017");

    final String roleCode;

    SystemRightEnum(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getCode() {
        return this.roleCode;
    }
}
