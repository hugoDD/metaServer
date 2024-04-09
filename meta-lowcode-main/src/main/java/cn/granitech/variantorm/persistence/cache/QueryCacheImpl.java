//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cn.granitech.variantorm.persistence.cache;

import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.pojo.IDName;
import cn.granitech.variantorm.pojo.OptionModel;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class QueryCacheImpl implements QueryCache {
    private final Map<String, IDName> idNameMap = new ConcurrentHashMap<>();
    private final OptionCacheManager optionCacheManager;
    private final Map<String, List<IDName>> userRoleMap = new ConcurrentHashMap<>();
    private final Map<String, List<IDName>> approvalTaskCcToMap = new ConcurrentHashMap<>();
    private final TagCacheManager tagCacheManager;
    private final Map<String, List<IDName>> approvalTaskApprover = new ConcurrentHashMap<>();

    public TagCacheManager getTagCacheManager() {
        return this.tagCacheManager;
    }

    public void deleteIDName(String refId) {
    }

    public void reloadReferenceListCache(String entityName, String refFieldName, String recordId) {
        if ("User".equals(entityName) && "roles".equals(refFieldName)) {
            userRoleMap.get(recordId);
        } else if ("ApprovalTask".equals(entityName) && "approver".equals(refFieldName)) {
            this.approvalTaskApprover.get(recordId);
        } else if ("ApprovalTask".equals(entityName) && "ccTo".equals(refFieldName)) {
            this.approvalTaskCcToMap.get(recordId);
        }
    }

    public void initIDName() {
    }

    public IDName getIDName(String refId) {

        return this.idNameMap.get(refId);
    }

    public void initIDNameList() {
    }

    public OptionCacheManager getOptionCacheManager() {
        return this.optionCacheManager;
    }

    public boolean updateIDName(IDName idName) {
        if (this.idNameMap.containsKey(idName.getId().toString())) {
            this.idNameMap.put(idName.getId().toString(), idName);
            return true;
        } else {
            return false;
        }
    }

    public QueryCacheImpl(PersistenceManager pm) {
        this.optionCacheManager = new OptionCacheManager(pm);
        this.tagCacheManager = new TagCacheManager(pm);
    }

    public boolean updateIDNameList(String entityName, String refFieldName, String recordId, List<IDName> idNameList) {
        if ("User".equals(entityName) && "roles".equals(refFieldName)) {
            userRoleMap.put(recordId,idNameList);
            return true;
        } else if ("ApprovalTask".equals(entityName) && "approver".equals(refFieldName)) {
            this.approvalTaskApprover.put(recordId,idNameList);
            return true;
        } else if ("ApprovalTask".equals(entityName) && "ccTo".equals(refFieldName)) {
            this.approvalTaskCcToMap.put(recordId,idNameList);
            return true;
        } else {
            return false;
        }
    }

    public List<IDName> getIDNameList(String entityName, String refFieldName, String recordId) {
        if ("User".equals(entityName) && "roles".equals(refFieldName)) {
            return this.userRoleMap.get(recordId);
        } else if ("ApprovalTask".equals(entityName) && "approver".equals(refFieldName)) {
            return this.approvalTaskApprover.get(recordId);
        } else if ("ApprovalTask".equals(entityName) && "ccTo".equals(refFieldName)) {
            return this.approvalTaskCcToMap.get(recordId);
        } else {
            return null;
        }
    }

    public OptionModel getStatus(String entityName, String fieldName, int statusValue) {
        List<OptionModel> options = this.optionCacheManager.getOptions(entityName, fieldName);
        return options.stream().filter(o -> Objects.equals(o.getValue(), statusValue)).findFirst().orElse(null);

    }

    public OptionModel getOption(String entityName, String fieldName, int optionValue) {
        List<OptionModel> options = this.optionCacheManager.getOptions(entityName, fieldName);
        return options.stream().filter(o -> Objects.equals(o.getValue(), optionValue)).findFirst().orElse(null);



    }
}
