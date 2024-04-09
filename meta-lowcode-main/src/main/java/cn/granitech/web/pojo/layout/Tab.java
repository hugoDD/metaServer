package cn.granitech.web.pojo.layout;

import java.util.List;

/**
 * @Description:
 * @Author: VDP 思南
 * @Date: 2020/11/24 16:07
 * @Version: 1.0
 */

public class Tab {

    private String title;
    private String id;
    private List<Section> sections;

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

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

}
