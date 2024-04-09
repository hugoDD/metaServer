package cn.granitech.trigger.business.trigger.action;

import cn.granitech.business.service.ShareAccessService;
import cn.granitech.trigger.business.trigger.BaseTrigger;
import cn.granitech.util.JsonHelper;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.web.pojo.approval.node.NodeRole;
import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class DataUnShareTrigger
        implements BaseTrigger {
    @Resource
    ShareAccessService shareAccessService;

    public boolean trigger(ID entityId, String actionContent) {
        DataShare dataShare = JsonHelper.readJsonValue(actionContent, DataShare.class);

        assert dataShare != null;
        int userType = CollUtil.isEmpty(dataShare.toUsersId) ? 1 : 2;
        List<String> shareRecordsId = Collections.singletonList(entityId.getId());
        List<String> toUsersList = dataShare.getToUsersId().stream().map(NodeRole::getId).map(ID::getId).collect(Collectors.toList());
        int successCount = this.shareAccessService.cancelShareRecord(userType, shareRecordsId, toUsersList);
        return (successCount > 0);
    }
}



