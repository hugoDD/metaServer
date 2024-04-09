package cn.granitech.web.pojo;

import cn.granitech.variantorm.metadata.ID;

public class DataListView extends BasePojo {
    private ID dataListViewId;
    private Integer entityCode;
    private String filterJson;
    private String headerJson;
    private String paginationJson;
    private String viewName;

    public ID getDataListViewId() {
        return this.dataListViewId;
    }

    public void setDataListViewId(ID dataListViewId2) {
        this.dataListViewId = dataListViewId2;
    }

    public Integer getEntityCode() {
        return this.entityCode;
    }

    public void setEntityCode(Integer entityCode2) {
        this.entityCode = entityCode2;
    }

    public String getViewName() {
        return this.viewName;
    }

    public void setViewName(String viewName2) {
        this.viewName = viewName2;
    }

    public String getHeaderJson() {
        return this.headerJson;
    }

    public void setHeaderJson(String headerJson2) {
        this.headerJson = headerJson2;
    }

    public String getFilterJson() {
        return this.filterJson;
    }

    public void setFilterJson(String filterJson2) {
        this.filterJson = filterJson2;
    }

    public String getPaginationJson() {
        return this.paginationJson;
    }

    public void setPaginationJson(String paginationJson2) {
        this.paginationJson = paginationJson2;
    }
}
