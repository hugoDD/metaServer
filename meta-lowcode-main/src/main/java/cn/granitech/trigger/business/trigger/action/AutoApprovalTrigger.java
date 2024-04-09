package cn.granitech.trigger.business.trigger.action;

import cn.granitech.business.service.ApprovalService;
import cn.granitech.business.service.CrudService;
import cn.granitech.business.service.NotificationService;
import cn.granitech.interceptor.CallerContext;
import cn.granitech.trigger.business.trigger.BaseTrigger;
import cn.granitech.util.AssertHelper;
import cn.granitech.util.JsonHelper;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.web.enumration.MessageEnum;
import cn.granitech.web.pojo.approval.ApprovalTask;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;


@Component
public class AutoApprovalTrigger
        implements BaseTrigger {
    private final Logger log = LoggerFactory.getLogger(getClass());


    @Resource
    ApprovalService approvalService;


    @Resource
    CrudService crudService;


    @Resource
    CallerContext callerContext;

    @Resource
    NotificationService notificationService;


    public boolean trigger(ID entityId, String actionContent) {
        if (this.callerContext.getCallerId() == null) {
            this.callerContext.setCallerId("0000021-00000000000000000000000000000001");
        }
        EntityRecord entityRecord = this.crudService.queryById(entityId);
        Integer approvalStatus = entityRecord.getFieldValue("approvalStatus");
        if (approvalStatus != null) {
            if (1 == approvalStatus || 3 == approvalStatus) {
                this.log.info("实体：{}，忽略审批", entityId.getId());
                return false;
            }
        }
        Map<String, Object> map = JsonHelper.writeObjectAsMap(actionContent);

        boolean submitMode = map.get("submitMode") != null && (Boolean) map.get("submitMode");

        String approvalConfigId = (String) map.get("approvalConfigId");
        AssertHelper.isNotNull(approvalConfigId, "流程配置不能为空");

        this.approvalService.startApproval(ID.valueOf(approvalConfigId), entityId);

        if (!submitMode) {
            autoApprovalSuccess(entityId, approvalConfigId);
        }
        return true;
    }


    private void autoApprovalSuccess(ID entityId, String approvalConfigId) {
        if (StrUtil.isBlank(approvalConfigId)) {
            return;
        }
        ID configId = ID.valueOf(approvalConfigId);
        ApprovalTask approvalTask = getApprovalTask(entityId, configId);
        approvalTask.setCurrentNode(Integer.valueOf(0));

        this.approvalService.saveApprovalHistory(approvalTask, "系统自动审批", Integer.valueOf(1), false, null, null);

        this.approvalService.updateEntityApprovalInfo(entityId, configId, Integer.valueOf(3), "系统自动审批");

        this.notificationService.addApprovalNotification(approvalTask.getEntityId(), MessageEnum.APPROVAL_MSG_COMPLETED);
    }


    private ApprovalTask getApprovalTask(ID entityId, ID configId) {
        ApprovalTask approvalTask = new ApprovalTask();
        approvalTask.setApprovalFlowId(null);
        approvalTask.setEntityId(entityId);
        approvalTask.setApprovalConfigId(configId);
        approvalTask.setStepName("系统自动审批");
        approvalTask.setCurrentNode(Integer.valueOf(-1));
        approvalTask.setApprover(null);
        approvalTask.setApprovalUser(ID.valueOf(this.callerContext.getCallerId()));
        approvalTask.setApprovalOn(new Date());
        approvalTask.setSignType(Integer.valueOf(1));
        approvalTask.setAttrMore("[{\"type\":1,\"nodeName\":\"系统自动审批\",\"nodeRoleList\":[],\"nodeRoleType\":1,\"multiPersonApproval\":1}]");
        approvalTask.setApprovalStatus(Integer.valueOf(3));
        approvalTask.setApprovalUser(ID.valueOf(this.callerContext.getCallerId()));
        approvalTask.setApprovalTaskId(this.crudService.saveOrUpdateRecord("ApprovalTask", null, approvalTask));
        return approvalTask;
    }
}



