package cn.granitech.trigger.business.trigger.action;

import cn.granitech.business.service.CrudService;
import cn.granitech.trigger.business.service.TriggerServiceImpl;
import cn.granitech.trigger.business.trigger.BaseTrigger;
import cn.granitech.util.JsonHelper;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.web.pojo.Cascade;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;


@Component
public class DataDeleteTrigger
        implements BaseTrigger {
    private final String deletedWith = "触发器删除";
    @Resource
    CrudService crudService;
    @Resource
    TriggerServiceImpl triggerServiceImpl;

    public boolean trigger(ID entityId, String actionContent) {
        DataDelete dataDelete = JsonHelper.readJsonValue(actionContent, DataDelete.class);


        List<String> deleteRecordIdList = this.triggerServiceImpl.getDeleteRecordIdList();

        assert dataDelete != null;
        List<Cascade> items = dataDelete.getItems();
        List<EntityRecord> entityRecords = this.crudService.queryCascadeRecordList(entityId, items);
        for (EntityRecord entityRecord : entityRecords) {
            ID recordId = entityRecord.id();
            if (!deleteRecordIdList.contains(recordId.getId())) {
                this.crudService.delete(recordId, deletedWith);
            }
        }
        return true;
    }
}



