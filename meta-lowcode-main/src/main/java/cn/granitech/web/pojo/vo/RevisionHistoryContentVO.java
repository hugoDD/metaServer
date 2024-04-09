package cn.granitech.web.pojo.vo;

import java.io.Serializable;

public class RevisionHistoryContentVO implements Serializable {
    private Object after;
    private Object before;
    private String label;

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label2) {
        this.label = label2;
    }

    public Object getBefore() {
        return this.before;
    }

    public void setBefore(Object before2) {
        this.before = before2;
    }

    public Object getAfter() {
        return this.after;
    }

    public void setAfter(Object after2) {
        this.after = after2;
    }
}
