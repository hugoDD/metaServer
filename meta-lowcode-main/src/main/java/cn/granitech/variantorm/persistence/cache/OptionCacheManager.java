package cn.granitech.variantorm.persistence.cache;


import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.persistence.RecordQuery;
import cn.granitech.variantorm.pojo.OptionModel;

import java.util.*;

import static cn.granitech.variantorm.constant.SystemEntities.OptionItem;

public class OptionCacheManager {
    private final Map<String, List<OptionModel>> statusesMap = new HashMap<>();
    private static final String UNDERLINE = "_";
    private final Map<String, List<OptionModel>> optionsMap = new HashMap<>();
    private PersistenceManager pm;

    public synchronized void reloadOptionsByField(String entityName, String fieldName) {
        this.optionsMap.remove( entityName+"_"+fieldName);
        Map<String,Object> param = new HashMap<>();
        param.put("entityName",entityName);
        param.put("fieldName",fieldName);
        RecordQuery recordQuery = this.pm.createRecordQuery();
        String filter = "([entityName] = :entityName) and ([fieldName] = :fieldName)";
        String sort = "[entityName], [fieldName], [displayOrder]";

        List<EntityRecord> entityRecordList = recordQuery.query(OptionItem, filter, param, sort, null);

        for (EntityRecord entityRecord :entityRecordList) {
            Integer value = entityRecord.getFieldValue("value");
            String label = entityRecord.getFieldValue("label");
            Integer displayOrder = entityRecord.getFieldValue("displayOrder");
            String key = entityName + UNDERLINE + fieldName;
            OptionModel optionModel = new OptionModel(value, label, displayOrder);
            if (this.optionsMap.containsKey(key)) {
                this.optionsMap.get(key).add(optionModel);
            } else {
                this.optionsMap.put(key, Collections.singletonList(optionModel));
            }
        }

    }


    public List<OptionModel> getOptions(String entityName, String fieldName) {

        if (this.optionsMap.size() <= 0) {
            this.initOptions();
        }
        String key =  entityName+ UNDERLINE +fieldName;
        if (!this.optionsMap.containsKey(key)) {
            this.reloadOptionsByField(entityName, fieldName);
        }

        return this.optionsMap.getOrDefault(key, Collections.emptyList());
    }

    private synchronized  void initOptions() {
        OptionCacheManager optionCacheManager = this;
        optionCacheManager.optionsMap.clear();
        List<EntityRecord> entityRecords = pm.createRecordQuery().query("OptionItem", null, null, "[entityName], [fieldName], [displayOrder]", null);
        for (EntityRecord entityRecord : entityRecords) {
            String entityName = entityRecord.getFieldValue("entityName");
            String fieldName = entityRecord.getFieldValue("fieldName");
            Integer value = entityRecord.getFieldValue("value");
            String label = entityRecord.getFieldValue("label");
            Integer displayOrder = entityRecord.getFieldValue("displayOrder");
            String key =  entityName+UNDERLINE+fieldName;
            OptionModel optionModel = new OptionModel(value, label, displayOrder);
            if (this.optionsMap.containsKey(key)) {
                this.optionsMap.get(key).add(optionModel);
            }else {

                List<OptionModel> list = new ArrayList<>();
                list.add(optionModel);
                this.optionsMap.put(key, list);
            }
        }
    }

    public OptionCacheManager(PersistenceManager pm) {
        this.pm = pm;
    }

    public List<OptionModel> getStatuses(String statusFieldName) {
        if (this.statusesMap.size() <= 0) {
            this.initStatusesMap();
        }

        return this.statusesMap.getOrDefault(statusFieldName, Collections.emptyList());
    }

    private synchronized  void initStatusesMap() {
        OptionCacheManager optionCacheManager = this;
        optionCacheManager.statusesMap.clear();
        List<EntityRecord> list = pm.createRecordQuery().query("StatusItem", null, null,"[fieldName], [displayOrder]", null);
        for (EntityRecord a : list) {
            String fieldName = a.getFieldValue("fieldName");
            Integer value = a.getFieldValue("value");
            String label = a.getFieldValue("label");
            Integer displayOrder = a.getFieldValue("displayOrder");
            OptionModel a6 = new OptionModel(value, label, displayOrder);
            this.statusesMap.computeIfAbsent(fieldName, k -> new ArrayList<>()).add(a6);
        }
    }
}
