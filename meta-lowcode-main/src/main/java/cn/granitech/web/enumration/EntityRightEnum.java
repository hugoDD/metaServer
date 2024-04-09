package cn.granitech.web.enumration;

public enum EntityRightEnum {
    query(1),
    create(2),
    update(3),
    delete(4),
    assign(5),
    share(6);

    private final Integer rightType;

    EntityRightEnum(Integer rightType) {
        this.rightType = rightType;
    }

    public Integer getRightType() {
        return this.rightType;
    }

    public String getCode(Integer entityCode) {
        return "r" + entityCode + "-" + this.rightType;
    }
}
