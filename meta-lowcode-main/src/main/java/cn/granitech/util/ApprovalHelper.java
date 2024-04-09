package cn.granitech.util;

import cn.granitech.business.service.ApprovalService;
import cn.granitech.business.service.CrudService;
import cn.granitech.business.service.NotificationService;
import cn.granitech.business.service.approval.ApprovalStrategy;
import cn.granitech.business.service.approval.ApproveApprovalStrategy;
import cn.granitech.business.service.approval.SendApprovalStrategy;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.web.constant.ApprovalConstant;
import cn.granitech.web.enumration.MessageEnum;
import cn.granitech.web.pojo.approval.ApprovalTask;
import cn.granitech.web.pojo.approval.dto.ApprovalProcessDTO;
import cn.granitech.web.pojo.approval.node.ChildNode;
import cn.granitech.web.pojo.approval.node.TaskChildNode;
import cn.granitech.web.pojo.filter.FilterItem;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ApprovalHelper {
    private static final Map<Integer, ApprovalStrategy> strategyMap = new HashMap<>();

    static {
        strategyMap.put(1, new ApproveApprovalStrategy());
        strategyMap.put(2, new SendApprovalStrategy());
    }

    private ApprovalHelper() {
    }

    public static void doStrategy(ApprovalTask approvalTask, List<TaskChildNode> taskChildNodeList, ApprovalProcessDTO approvalProcessDTO) {
        CrudService crudService = SpringHelper.getBean(CrudService.class);
        ApprovalService approvalService = SpringHelper.getBean(ApprovalService.class);
        boolean isSkipApprove = false;
        while (approvalTask.getCurrentNode().intValue() < taskChildNodeList.size() && !isSkipApprove) {
            int nextNode = approvalTask.getCurrentNode().intValue() + 1;
            if (nextNode == taskChildNodeList.size()) {
                handleLastNode(approvalTask, taskChildNodeList, approvalProcessDTO);
                return;
            }
            TaskChildNode nextTaskNode = taskChildNodeList.get(nextNode);
            if (nextTaskNode.getType() == 1) {
                if (approvalService.getApproverUserIdsByNodeType(nextTaskNode.getNodeRoleType(), nextTaskNode.getNodeRoleList(), crudService.queryById(approvalTask.getEntityId()), nextTaskNode.getDeptLevel()).isEmpty()) {
                    handleEmptyApprover(approvalTask, nextNode, approvalService);
                    nextNode++;
                    if (nextNode == taskChildNodeList.size()) {
                        approvalProcessDTO.setRemark(ApprovalConstant.APPROVAL_NO_USER);
                        handleLastNode(approvalTask, taskChildNodeList, approvalProcessDTO);
                        return;
                    }
                    nextTaskNode = taskChildNodeList.get(nextNode);
                }
            }
            executeApprovalStrategy(approvalTask, nextTaskNode, approvalProcessDTO);
            if (nextTaskNode.getType() == 1) {
                isSkipApprove = true;
            }
            approvalTask.setCurrentNode(Integer.valueOf(nextNode));
        }
    }

    private static void handleLastNode(ApprovalTask approvalTask, List<TaskChildNode> taskChildNodeList, ApprovalProcessDTO approvalProcessDTO) {
        if (approvalTask.getCurrentNode().intValue() == 0) {
            executeApprovalStrategy(approvalTask, taskChildNodeList.get(approvalTask.getCurrentNode().intValue()), approvalProcessDTO);
        }
        approvalTask.setStepName(ApprovalConstant.APPROVAL_STR_END);
        approvalTask.setCurrentNode(-1);
        approvalTask.setApprover(new ID[0]);
        approvalTask.setApprovalStatus(3);
        SpringHelper.getBean(NotificationService.class).addApprovalNotification(approvalTask.getEntityId(), MessageEnum.APPROVAL_MSG_COMPLETED, approvalTask.getCreatedBy());
    }

    private static void handleEmptyApprover(ApprovalTask approvalTask, int nextNode, ApprovalService approvalService) {
        approvalTask.setSignType(1);
        approvalTask.setStepName(ApprovalConstant.APPROVAL_NO_USER);
        approvalTask.setApprover(new ID[0]);
        approvalTask.setCurrentNode(Integer.valueOf(nextNode));
        approvalService.saveApprovalHistory(approvalTask, ApprovalConstant.APPROVAL_NO_USER, 1, false, null, null);
    }

    private static void executeApprovalStrategy(ApprovalTask approvalTask, TaskChildNode taskNode, ApprovalProcessDTO approvalProcessDTO) {
        ApprovalStrategy strategy = strategyMap.get(Integer.valueOf(taskNode.getType()));
        if (strategy == null) {
            throw new IllegalArgumentException("Invalid task child node type.");
        }
        strategy.execute(approvalTask, taskNode, approvalProcessDTO);
    }

    public static ChildNode getChildNode(ChildNode childNode, int level) {
        return level == 0 ? childNode : getChildNode(childNode.getChildNode(), level - 1);
    }

    public static String getFilterByStatus(ID entityId, int approvalStatus) {
        return String.format(" [entityId] = '%s'  AND [approvalStatus]=%d ", entityId, Integer.valueOf(approvalStatus));
    }

    public static FilterItem getFieldNameByType(int type, String callerId) {
        if (StrUtil.isBlank(callerId)) {
            throw new IllegalArgumentException("用户信息不能为空！");
        }
        switch (type) {
            case 2:
                return new FilterItem("createdBy", "EQ", callerId);
            case 3:
                return new FilterItem("ccTo", "REF", "approvalTaskId", callerId);
            default:
                return new FilterItem("approver", "REF", "approvalTaskId", callerId);
        }
    }
}
