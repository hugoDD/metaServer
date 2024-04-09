package cn.granitech.business.plugins.trigger;

import java.util.List;
import java.util.Map;

public class TriggerLock {
    private final int hierarchyNumber;
    private List<String> deleteRecordIdList;
    private String executeTriggerId;
    private Map<String, Object> updateDataCache;

    public TriggerLock() {
        this.hierarchyNumber = 1;
    }

    public TriggerLock(int hierarchyNumber2) {
        this.hierarchyNumber = hierarchyNumber2;
    }

    public String getExecuteTriggerId() {
        return this.executeTriggerId;
    }

    public void setExecuteTriggerId(String executeTriggerId2) {
        this.executeTriggerId = executeTriggerId2;
    }

    public TriggerLock getNextHierarchy() {
        return new TriggerLock(this.hierarchyNumber + 1);
    }

    public int getHierarchyNumber() {
        return this.hierarchyNumber;
    }

    public Map<String, Object> getUpdateDataCache() {
        return this.updateDataCache;
    }

    public void setUpdateDataCache(Map<String, Object> updateDataCache2) {
        this.updateDataCache = updateDataCache2;
    }

    public List<String> getDeleteRecordIdList() {
        return this.deleteRecordIdList;
    }

    public void setDeleteRecordIdList(List<String> deleteRecordIdList2) {
        this.deleteRecordIdList = deleteRecordIdList2;
    }
}
