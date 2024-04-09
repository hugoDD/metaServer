package cn.granitech.web.pojo;

import cn.granitech.variantorm.pojo.Pagination;

import java.util.List;
import java.util.Map;

public class ReferenceQueryResult {
    private List<Map<String, String>> columnList;
    private List<Map<String, Object>> dataList;
    private String idFieldName;
    private String nameFieldName;
    private Pagination pagination;

    public ReferenceQueryResult(List<Map<String, String>> columnList2, List<Map<String, Object>> dataList2, Pagination pagination2, String idFieldName2, String nameFieldName2) {
        this.columnList = columnList2;
        this.dataList = dataList2;
        this.pagination = pagination2;
        this.idFieldName = idFieldName2;
        this.nameFieldName = nameFieldName2;
    }

    public String getIdFieldName() {
        return this.idFieldName;
    }

    public void setIdFieldName(String idFieldName2) {
        this.idFieldName = idFieldName2;
    }

    public String getNameFieldName() {
        return this.nameFieldName;
    }

    public void setNameFieldName(String nameFieldName2) {
        this.nameFieldName = nameFieldName2;
    }

    public List<Map<String, String>> getColumnList() {
        return this.columnList;
    }

    public void setColumnList(List<Map<String, String>> columnList2) {
        this.columnList = columnList2;
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
}
