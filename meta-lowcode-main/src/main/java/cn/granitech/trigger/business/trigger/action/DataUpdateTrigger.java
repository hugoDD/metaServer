package cn.granitech.trigger.business.trigger.action;

import cn.granitech.business.service.CrudService;
import cn.granitech.trigger.business.trigger.BaseTrigger;
import cn.granitech.util.EntityHelper;
import cn.granitech.util.JsonHelper;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.variantorm.pojo.IDName;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class DataUpdateTrigger
        implements BaseTrigger {
    @Resource
    CrudService crudService;

    public boolean trigger(ID entityId, String actionContent) {
        DataOperate dataUpdate = JsonHelper.readJsonValue(actionContent, DataOperate.class);
        assert dataUpdate != null;
        Entity entity = EntityHelper.getEntity(dataUpdate.getEntityName());

        List<DataOperateItem> items = dataUpdate.getItems();
        Map<String, List<DataOperateItem>> itemMap = items.stream().collect(Collectors.groupingBy(DataOperateItem::getUpdateMode));


        List<String> queryFieldList = DataOperateUtil.prepareQueryFields(dataUpdate, itemMap);

        Map<String, Object> entityMap = this.crudService.queryMapById(entityId, queryFieldList.toArray(new String[0]));
        Map<String, Object> updateFieldMap = DataOperateUtil.prepareUpdateFieldMap(entity, entityMap, itemMap);

        if (dataUpdate.isReferenced()) {
            String filter = String.format(" %s = '%s'", dataUpdate.getFieldName(), entityId);
            List<EntityRecord> entityRecords = this.crudService.queryListRecord(entity.getName(), filter, null, null, null, entity.getIdField().getName(), dataUpdate.getFieldName());
            for (EntityRecord entityRecord : entityRecords) {
                ID recordId = entityRecord.getFieldValue(entity.getIdField().getName());
                this.crudService.saveRecord(dataUpdate.getEntityName(), recordId.getId(), updateFieldMap);
            }
        } else {
            String targetEntityId = entityId.getId();
            if (StringUtils.isNotBlank(dataUpdate.getFieldName())) {
                targetEntityId = ((IDName) entityMap.get(dataUpdate.getFieldName())).getId().toString();
            }
            this.crudService.saveRecord(dataUpdate.getEntityName(), targetEntityId, updateFieldMap);
        }
        return true;
    }
}



