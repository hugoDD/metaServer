package cn.granitech.variantorm.pojo;

public class QuerySchema {
    private String selectFields;

    private String mainEntity;

    private String filter;

    private String sort;

    private String groupBy;

    private Boolean listResult;

    public String getSelectFields() {
        return this.selectFields;
    }

    public void setSelectFields(String selectFields) {
        this.selectFields = selectFields;
    }

    public String getMainEntity() {
        return this.mainEntity;
    }

    public void setMainEntity(String mainEntity) {
        this.mainEntity = mainEntity;
    }

    public String getFilter() {
        return this.filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getSort() {
        return this.sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getGroupBy() {
        return this.groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    public Boolean getListResult() {
        return this.listResult;
    }

    public void setListResult(Boolean listResult) {
        this.listResult = listResult;
    }
}
