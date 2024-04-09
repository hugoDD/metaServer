package cn.granitech.web.pojo;

import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.web.pojo.layout.FieldProps;

import java.util.List;
import java.util.Map;

public class FormQueryResult {
    private List<String> deletedFields;
    private Map<String, FieldProps> fieldPropsMap;
    private EntityRecord formData;
    private Map<String, String> labelData;
    private String layoutJson;

    public FormQueryResult(String layoutJson2, Map<String, FieldProps> fieldPropsMap2, EntityRecord formData2, Map<String, String> labelsMap, List<String> deletedFields2) {
        this.layoutJson = layoutJson2;
        this.fieldPropsMap = fieldPropsMap2;
        this.formData = formData2;
        this.labelData = labelsMap;
        this.deletedFields = deletedFields2;
    }

    public String getLayoutJson() {
        return this.layoutJson;
    }

    public void setLayoutJson(String layoutJson2) {
        this.layoutJson = layoutJson2;
    }

    public Map<String, FieldProps> getFieldPropsMap() {
        return this.fieldPropsMap;
    }

    public void setFieldPropsMap(Map<String, FieldProps> fieldPropsMap2) {
        this.fieldPropsMap = fieldPropsMap2;
    }

    public EntityRecord getFormData() {
        return this.formData;
    }

    public void setFormData(EntityRecord formData2) {
        this.formData = formData2;
    }

    public Map<String, String> getLabelData() {
        return this.labelData;
    }

    public void setLabelData(Map<String, String> labelData2) {
        this.labelData = labelData2;
    }

    public List<String> getDeletedFields() {
        return this.deletedFields;
    }

    public void setDeletedFields(List<String> deletedFields2) {
        this.deletedFields = deletedFields2;
    }
}
