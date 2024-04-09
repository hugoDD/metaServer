package cn.granitech.variantorm.pojo;

public class OptionModel {
    private Integer value;

    private String label;

    private Integer displayOrder;

    public OptionModel(Integer value, String label, Integer displayOrder) {
        this.value = value;
        this.label = label;
        this.displayOrder = displayOrder;
    }

    public Integer getValue() {
        return this.value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getDisplayOrder() {
        return this.displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
}
