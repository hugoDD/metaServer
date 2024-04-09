package cn.granitech.web.pojo;

import java.io.Serializable;

public class EntityBasicInfo implements Serializable {
    private String idField;
    private String label;
    private String name;
    private String nameField;

    public EntityBasicInfo(String name2, String label2, String idFieldName, String nameFieldName) {
        this.name = name2;
        this.label = label2;
        this.idField = idFieldName;
        this.nameField = nameFieldName;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label2) {
        this.label = label2;
    }

    public String getIdField() {
        return this.idField;
    }

    public void setIdField(String idField2) {
        this.idField = idField2;
    }

    public String getNameField() {
        return this.nameField;
    }

    public void setNameField(String nameField2) {
        this.nameField = nameField2;
    }
}
