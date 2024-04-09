package cn.granitech.web.pojo.chart;

import cn.granitech.web.pojo.filter.Filter;

import java.util.List;

public class Chart {
    public static final String CHART_TYPE_AXIS = "axis";
    public static final String CHART_TYPE_NUMBER = "number";
    public static final String CHART_TYPE_PIE = "pie";
    public static final String CHART_TYPE_TABLE = "table";
    private String chartType;
    private String entityName;
    private Filter filter;
    private List<ChartAxis> latitude;
    private List<ChartAxis> longitude;
    private boolean noPrivileges;

    public Chart() {
    }

    public Chart(String entityName2, Filter filter2, String chartType2, List<ChartAxis> latitude2, List<ChartAxis> longitude2, boolean noPrivileges2) {
        this.latitude = latitude2;
        this.longitude = longitude2;
        this.entityName = entityName2;
        this.filter = filter2;
        this.chartType = chartType2;
        this.noPrivileges = noPrivileges2;
    }

    public String getChartType() {
        return this.chartType;
    }

    public void setChartType(String chartType2) {
        this.chartType = chartType2;
    }

    public boolean isNoPrivileges() {
        return this.noPrivileges;
    }

    public void setNoPrivileges(boolean noPrivileges2) {
        this.noPrivileges = noPrivileges2;
    }

    public List<ChartAxis> getLatitude() {
        return this.latitude;
    }

    public void setLatitude(List<ChartAxis> latitude2) {
        this.latitude = latitude2;
    }

    public List<ChartAxis> getLongitude() {
        return this.longitude;
    }

    public void setLongitude(List<ChartAxis> longitude2) {
        this.longitude = longitude2;
    }

    public String getEntityName() {
        return this.entityName;
    }

    public void setEntityName(String entityName2) {
        this.entityName = entityName2;
    }

    public Filter getFilter() {
        return this.filter;
    }

    public void setFilter(Filter filter2) {
        this.filter = filter2;
    }
}
