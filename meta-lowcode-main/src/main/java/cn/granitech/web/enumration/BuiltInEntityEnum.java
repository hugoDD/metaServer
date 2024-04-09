package cn.granitech.web.enumration;

public enum BuiltInEntityEnum {
    FOLLOW_UP("跟进", 54),
    TODO_TASK("待办", 55);

    private final String entityName;
    private final int entityCode;

    BuiltInEntityEnum(String entityName, int entityCode) {
        this.entityName = entityName;
        this.entityCode = entityCode;
    }

    public String getEntityName() {
        return this.entityName;
    }

    public int getEntityCode() {
        return this.entityCode;
    }
}
