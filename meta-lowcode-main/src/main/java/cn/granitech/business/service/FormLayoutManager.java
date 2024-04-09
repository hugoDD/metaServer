package cn.granitech.business.service;

import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.web.pojo.FormLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FormLayoutManager extends BaseService {
    private static final String EntityCode = "entityCode";
    private static final String LayoutJson = "layoutJson";
    private static final String LayoutName = "layoutName";
    @Autowired
    CrudService crudService;
    @Autowired
    EntityManagerService entityManagerService;
    @Autowired
    PersistenceManager persistenceManager;

    @Transactional
    public ID saveLayout(String entityName, String layoutJson) {
        EntityRecord record = newRecord("FormLayout");
        int entityCode = this.entityManagerService.getMetadataManager().getEntity(entityName).getEntityCode().intValue();
        record.setFieldValue(LayoutName, "默认表单布局");
        record.setFieldValue("entityCode", Integer.valueOf(entityCode));
        record.setFieldValue(LayoutJson, layoutJson);
        return createRecord(record);
    }

    @Transactional
    public ID updateLayout(String layoutId, String layoutJson) {
        EntityRecord record = queryRecordById(ID.valueOf(layoutId));
        record.setFieldValue(LayoutJson, layoutJson);
        updateRecord(record);
        return record.id();
    }

    @Transactional
    public FormLayout getLayout(String entityName) {
        return this.crudService.getFormLayout(entityName);
    }

    @Transactional
    public Boolean deleteEntityLayout(int entityCode) {
        this.persistenceManager.batchDelete("FormLayout", String.format("entityCode=%d", Integer.valueOf(entityCode)));
        return true;
    }
}
