package cn.granitech.web.pojo.layout;

import java.util.List;

/**
 * @Description:
 * @Author: VDP 思南
 * @Date: 2020/11/24 16:09
 * @Version: 1.0
 */

public class FieldWrapper {

    private Boolean reserved;
    private String name;
    private String label;
    private String type;

    /* 字段是否已被删除标志 */
    //暂未用
    //private Boolean deleted;  //暂未用

    private Integer labelWidth;
    private Boolean unDraggable;

    /* 表单插槽绑定的数据字段 */
    private List<String> fields;

    /* 表单自定义组件名称 */
    //暂未用
    //private String componentName;

    public Boolean getReserved() {
        return reserved;
    }

    public void setReserved(Boolean reserved) {
        this.reserved = reserved;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getLabelWidth() {
        return labelWidth;
    }

    public void setLabelWidth(Integer labelWidth) {
        this.labelWidth = labelWidth;
    }

    public Boolean getUnDraggable() {
        return unDraggable;
    }

    public void setUnDraggable(Boolean unDraggable) {
        this.unDraggable = unDraggable;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

//    public String getComponentName() {
//        return componentName;
//    }
//
//    public void setComponentName(String componentName) {
//        this.componentName = componentName;
//    }

}
