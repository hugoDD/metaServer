package cn.granitech.web.pojo;

import cn.granitech.variantorm.pojo.Entity;

import java.io.Serializable;

public class CopyEntityBody implements Serializable {
    private String mainEntityName;
    private int operations;
    private Entity sourceEntity;

    public Entity getSourceEntity() {
        return this.sourceEntity;
    }

    public void setSourceEntity(Entity sourceEntity2) {
        this.sourceEntity = sourceEntity2;
    }

    public String getMainEntityName() {
        return this.mainEntityName;
    }

    public void setMainEntityName(String mainEntityName2) {
        this.mainEntityName = mainEntityName2;
    }

    public int getOperations() {
        return this.operations;
    }

    public void setOperations(int operations2) {
        this.operations = operations2;
    }
}
