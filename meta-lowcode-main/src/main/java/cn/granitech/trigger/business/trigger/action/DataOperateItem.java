package cn.granitech.trigger.business.trigger.action;


class DataOperateItem {
    private String sourceField;
    private String targetField;
    private String updateMode;
    private Boolean simpleFormula;

    public Boolean getSimpleFormula() {
        return this.simpleFormula;
    }

    public void setSimpleFormula(Boolean simpleFormula) {
        simpleFormula = simpleFormula;
    }

    public String getSourceField() {
        return this.sourceField;
    }

    public void setSourceField(String sourceField) {
        this.sourceField = sourceField;
    }

    public String getTargetField() {
        return this.targetField;
    }

    public void setTargetField(String targetField) {
        this.targetField = targetField;
    }

    public String getUpdateMode() {
        return this.updateMode;
    }

    public void setUpdateMode(String updateMode) {
        this.updateMode = updateMode;
    }
}



