package cn.granitech.web.pojo.layout;

import cn.granitech.variantorm.pojo.FieldViewModel;
import cn.granitech.variantorm.pojo.OptionModel;
import cn.granitech.variantorm.pojo.TagModel;

import java.util.List;

public class FieldProps {
    private boolean creatable;
    private FieldViewModel fieldViewModel;
    private String label;
    private String name;
    private boolean nullable;
    private List<OptionModel> optionList;
    private List<TagModel> tagList;
    private String type;
    private boolean updatable;

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

    public boolean isNullable() {
        return this.nullable;
    }

    public void setNullable(boolean nullable2) {
        this.nullable = nullable2;
    }

    public boolean isCreatable() {
        return this.creatable;
    }

    public void setCreatable(boolean creatable2) {
        this.creatable = creatable2;
    }

    public boolean isUpdatable() {
        return this.updatable;
    }

    public void setUpdatable(boolean updatable2) {
        this.updatable = updatable2;
    }

    public FieldViewModel getFieldViewModel() {
        return this.fieldViewModel;
    }

    public void setFieldViewModel(FieldViewModel fieldViewModel2) {
        this.fieldViewModel = fieldViewModel2;
    }

    public List<OptionModel> getOptionList() {
        return this.optionList;
    }

    public void setOptionList(List<OptionModel> optionList2) {
        this.optionList = optionList2;
    }

    public List<TagModel> getTagList() {
        return this.tagList;
    }

    public void setTagList(List<TagModel> tagList2) {
        this.tagList = tagList2;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type2) {
        this.type = type2;
    }
}
