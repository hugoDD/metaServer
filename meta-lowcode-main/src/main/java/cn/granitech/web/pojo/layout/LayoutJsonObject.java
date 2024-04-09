package cn.granitech.web.pojo.layout;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: VDP 思南
 * @Date: 2020/11/24 15:09
 * @Version: 1.0
 */

public class LayoutJsonObject {

    private String labelAlign;
    private String labelPosition;
    private Integer labelWidth;
    private Boolean hideOnlyTabTitle;

    private List<String> tabNames;
    private List<Tab> formTabs;

    public String getLabelAlign() {
        return labelAlign;
    }

    public void setLabelAlign(String labelAlign) {
        this.labelAlign = labelAlign;
    }

    public String getLabelPosition() {
        return labelPosition;
    }

    public void setLabelPosition(String labelPosition) {
        this.labelPosition = labelPosition;
    }

    public Integer getLabelWidth() {
        return labelWidth;
    }

    public void setLabelWidth(Integer labelWidth) {
        this.labelWidth = labelWidth;
    }

    public Boolean getHideOnlyTabTitle() {
        return hideOnlyTabTitle;
    }

    public void setHideOnlyTabTitle(Boolean hideOnlyTabTitle) {
        this.hideOnlyTabTitle = hideOnlyTabTitle;
    }

    public List<String> getTabNames() {
        return tabNames;
    }

    public void setTabNames(List<String> tabNames) {
        this.tabNames = tabNames;
    }

    public List<Tab> getFormTabs() {
        return formTabs;
    }

    public void setFormTabs(List<Tab> formTabs) {
        this.formTabs = formTabs;
    }

    public List<String> getFieldsOfLayout() {
        List<String> resultList = new ArrayList<>();
        for (Tab tab : getFormTabs()) {
            for (Section section : tab.getSections()) {
                for (Row row : section.getRows()) {
                    for (Cell cell : row.getCells()) {
                        if (cell.getFields().size() > 0) {
                            FieldWrapper fieldWrapper = cell.getFields().get(0);
                            if (!"slot".equalsIgnoreCase(fieldWrapper.getType()) &&
                                    !"custom".equalsIgnoreCase(fieldWrapper.getType())) {
                                resultList.add(fieldWrapper.getName());
                            } else { //添加表单插槽、自定义组件绑定的所有数据字段！！
                                if (fieldWrapper.getFields() != null) {
                                    resultList.addAll(fieldWrapper.getFields());
                                }
                            }
                        }
                    }
                }
            }
        }

        return resultList;
    }

}
