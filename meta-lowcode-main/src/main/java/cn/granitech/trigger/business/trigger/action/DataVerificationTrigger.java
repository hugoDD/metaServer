package cn.granitech.trigger.business.trigger.action;

import cn.granitech.business.service.CrudService;
import cn.granitech.trigger.business.trigger.BaseTrigger;
import cn.granitech.trigger.exception.TriggerServiceException;
import cn.granitech.util.FilterHelper;
import cn.granitech.util.JsonHelper;
import cn.granitech.util.RegexHelper;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.pojo.Entity;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;


@Component
public class DataVerificationTrigger
        implements BaseTrigger {
    @Resource
    CrudService crudService;
    @Resource
    PersistenceManager pm;

    public boolean trigger(ID entityId, String actionContent) {
        DataVerification dataVerification = JsonHelper.readJsonValue(actionContent, DataVerification.class);

        Entity entity = this.pm.getMetadataManager().getEntity(entityId.getEntityCode());
        assert dataVerification != null;
        String filter = String.format("%s AND %s = '%s'", FilterHelper.toFilter(dataVerification.getFilter()), entity.getIdField().getName(), entityId.getId());
        List<EntityRecord> entityRecords = this.crudService.queryListRecord(entity.getName(), filter, null, null, null, entity.getIdField().getName());
        if (entityRecords.size() > 0) {

            String content = dataVerification.getTipContent();
            Set<String> variables = RegexHelper.getVariableSet(content, "\\{([a-zA-Z0-9$\\.]+)}");
            EntityRecord entityRecord = this.crudService.queryById(entityId, variables.toArray(new String[0]));
            for (String variable : variables) {
                content = content.replace(String.format("{%s}", variable), entityRecord.getFieldValue(variable));
            }
            throw new TriggerServiceException(content);
        }
        return true;
    }
}



