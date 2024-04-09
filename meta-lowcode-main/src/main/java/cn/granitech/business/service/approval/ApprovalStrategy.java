package cn.granitech.business.service.approval;

import cn.granitech.web.pojo.approval.ApprovalTask;
import cn.granitech.web.pojo.approval.dto.ApprovalProcessDTO;
import cn.granitech.web.pojo.approval.node.TaskChildNode;

public interface ApprovalStrategy {
    void execute(ApprovalTask approvalTask, TaskChildNode taskChildNode, ApprovalProcessDTO approvalProcessDTO);
}
