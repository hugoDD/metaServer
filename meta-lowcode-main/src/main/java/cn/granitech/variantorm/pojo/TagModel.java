package cn.granitech.variantorm.pojo;

public class TagModel {
    private String value;

    private Integer displayOrder;

    public TagModel(String value, Integer displayOrder) {
        this.value = value;
        this.displayOrder = displayOrder;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getDisplayOrder() {
        return this.displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
}
