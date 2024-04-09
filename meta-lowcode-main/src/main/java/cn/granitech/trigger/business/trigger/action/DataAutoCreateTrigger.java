package cn.granitech.trigger.business.trigger.action;

import cn.granitech.business.service.CrudService;
import cn.granitech.trigger.business.trigger.BaseTrigger;
import cn.granitech.util.EntityHelper;
import cn.granitech.util.JsonHelper;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.pojo.Entity;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class DataAutoCreateTrigger
        implements BaseTrigger {
    @Resource
    CrudService crudService;

    public boolean trigger(ID entityId, String actionContent) {
        DataOperate dataCreate = JsonHelper.readJsonValue(actionContent, DataOperate.class);
        assert dataCreate != null;
        Entity entity = EntityHelper.getEntity(dataCreate.getEntityName());

        List<DataOperateItem> items = dataCreate.getItems();
        Map<String, List<DataOperateItem>> itemMap = items.stream().collect(Collectors.groupingBy(DataOperateItem::getUpdateMode));


        List<String> queryFieldList = DataOperateUtil.prepareQueryFields(dataCreate, itemMap);
        Map<String, Object> entityMap = this.crudService.queryMapById(entityId, queryFieldList.toArray(new String[0]));
        Map<String, Object> updateFieldMap = DataOperateUtil.prepareUpdateFieldMap(entity, entityMap, itemMap);

        EntityRecord entityRecord = this.crudService.newRecord(entity.getEntityCode());
        EntityHelper.formatFieldValue(entityRecord, updateFieldMap);
        ID refId = this.crudService.create(entityRecord);
        EntityRecord sourceRecord = this.crudService.queryById(entityId);
        sourceRecord.setFieldValue(dataCreate.getFieldName(), refId);

        this.crudService.updateRecord(sourceRecord);
        return true;
    }
}



