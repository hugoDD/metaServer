package cn.granitech.trigger.business.trigger.action;

import cn.granitech.web.pojo.Cascade;
import cn.granitech.web.pojo.approval.node.NodeRole;

import java.util.List;


public class DataShare {
    boolean withUpdate;
    List<NodeRole> toUsersId;
    private List<Cascade> items;

    public boolean isWithUpdate() {
        return this.withUpdate;
    }

    public void setWithUpdate(boolean withUpdate) {
        this.withUpdate = withUpdate;
    }

    public List<NodeRole> getToUsersId() {
        return this.toUsersId;
    }

    public void setToUsersId(List<NodeRole> toUsersId) {
        this.toUsersId = toUsersId;
    }

    public List<Cascade> getItems() {
        return this.items;
    }

    public void setItems(List<Cascade> items) {
        this.items = items;
    }
}



