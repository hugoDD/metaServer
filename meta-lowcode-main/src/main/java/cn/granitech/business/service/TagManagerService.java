package cn.granitech.business.service;

import cn.granitech.variantorm.metadata.MetadataManager;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.pojo.TagModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TagManagerService {
    private static final String displayOrderFld = "displayOrder";
    private static final String entityNameFld = "entityName";
    private static final String fieldNameFld = "fieldName";
    private static final String valueFld = "value";
    @Autowired
    PersistenceManager persistenceManager;

    public MetadataManager getMetadataManager() {
        return this.persistenceManager.getMetadataManager();
    }

    @Transactional
    public boolean saveTagList(String entityName, String fieldName, List<String> tagList) {
        this.persistenceManager.batchDelete("TagItem", String.format(" (entityName = '%s') AND (fieldName = '%s') ", entityName, fieldName));
        int displayOrder = 1;
        for (String tagText : tagList) {
            EntityRecord tagRecord = this.persistenceManager.newRecord("TagItem");
            tagRecord.setFieldValue(entityNameFld, entityName);
            tagRecord.setFieldValue(fieldNameFld, fieldName);
            tagRecord.setFieldValue(valueFld, tagText);
            tagRecord.setFieldValue(displayOrderFld, displayOrder);
            this.persistenceManager.insert(tagRecord);
            displayOrder++;
        }
        this.persistenceManager.getTagCacheManager().reloadTagsByField(entityName, fieldName);
        return true;
    }

    public List<TagModel> getTagList(String entityName, String fieldName) {
        return this.persistenceManager.getTagCacheManager().getTags(entityName, fieldName);
    }
}
