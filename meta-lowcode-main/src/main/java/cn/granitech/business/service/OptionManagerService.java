package cn.granitech.business.service;

import cn.granitech.variantorm.metadata.MetadataManager;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.pojo.OptionModel;
import cn.granitech.variantorm.pojo.Pagination;
import cn.granitech.web.pojo.KeyValueEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OptionManagerService extends BaseService {
    private static final String displayOrderFld = "displayOrder";
    private static final String entityNameFld = "entityName";
    private static final String fieldNameFld = "fieldName";
    private static final String labelFld = "label";
    private static final String valueFld = "value";
    @Autowired
    PersistenceManager persistenceManager;

    public MetadataManager getMetadataManager() {
        return this.persistenceManager.getMetadataManager();
    }

    @Transactional
    public boolean saveOptionList(String entityName, String fieldName, List<KeyValueEntry<String, Integer>> optionList) {
        this.persistenceManager.batchDelete("OptionItem", String.format(" (entityName = '%s') AND (fieldName = '%s') ", entityName, fieldName));
        int displayOrder = 1;
        for (KeyValueEntry<String, Integer> optionKv : optionList) {
            EntityRecord optionRecord = this.persistenceManager.newRecord("OptionItem");
            optionRecord.setFieldValue(entityNameFld, entityName);
            optionRecord.setFieldValue(fieldNameFld, fieldName);
            optionRecord.setFieldValue(labelFld, optionKv.getKey());
            optionRecord.setFieldValue(valueFld, optionKv.getValue());
            optionRecord.setFieldValue(displayOrderFld, Integer.valueOf(displayOrder));
            this.persistenceManager.insert(optionRecord);
            displayOrder++;
        }
        reloadOptionsByField(entityName, fieldName);
        return true;
    }

    public void reloadOptionsByField(String entityName, String fieldName) {
        this.persistenceManager.getOptionCacheManager().reloadOptionsByField(entityName, fieldName);
    }

    public boolean optionCanBeDeleted(String entityName, String fieldName, String value) {
        List<EntityRecord> entityRecords = super.queryListRecord(entityName, String.format("%s = %s", fieldName, value), null, null, new Pagination(1, 1), fieldName);
        return entityRecords == null || entityRecords.size() == 0;
    }

    @Transactional
    public List<OptionModel> getOptionList(String entityName, String fieldName) {
        return this.persistenceManager.getOptionCacheManager().getOptions(entityName, fieldName);
    }

    @Transactional
    public List<OptionModel> getStatusList(String statusFieldName) {
        return this.persistenceManager.getOptionCacheManager().getStatuses(statusFieldName);
    }
}
