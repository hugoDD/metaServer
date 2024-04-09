package cn.granitech.web.pojo;

import cn.granitech.variantorm.persistence.EntityRecord;

import java.io.Serializable;

public class BasePojo implements Serializable {
    protected EntityRecord _entityRecord;

    public EntityRecord getEntityRecord() {
        return this._entityRecord;
    }

    public void setEntityRecord(EntityRecord entityRecord) {
        this._entityRecord = entityRecord;
    }

    public void setFieldValue(String fieldName, Object value) {
        if (this._entityRecord == null) {
            throw new IllegalArgumentException("entityRecord prop is null");
        }
        this._entityRecord.setFieldValue(fieldName, value);
    }

    public Object getFieldValue(String fieldName) {
        if (this._entityRecord != null) {
            return this._entityRecord.getFieldValue(fieldName);
        }
        throw new IllegalArgumentException("entityRecord prop is null");
    }

    public void setFieldLabel(String fieldName, String labelValue) {
        if (this._entityRecord == null) {
            throw new IllegalArgumentException("entityRecord prop is null");
        }
        this._entityRecord.setFieldLabel(fieldName, labelValue);
    }

    public String getFieldLabel(String fieldName) {
        if (this._entityRecord != null) {
            return this._entityRecord.getFieldLabel(fieldName);
        }
        throw new IllegalArgumentException("entityRecord prop is null");
    }
}
