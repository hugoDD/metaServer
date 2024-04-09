package cn.granitech.variantorm.pojo;

public class JointModel {
    private final String selfEntity;

    private final String selfEntityAlias;

    private final String selfField;

    private final String jointEntity;

    private final String jointEntityAlias;

    private final String jointIdField;

    public JointModel(String selfEntity, String selfEntityAlias, String selfField, String jointEntity, String jointEntityAlias, String jointIdField) {
        this.selfEntity = selfEntity;
        this.selfEntityAlias = selfEntityAlias;
        this.selfField = selfField;
        this.jointEntity = jointEntity;
        this.jointEntityAlias = jointEntityAlias;
        this.jointIdField = jointIdField;
    }

    public String getSelfEntity() {
        return this.selfEntity;
    }

    public String getSelfEntityAlias() {
        return this.selfEntityAlias;
    }

    public String getSelfField() {
        return this.selfField;
    }

    public String getJointEntity() {
        return this.jointEntity;
    }

    public String getJointEntityAlias() {
        return this.jointEntityAlias;
    }

    public String getJointIdField() {
        return this.jointIdField;
    }
}
