package cn.granitech.web.pojo;

import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.pojo.OptionModel;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class FormLayout extends BasePojo {
    private ID createdBy;
    private Date createdOn;
    private Integer entityCode;
    private ID formLayoutId;
    private FormUploadParam formUploadParam;
    private String layoutJson;
    private String layoutName;
    private ID modifiedBy;
    private Date modifiedOn;
    private Map<String, List<OptionModel>> optionData;

    public ID getFormLayoutId() {
        this.formLayoutId = this._entityRecord.getFieldValue("formLayoutId");
        return this.formLayoutId;
    }

    public void setFormLayoutId(ID formLayoutId2) {
        this.formLayoutId = formLayoutId2;
        this._entityRecord.setFieldValue("formLayoutId", formLayoutId2);
    }

    public String getLayoutName() {
        this.layoutName = this._entityRecord.getFieldValue("layoutName");
        return this.layoutName;
    }

    public void setLayoutName(String layoutName2) {
        this.layoutName = layoutName2;
        this._entityRecord.setFieldValue("layoutName", layoutName2);
    }

    public Integer getEntityCode() {
        this.entityCode = this._entityRecord.getFieldValue("entityCode");
        return this.entityCode;
    }

    public void setEntityCode(Integer entityCode2) {
        this.entityCode = entityCode2;
        this._entityRecord.setFieldValue("entityCode", entityCode2);
    }

    public String getLayoutJson() {
        this.layoutJson = this._entityRecord.getFieldValue("layoutJson");
        return this.layoutJson;
    }

    public void setLayoutJson(String layoutJson2) {
        this.layoutJson = layoutJson2;
        this._entityRecord.setFieldValue("layoutJson", layoutJson2);
    }

    public Date getCreatedOn() {
        this.createdOn = this._entityRecord.getFieldValue("createdOn");
        return this.createdOn;
    }

    public void setCreatedOn(Date createdOn2) {
        this.createdOn = createdOn2;
        this._entityRecord.setFieldValue("createdOn", createdOn2);
    }

    public ID getCreatedBy() {
        this.createdBy = this._entityRecord.getFieldValue("createdBy");
        return this.createdBy;
    }

    public void setCreatedBy(ID createdBy2) {
        this.createdBy = createdBy2;
        this._entityRecord.setFieldValue("createdBy", createdBy2);
    }

    public Date getModifiedOn() {
        this.modifiedOn = this._entityRecord.getFieldValue("modifiedOn");
        return this.modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn2) {
        this.modifiedOn = modifiedOn2;
        this._entityRecord.setFieldValue("modifiedOn", modifiedOn2);
    }

    public ID getModifiedBy() {
        this.modifiedBy = this._entityRecord.getFieldValue("modifiedBy");
        return this.modifiedBy;
    }

    public void setModifiedBy(ID modifiedBy2) {
        this.modifiedBy = modifiedBy2;
        this._entityRecord.setFieldValue("modifiedBy", modifiedBy2);
    }

    public Map<String, List<OptionModel>> getOptionData() {
        return this.optionData;
    }

    public void setOptionData(Map<String, List<OptionModel>> optionData2) {
        this.optionData = optionData2;
    }

    public FormUploadParam getFormUploadParam() {
        return this.formUploadParam;
    }

    public void setFormUploadParam(FormUploadParam formUploadParam2) {
        this.formUploadParam = formUploadParam2;
    }
}
