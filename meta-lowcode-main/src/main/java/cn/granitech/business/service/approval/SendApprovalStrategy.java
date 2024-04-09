package cn.granitech.business.service.approval;

import cn.granitech.business.service.ApprovalService;
import cn.granitech.business.service.NotificationService;
import cn.granitech.util.SpringHelper;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.web.enumration.MessageEnum;
import cn.granitech.web.pojo.approval.ApprovalTask;
import cn.granitech.web.pojo.approval.dto.ApprovalProcessDTO;
import cn.granitech.web.pojo.approval.node.TaskChildNode;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Component
public class SendApprovalStrategy implements ApprovalStrategy {
    public SendApprovalStrategy() {
    }

    public void execute(ApprovalTask approvalTask, TaskChildNode taskChildNode, ApprovalProcessDTO approvalProcessDTO) {
        ApprovalService approvalService = SpringHelper.getBean(ApprovalService.class);
        NotificationService notificationService = SpringHelper.getBean(NotificationService.class);
        ID[] ccTo = approvalTask.getCcTo();
        List<ID> ccUserIds = approvalService.getApprovalUser(taskChildNode.getNodeUserList());
        ID[] mergedArray = ccTo != null ? Stream.concat(Arrays.stream(ccTo), ccUserIds.stream()).distinct().toArray(ID[]::new) : ccUserIds.toArray(new ID[0]);
        approvalTask.setCcTo(mergedArray);
        approvalService.saveApprovalHistory(approvalTask, taskChildNode.getNodeName(), 2, approvalProcessDTO.getIsBacked(), null);
        notificationService.addApprovalNotification(approvalTask.getCcTo(), approvalTask.getEntityId(), MessageEnum.APPROVAL_MSG_SEND);
    }
}
