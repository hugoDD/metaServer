package cn.granitech.web.pojo;

import cn.granitech.variantorm.metadata.ID;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImportRequestBody {
    private Map<String, Integer> fieldsMapping;
    private String filePath;
    private String mainEntity;
    private ID owningUser;
    private String[] repeatFields;
    private Integer repeatOpt;

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath2) {
        this.filePath = filePath2;
    }

    public String getMainEntity() {
        return this.mainEntity;
    }

    public void setMainEntity(String mainEntity2) {
        this.mainEntity = mainEntity2;
    }

    public Integer getRepeatOpt() {
        return this.repeatOpt;
    }

    public void setRepeatOpt(Integer repeatOpt2) {
        this.repeatOpt = repeatOpt2;
    }

    public String[] getRepeatFields() {
        return this.repeatFields;
    }

    public void setRepeatFields(String[] repeatFields2) {
        this.repeatFields = repeatFields2;
    }

    public ID getOwningUser() {
        return this.owningUser;
    }

    public void setOwningUser(ID owningUser2) {
        this.owningUser = owningUser2;
    }

    public Map<String, Integer> getFieldsMapping() {
        return this.fieldsMapping;
    }

    public void setFieldsMapping(Map<String, Integer> fieldsMapping2) {
        this.fieldsMapping = fieldsMapping2;
    }
}
