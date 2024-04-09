package cn.granitech.web.controller;

import cn.granitech.aop.annotation.SystemRight;
import cn.granitech.business.service.ApprovalService;
import cn.granitech.util.ResponseHelper;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.pojo.Pagination;
import cn.granitech.variantorm.pojo.QuerySchema;
import cn.granitech.web.enumration.SystemRightEnum;
import cn.granitech.web.pojo.ListQueryRequestBody;
import cn.granitech.web.pojo.ListQueryResult;
import cn.granitech.web.pojo.ResponseBean;
import cn.granitech.web.pojo.approval.dto.ApprovalProcessDTO;
import cn.granitech.web.pojo.approval.dto.ApprovalTaskOperationDTO;
import cn.granitech.web.pojo.approval.node.FlowRootNode;
import cn.granitech.web.pojo.approval.node.TaskChildNode;
import cn.granitech.web.pojo.approval.vo.ApprovalConfigEntityVO;
import cn.granitech.web.pojo.approval.vo.ApprovalFlowNodeVO;
import cn.granitech.web.pojo.approval.vo.ApprovalStepsVO;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RequestMapping({"/approval"})
@RestController
public class ApprovalController {
    @Resource
    ApprovalService approvalService;

    @RequestMapping({"/getEntityApprovalTaskList"})
    public ResponseBean<List<Map<String, Object>>> getEntityApprovalTaskList(@RequestParam(name = "type") int type) {
        return ResponseHelper.ok(this.approvalService.getEntityApprovalTaskList(type));
    }

    @RequestMapping({"/getEntityApprovalConfigList"})
    public ResponseBean<List<Map<String, Object>>> getEntityApprovalConfigList(@RequestParam(name = "recordId") ID recordId) {
        return ResponseHelper.ok(this.approvalService.getEntityApprovalConfigList(recordId));
    }

    @RequestMapping({"/recordApprovalState"})
    public ResponseBean recordApprovalState(@RequestParam(name = "recordId") ID recordId) {
        return ResponseHelper.ok(this.approvalService.recordApprovalState(recordId));
    }

    @RequestMapping({"/configList"})
    @SystemRight(SystemRightEnum.APPROVAL_MANAGE)
    public ResponseBean<ListQueryResult> configList(@RequestBody ListQueryRequestBody requestBody) {
        return listQuery(requestBody);
    }

    @RequestMapping({"/listQuery"})
    public ResponseBean<ListQueryResult> listQuery(@RequestBody ListQueryRequestBody requestBody) {
        QuerySchema querySchema = requestBody.querySchema();
        Pagination pagination = requestBody.pagination();
        ListQueryResult queryResult = new ListQueryResult();
        queryResult.setDataList(this.approvalService.queryListMap(querySchema, pagination));
        queryResult.setPagination(pagination);
        return new ResponseBean<>(200, null, "success", queryResult);
    }

    @PostMapping({"/approvalRevocation"})
    @SystemRight(SystemRightEnum.APPROVAL_REVOCATION_MANAGE)
    public ResponseBean<List<ApprovalStepsVO>> approvalRevocation(@RequestParam("entityId") ID entityId) {
        this.approvalService.approvalRevocation(entityId);
        return ResponseHelper.ok();
    }

    @GetMapping({"/getTaskDetailsById"})
    public ResponseBean<List<ApprovalStepsVO>> getTaskDetailsById(@RequestParam("entityId") ID entityId) {
        return ResponseHelper.ok(this.approvalService.getTaskDetailsById(entityId));
    }

    @PostMapping({"/taskOperation"})
    public ResponseBean<T> taskOperation(@RequestBody ApprovalTaskOperationDTO approvalTaskOperationDTO) {
        this.approvalService.taskOperation(approvalTaskOperationDTO);
        return ResponseHelper.ok();
    }

    @GetMapping({"/getApprovalTaskById"})
    public ResponseBean<TaskChildNode> getApprovalTaskById(@RequestParam("approvalTaskId") ID approvalTaskId) {
        return ResponseHelper.ok(this.approvalService.getApprovalTaskById(approvalTaskId));
    }

    @GetMapping({"/getAllApprovalConfigEntity"})
    public ResponseBean<List<ApprovalConfigEntityVO>> getAllApprovalConfigEntity() {
        return ResponseHelper.ok(this.approvalService.getAllApprovalConfigEntity());
    }

    @GetMapping({"/getFlowDefinitionByConfigId"})
    public ResponseBean<ApprovalFlowNodeVO> getFlowDefinitionByConfigId(@RequestParam("approvalConfigId") String approvalConfigId) {
        return ResponseHelper.ok(this.approvalService.getFlowDefinitionByConfigId(approvalConfigId));
    }

    @PostMapping({"/saveLastApprovalFlow"})
    @SystemRight(SystemRightEnum.APPROVAL_MANAGE)
    public ResponseBean<T> saveLastApprovalFlow(@RequestBody FlowRootNode flowRootNode) {
        this.approvalService.saveLastApprovalFlow(flowRootNode);
        return ResponseHelper.ok();
    }

    @GetMapping({"/startApproval"})
    public ResponseBean<T> startApproval(@RequestParam(required = false, value = "approvalConfigId") ID approvalConfigId, @RequestParam("entityId") ID entityId) {
        this.approvalService.startApproval(approvalConfigId, entityId);
        return ResponseHelper.ok();
    }

    @PostMapping({"/approvalProcess"})
    public ResponseBean<T> approvalProcess(@RequestBody ApprovalProcessDTO approvalProcessDTO) {
        this.approvalService.approvalProcess(approvalProcessDTO);
        return ResponseHelper.ok();
    }

    @GetMapping({"/currentShowAbleFields"})
    public ResponseBean<List<Map<String, Object>>> currentShowAbleFields(@RequestParam("entityId") ID entityId) {
        return ResponseHelper.ok(this.approvalService.currentShowAbleFields(entityId));
    }
}
