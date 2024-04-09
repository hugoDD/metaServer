package cn.granitech.web.pojo.filter;

import java.util.List;

public class Filter {
    private String equation;
    private List<FilterItem> items;

    public Filter() {
    }

    public Filter(String equation2, List<FilterItem> items2) {
        this.equation = equation2;
        this.items = items2;
    }

    public String getEquation() {
        return this.equation;
    }

    public void setEquation(String equation2) {
        this.equation = equation2;
    }

    public List<FilterItem> getItems() {
        return this.items;
    }

    public void setItems(List<FilterItem> items2) {
        this.items = items2;
    }
}
