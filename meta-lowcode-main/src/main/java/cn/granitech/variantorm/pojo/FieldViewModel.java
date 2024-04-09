package cn.granitech.variantorm.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FieldViewModel {
    private Integer precision;

    private Integer maxValue;

    private Integer minValue;

    private Integer minLength;

    private Integer maxLength;

    private Integer rows;

    private Integer maxFileCount;

    private Integer fileMaxSize;

    private List<String> uploadFileTypes;

    private String uploadHint;

    private Integer searchDialogWidth;

    private List<String> validators;

    private Integer areaDataType;

    public Integer getPrecision() {
        return this.precision;
    }

    public void setPrecision(Integer precision) {
        this.precision = precision;
    }

    public Integer getMaxValue() {
        return this.maxValue;
    }

    public void setMaxValue(Integer maxValue) {
        this.maxValue = maxValue;
    }

    public Integer getMinValue() {
        return this.minValue;
    }

    public void setMinValue(Integer minValue) {
        this.minValue = minValue;
    }

    public Integer getMinLength() {
        return this.minLength;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public Integer getMaxLength() {
        return this.maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public Integer getRows() {
        return this.rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getMaxFileCount() {
        return this.maxFileCount;
    }

    public void setMaxFileCount(Integer maxFileCount) {
        this.maxFileCount = maxFileCount;
    }

    public Integer getFileMaxSize() {
        return this.fileMaxSize;
    }

    public void setFileMaxSize(Integer fileMaxSize) {
        this.fileMaxSize = fileMaxSize;
    }

    public List<String> getUploadFileTypes() {
        return this.uploadFileTypes;
    }

    public void setUploadFileTypes(List<String> uploadFileTypes) {
        this.uploadFileTypes = uploadFileTypes;
    }

    public String getUploadHint() {
        return this.uploadHint;
    }

    public void setUploadHint(String uploadHint) {
        this.uploadHint = uploadHint;
    }

    public Integer getSearchDialogWidth() {
        return this.searchDialogWidth;
    }

    public void setSearchDialogWidth(Integer searchDialogWidth) {
        this.searchDialogWidth = searchDialogWidth;
    }

    public List<String> getValidators() {
        return this.validators;
    }

    public void setValidators(List<String> validators) {
        this.validators = validators;
    }

    public Integer getAreaDataType() {
        return this.areaDataType;
    }

    public void setAreaDataType(Integer areaDataType) {
        this.areaDataType = areaDataType;
    }
}
