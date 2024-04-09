package cn.granitech.web.pojo.layout;

import java.util.List;

/**
 * @Description:
 * @Author: VDP 思南
 * @Date: 2020/11/24 16:07
 * @Version: 1.0
 */

public class Row {

    private String title;
    private String id;
    private Integer gutter;
    private List<Cell> cells;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getGutter() {
        return gutter;
    }

    public void setGutter(Integer gutter) {
        this.gutter = gutter;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }

}
