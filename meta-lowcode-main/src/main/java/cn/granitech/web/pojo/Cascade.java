package cn.granitech.web.pojo;

public class Cascade {
    private String entityName;
    private String fieldName;
    private boolean isReferenced;

    public String getEntityName() {
        return this.entityName;
    }

    public void setEntityName(String entityName2) {
        this.entityName = entityName2;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public void setFieldName(String fieldName2) {
        this.fieldName = fieldName2;
    }

    public boolean isReferenced() {
        return this.isReferenced;
    }

    public void setIsReferenced(boolean referenced) {
        this.isReferenced = referenced;
    }
}
