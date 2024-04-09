package cn.granitech.web.pojo;

import java.util.List;

public class BatchOperation {
    List<Cascade> cascades;
    List<String> recordIds;
    String toUser;
    List<String> toUsersId;
    boolean withUpdate;

    public String getToUser() {
        return this.toUser;
    }

    public void setToUser(String toUser2) {
        this.toUser = toUser2;
    }

    public List<String> getRecordIds() {
        return this.recordIds;
    }

    public void setRecordIds(List<String> recordIds2) {
        this.recordIds = recordIds2;
    }

    public List<Cascade> getCascades() {
        return this.cascades;
    }

    public void setCascades(List<Cascade> cascades2) {
        this.cascades = cascades2;
    }

    public List<String> getToUsersId() {
        return this.toUsersId;
    }

    public void setToUsersId(List<String> toUsersId2) {
        this.toUsersId = toUsersId2;
    }

    public boolean isWithUpdate() {
        return this.withUpdate;
    }

    public void setWithUpdate(boolean withUpdate2) {
        this.withUpdate = withUpdate2;
    }
}
