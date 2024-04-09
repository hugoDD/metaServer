package cn.granitech.web.pojo.layout;

import java.util.List;

/**
 * @Description:
 * @Author: VDP 思南
 * @Date: 2020/11/24 16:08
 * @Version: 1.0
 */

public class Cell {

    private String id;
    private Integer span;

    private List<FieldWrapper> fields;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getSpan() {
        return span;
    }

    public void setSpan(Integer span) {
        this.span = span;
    }

    public List<FieldWrapper> getFields() {
        return fields;
    }

    public void setFields(List<FieldWrapper> fields) {
        this.fields = fields;
    }

}
