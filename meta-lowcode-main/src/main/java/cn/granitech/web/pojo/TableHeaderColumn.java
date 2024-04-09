package cn.granitech.web.pojo;

import java.io.Serializable;

public class TableHeaderColumn implements Serializable {
    private String align;
    private String label;
    private String prop;
    private String type;
    private String width;

    public TableHeaderColumn() {
    }

    public TableHeaderColumn(String width2, String align2) {
        this.width = width2;
        this.align = align2;
    }

    public String getProp() {
        return this.prop;
    }

    public void setProp(String prop2) {
        this.prop = prop2;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label2) {
        this.label = label2;
    }

    public String getWidth() {
        return this.width;
    }

    public void setWidth(String width2) {
        this.width = width2;
    }

    public String getAlign() {
        return this.align;
    }

    public void setAlign(String align2) {
        this.align = align2;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type2) {
        this.type = type2;
    }
}
