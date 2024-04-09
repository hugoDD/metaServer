package cn.granitech.trigger.business.trigger.action;


class AggregationItem {
    private String sourceField;
    private String targetField;
    private String calcMode;
    private String asName;

    public String getAsName() {
        return this.asName;
    }

    public void setAsName(String asName) {
        this.asName = asName;
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

    public String getCalcMode() {
        return this.calcMode;
    }

    public void setCalcMode(String calcMode) {
        this.calcMode = calcMode;
    }
}



