package cn.granitech.web.pojo;

import cn.granitech.variantorm.pojo.Pagination;

import java.util.List;
import java.util.Map;

public class ListQueryResult {
    private List<TableHeaderColumn> columnList;
    private List<Map<String, Object>> dataList;
    private EntityBasicInfo entityBasicInfo;
    private Pagination pagination;

    public ListQueryResult() {
        this.dataList = null;
        this.pagination = null;
        this.columnList = null;
    }

    public ListQueryResult(List<Map<String, Object>> dataList2, Pagination pagination2) {
        this.dataList = dataList2;
        this.pagination = pagination2;
        this.columnList = null;
        this.entityBasicInfo = null;
    }

    public ListQueryResult(List<Map<String, Object>> dataList2, Pagination pagination2, List<TableHeaderColumn> columnList2, EntityBasicInfo entityBasicInfo2) {
        this.dataList = dataList2;
        this.pagination = pagination2;
        this.columnList = columnList2;
        this.entityBasicInfo = entityBasicInfo2;
    }

    public List<Map<String, Object>> getDataList() {
        return this.dataList;
    }

    public void setDataList(List<Map<String, Object>> dataList2) {
        this.dataList = dataList2;
    }

    public Pagination getPagination() {
        return this.pagination;
    }

    public void setPagination(Pagination pagination2) {
        this.pagination = pagination2;
    }

    public List<TableHeaderColumn> getColumnList() {
        return this.columnList;
    }

    public void setColumnList(List<TableHeaderColumn> columnList2) {
        this.columnList = columnList2;
    }

    public EntityBasicInfo getEntityBasicInfo() {
        return this.entityBasicInfo;
    }

    public void setEntityBasicInfo(EntityBasicInfo entityBasicInfo2) {
        this.entityBasicInfo = entityBasicInfo2;
    }
}
