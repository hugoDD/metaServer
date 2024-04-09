package cn.granitech.web.pojo.filter;

import java.io.Serializable;

public class FilterItem implements Serializable {
    String fieldName;
    String op;
    String value;
    String value2;

    public FilterItem() {
    }

    public FilterItem(String fieldName2, String op2, String value3) {
        this.fieldName = fieldName2;
        this.op = op2;
        this.value = value3;
    }

    public FilterItem(String fieldName2, String op2, String value3, String value22) {
        this.fieldName = fieldName2;
        this.op = op2;
        this.value = value3;
        this.value2 = value22;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public void setFieldName(String fieldName2) {
        this.fieldName = fieldName2;
    }

    public String getOp() {
        return this.op;
    }

    public void setOp(String op2) {
        this.op = op2;
    }

    public String getValue2() {
        return this.value2;
    }

    public void setValue2(String value22) {
        this.value2 = value22;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value3) {
        this.value = value3;
    }
}
