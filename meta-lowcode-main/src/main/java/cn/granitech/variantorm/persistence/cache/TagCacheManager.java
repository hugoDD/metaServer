package cn.granitech.variantorm.persistence.cache;

import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.persistence.RecordQuery;
import cn.granitech.variantorm.pojo.TagModel;

import java.util.*;

public class TagCacheManager {
    private final PersistenceManager persistenceManager;
    private final Map<String, List<TagModel>> tagModelMap = new HashMap<>();

    public List<TagModel> getTags(String entityName, String fieldName) {


        String a =  entityName+"_"+fieldName;
        if (!this.tagModelMap.containsKey(a)) {
            this.reloadTagsByField(entityName, fieldName);
        }

        return this.tagModelMap.containsKey(a) ? this.tagModelMap.get(a) : new ArrayList<>();
    }

    public TagCacheManager(PersistenceManager var1) {
        this.persistenceManager = var1;
    }

    public synchronized void reloadTagsByField(String entityName, String fieldName) {
        this.tagModelMap.remove((new StringBuilder()).insert(0, entityName).append('_').append(fieldName).toString());
//        Map a = new J(this, entityName, fieldName);
        Map<String,Object> param = new HashMap<>();
        param.put("entityName",entityName);
        param.put("fieldName",fieldName);
        RecordQuery recordQuery = this.persistenceManager.createRecordQuery();
        String filter = "([entityName] = :entityName) and ([fieldName] = :fieldName)";
        String orderSql = "[entityName], [fieldName], [displayOrder]";
        List<EntityRecord> entityRecords = recordQuery.query("TagItem", filter, param, orderSql, null);
        for (EntityRecord entityRecord : entityRecords) {
            String value = entityRecord.getFieldValue("value");
            Integer displayOrder = entityRecord.getFieldValue("displayOrder");
            String key = entityName + "_" + fieldName;
            TagModel tagModel = new TagModel(value, displayOrder);
            if (this.tagModelMap.containsKey(key)) {
                this.tagModelMap.get(key).add(tagModel);
            } else {
                List<TagModel> list = new ArrayList<>();
                list.add(tagModel);
                this.tagModelMap.put(key, list);
            }
        }

    }
}
