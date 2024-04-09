package cn.granitech.web.pojo.chart;

public class ChartAxis {
    private AxisFormat axisFormat;
    private String calcMode;
    private boolean columns;
    private String fieldName;
    private String label;
    private String sort;

    public ChartAxis() {
    }

    public ChartAxis(String label2, String fieldName2, String sort2, String calcMode2) {
        this.label = label2;
        this.fieldName = fieldName2;
        this.sort = sort2;
        this.calcMode = calcMode2;
    }

    public boolean isColumns() {
        return this.columns;
    }

    public void setColumns(boolean columns2) {
        this.columns = columns2;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label2) {
        this.label = label2;
    }

    public AxisFormat getAxisFormat() {
        return this.axisFormat;
    }

    public void setAxisFormat(AxisFormat axisFormat2) {
        this.axisFormat = axisFormat2;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public void setFieldName(String fieldName2) {
        this.fieldName = fieldName2;
    }

    public String getSort() {
        return this.sort;
    }

    public void setSort(String sort2) {
        this.sort = sort2;
    }

    public String getCalcMode() {
        return this.calcMode;
    }

    public void setCalcMode(String calcMode2) {
        this.calcMode = calcMode2;
    }
}
