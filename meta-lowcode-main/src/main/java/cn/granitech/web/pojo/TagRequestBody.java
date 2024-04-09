package cn.granitech.web.pojo;

import cn.granitech.variantorm.pojo.Field;

import java.util.List;

public class TagRequestBody {
    private Field field;
    private List<String> tagList;

    public Field getField() {
        return this.field;
    }

    public void setField(Field field2) {
        this.field = field2;
    }

    public List<String> getTagList() {
        return this.tagList;
    }

    public void setTagList(List<String> tagList2) {
        this.tagList = tagList2;
    }
}
