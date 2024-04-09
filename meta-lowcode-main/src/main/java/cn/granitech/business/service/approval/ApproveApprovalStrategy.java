package cn.granitech.business.service.approval;

import cn.granitech.business.service.ApprovalService;
import cn.granitech.business.service.CrudService;
import cn.granitech.business.service.NotificationService;
import cn.granitech.util.SpringHelper;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.web.enumration.MessageEnum;
import cn.granitech.web.pojo.approval.ApprovalTask;
import cn.granitech.web.pojo.approval.dto.ApprovalProcessDTO;
import cn.granitech.web.pojo.approval.node.TaskChildNode;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApproveApprovalStrategy implements ApprovalStrategy {
    public void execute(ApprovalTask approvalTask, TaskChildNode taskChildNode, ApprovalProcessDTO approvalProcessDTO) {
        List<ID> approveUserIds = SpringHelper.getBean(ApprovalService.class).getApproverUserIdsByNodeType(taskChildNode.getNodeRoleType(), taskChildNode.getNodeRoleList(), SpringHelper.getBean(CrudService.class).queryById(approvalTask.getEntityId()), taskChildNode.getDeptLevel());
        ID[] userIds = approveUserIds.toArray(new ID[0]);
        approvalTask.setApprover(userIds);
        approvalTask.setSignType(taskChildNode.getMultiPersonApproval());
        approvalTask.setStepName(taskChildNode.getNodeName());
        SpringHelper.getBean(NotificationService.class).addApprovalNotification(userIds, approvalTask.getEntityId(), MessageEnum.APPROVAL_MSG_REVIEW);
    }
}
