package cn.granitech.web.pojo;

import cn.granitech.variantorm.pojo.Field;

import java.util.List;

public class OptionRequestBody {
    private Field field;
    private List<KeyValueEntry<String, Integer>> optionList;

    public Field getField() {
        return this.field;
    }

    public void setField(Field field2) {
        this.field = field2;
    }

    public List<KeyValueEntry<String, Integer>> getOptionList() {
        return this.optionList;
    }

    public void setOptionList(List<KeyValueEntry<String, Integer>> optionList2) {
        this.optionList = optionList2;
    }
}
