package cn.granitech.trigger.business.trigger.action;

import cn.granitech.business.service.ApprovalService;
import cn.granitech.business.service.CrudService;
import cn.granitech.interceptor.CallerContext;
import cn.granitech.trigger.business.trigger.BaseTrigger;
import cn.granitech.util.JsonHelper;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.web.pojo.Cascade;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;


@Component
public class AutoRevokeTrigger
        implements BaseTrigger {
    @Resource
    ApprovalService approvalService;
    @Resource
    CrudService crudService;
    @Resource
    CallerContext callerContext;
    @Resource
    PersistenceManager pm;

    public boolean trigger(ID entityId, String actionContent) {
        this.callerContext.setCallerId("0000021-00000000000000000000000000000001");
        AutoRevoke autoRevoke = JsonHelper.readJsonValue(actionContent, AutoRevoke.class);
        List<Cascade> items = autoRevoke.getItems();
        for (Cascade item : items) {
            if (StringUtils.isBlank(item.getFieldName())) {

                this.approvalService.approvalRevocation(entityId);
                continue;
            }
            if (!item.isReferenced()) {

                EntityRecord entityRecord = this.crudService.queryById(entityId, item.getFieldName());
                ID recordId = entityRecord.getFieldValue(item.getFieldName());
                this.approvalService.approvalRevocation(recordId);
                continue;
            }
            Entity entity = this.pm.getMetadataManager().getEntity(item.getEntityName());
            String filter = String.format("%s = '%s'", item.getFieldName(), entityId);
            List<EntityRecord> entityRecords = this.crudService.queryListRecord(item.getEntityName(), filter, null, null, null, entity.getIdField().getName());
            for (EntityRecord entityRecord : entityRecords) {
                ID recordId = entityRecord.getFieldValue(entity.getIdField().getName());
                this.approvalService.approvalRevocation(recordId);
            }
        }

        return true;
    }
}



