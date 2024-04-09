package cn.granitech.trigger.business.trigger.action;

import cn.granitech.business.service.ShareAccessService;
import cn.granitech.trigger.business.trigger.BaseTrigger;
import cn.granitech.util.JsonHelper;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.web.pojo.approval.node.NodeRole;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class DataShareTrigger
        implements BaseTrigger {
    @Resource
    ShareAccessService shareAccessService;

    public boolean trigger(ID entityId, String actionContent) {
        DataShare dataShare =  JsonHelper.readJsonValue(actionContent, DataShare.class);

        List<String> shareRecordsId = Collections.singletonList(entityId.getId());
        assert dataShare != null;
        List<String> toUsersList = dataShare.getToUsersId().stream().map(NodeRole::getId).map(ID::getId).collect(Collectors.toList());
        int successCount = this.shareAccessService.shareRecord(shareRecordsId, toUsersList, dataShare.getItems(), dataShare.isWithUpdate());
        return (successCount > 0);
    }
}



