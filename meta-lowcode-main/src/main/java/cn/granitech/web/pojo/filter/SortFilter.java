package cn.granitech.web.pojo.filter;


import java.io.Serializable;

public class SortFilter implements Serializable {
    private String fieldName;
    private String type;


    public String getFieldName() {
        return this.fieldName;
    }

    public void setFieldName(String fieldName2) {
        this.fieldName = fieldName2;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type2) {
        this.type = type2;
    }
}
