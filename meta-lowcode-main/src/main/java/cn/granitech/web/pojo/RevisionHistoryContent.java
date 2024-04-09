package cn.granitech.web.pojo;

import java.io.Serializable;

public class RevisionHistoryContent implements Serializable {
    private Object after;
    private Object before;
    private String fileId;

    public String getFileId() {
        return this.fileId;
    }

    public void setFileId(String fileId2) {
        this.fileId = fileId2;
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
