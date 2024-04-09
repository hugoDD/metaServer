package cn.granitech.report.luckySheet;

import java.util.List;
import java.util.Map;


public class ResLuckySheetDataDto {
    private String title;
    private Map<String, Map<String, Object>> hyperlinks;
    private List<Map<String, Object>> celldata;
    private Map<String, Object> config;
    private Map<String, Map<String, Integer>> pagination;
    private Map<String, Integer> maxXAndY;
    private Map<String, Integer> mergePagination;
    private Map<String, Object> frozen;

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<String, Map<String, Object>> getHyperlinks() {
        return this.hyperlinks;
    }

    public void setHyperlinks(Map<String, Map<String, Object>> hyperlinks) {
        this.hyperlinks = hyperlinks;
    }

    public List<Map<String, Object>> getCelldata() {
        return this.celldata;
    }

    public void setCelldata(List<Map<String, Object>> celldata) {
        this.celldata = celldata;
    }

    public Map<String, Object> getConfig() {
        return this.config;
    }

    public void setConfig(Map<String, Object> config) {
        this.config = config;
    }

    public Map<String, Map<String, Integer>> getPagination() {
        return this.pagination;
    }

    public void setPagination(Map<String, Map<String, Integer>> pagination) {
        this.pagination = pagination;
    }

    public Map<String, Integer> getMaxXAndY() {
        return this.maxXAndY;
    }

    public void setMaxXAndY(Map<String, Integer> maxXAndY) {
        this.maxXAndY = maxXAndY;
    }

    public Map<String, Integer> getMergePagination() {
        return this.mergePagination;
    }

    public void setMergePagination(Map<String, Integer> mergePagination) {
        this.mergePagination = mergePagination;
    }

    public Map<String, Object> getFrozen() {
        return this.frozen;
    }

    public void setFrozen(Map<String, Object> frozen) {
        this.frozen = frozen;
    }
}



