package cn.granitech.business.service;

import cn.granitech.business.plugins.trigger.TriggerService;
import cn.granitech.exception.ServiceException;
import cn.granitech.util.*;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.variantorm.pojo.Pagination;
import cn.granitech.variantorm.pojo.QuerySchema;
import cn.granitech.web.enumration.MessageEnum;
import cn.granitech.web.enumration.TriggerWhenEnum;
import cn.granitech.web.pojo.approval.ApprovalConfig;
import cn.granitech.web.pojo.approval.ApprovalFlow;
import cn.granitech.web.pojo.approval.ApprovalHistory;
import cn.granitech.web.pojo.approval.ApprovalTask;
import cn.granitech.web.pojo.approval.dto.ApprovalProcessDTO;
import cn.granitech.web.pojo.approval.dto.ApprovalTaskOperationDTO;
import cn.granitech.web.pojo.approval.node.*;
import cn.granitech.web.pojo.approval.vo.ApprovalConfigEntityVO;
import cn.granitech.web.pojo.approval.vo.ApprovalFlowNodeVO;
import cn.granitech.web.pojo.approval.vo.ApprovalStepsVO;
import cn.granitech.web.pojo.approval.vo.RecordApprovalState;
import cn.granitech.web.pojo.filter.Filter;
import cn.granitech.web.pojo.filter.FilterItem;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ApprovalService extends BaseService {
    private static final String DATE_DESC_STR = " [createdOn]  DESC ";
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Resource
    EntityManagerService entityManagerService;
    @Resource
    UserService userService;
    @Resource
    RevisionHistoryService revisionHistoryService;
    @Resource
    NotificationService notificationService;
    @Resource
    PluginService pluginService;

    public ApprovalService() {
    }

    public List<Map<String, Object>> getEntityApprovalTaskList(int type) {
        QuerySchema querySchema = new QuerySchema();
        querySchema.setMainEntity("ApprovalTask");
        querySchema.setSelectFields("approvalConfigId.entityCode");
        querySchema.setGroupBy("approvalConfigId.entityCode");
        List<FilterItem> filterItemList = new ArrayList<>();
        filterItemList.add(ApprovalHelper.getFieldNameByType(type, this.callerContext.getCallerId()));
        Filter filter = new Filter("AND", filterItemList);
        querySchema.setFilter(FilterHelper.toFilter(filter));
        List<Map<String, Object>> resultList = new ArrayList<>();
        List<Map<String, Object>> query = this.pm.createDataQuery().query(querySchema, null);

        for (Map<String, Object> stringObjectMap : query) {
            Map<String, Object> map = stringObjectMap;
            int entityCode = (Integer) map.get("approvalConfigId.entityCode");
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("entityCode", entityCode);
            resultMap.put("entityName", EntityHelper.getEntity(entityCode).getName());
            resultList.add(resultMap);
        }

        return resultList;
    }

    public boolean containsApprover(ID recordId) {
        String filter = ApprovalHelper.getFilterByStatus(recordId, 1);
        ApprovalTask approvalTask = super.queryOneCustomPojo(ApprovalTask.class, "ApprovalTask", filter, null, DATE_DESC_STR, new String[0]);
        Set<ID> approveSet = new HashSet(Arrays.asList(approvalTask.getApprover()));
        return approveSet.contains(this.callerContext.getCallerID());
    }

    public List<Map<String, Object>> getEntityApprovalConfigList(ID recordId) {
        String filter = String.format("[entityCode] = '%s' and   isDisabled =0   and approvalFlowId is not null", recordId.getEntityCode());
        List<EntityRecord> flowNameList = this.queryListRecord("ApprovalConfig", filter, null, null, null, "approvalConfigId", "approvalFlowId", "flowName");
        List<Map<String, Object>> resultList = new ArrayList();
        Iterator var5 = flowNameList.iterator();

        while (var5.hasNext()) {
            EntityRecord entityRecord = (EntityRecord) var5.next();
            ID approvalFlowId = entityRecord.getFieldValue("approvalFlowId");
            EntityRecord approvalFlow = this.queryRecordById(approvalFlowId);
            AssertHelper.isNotNull(approvalFlow, "未找到对应的流程定义flowId=" + approvalFlowId);
            FlowRootNode flowRootNode = JsonHelper.readJsonValue(approvalFlow.getFieldValue("flowDefinition"), new TypeReference<FlowRootNode>() {
            });
            EntityRecord findRecord = this.findStartNodeRecord(recordId, flowRootNode);
            if (findRecord != null) {
                resultList.add(entityRecord.getValuesMap());
            }
        }

        return resultList;
    }

    private EntityRecord findStartNodeRecord(ID recordId, FlowRootNode flowRootNode) {
        NodeConfig nodeConfig = flowRootNode.getNodeConfig();
        String nodeFilter = FilterHelper.toFilter(nodeConfig.getFilter());
        String flowFilter = nodeFilter.isEmpty() ? "1=1" : nodeFilter;
        Entity entity = EntityHelper.getEntity(recordId.getEntityCode());
        String entityIdFieldName = entity.getIdField().getName();
        String findRecordFilter = flowFilter + String.format(" AND [%s] = '%s'", entityIdFieldName, recordId);
        EntityRecord findRecord = this.queryOneRecord(entity.getName(), findRecordFilter, null, null, entityIdFieldName);
        return findRecord;
    }

    public RecordApprovalState recordApprovalState(ID recordId) {
        Entity entity = EntityHelper.getEntity(recordId.getEntityCode());
        if (!MetadataHelper.hasApprovalField(entity)) {
            return null;
        } else {
            EntityRecord entityRecord = this.queryRecordById(recordId);
            String filter = String.format(" [entityId] = '%s' ", recordId);
            ApprovalTask approvalTask = super.queryOneCustomPojo(ApprovalTask.class, "ApprovalTask", filter, null, DATE_DESC_STR);
            RecordApprovalState recordState = new RecordApprovalState();
            ID approvalFlowId;
            ApprovalConfig approvalConfig;
            if (approvalTask == null) {
                approvalConfig = this.getLastApprovalConfig(null, recordId);
                approvalFlowId = approvalConfig.getApprovalFlowId();
            } else {
                approvalFlowId = approvalTask.getApprovalFlowId();
                if (approvalFlowId == null) {
                    approvalConfig = this.getLastApprovalConfig(approvalTask.getApprovalConfigId(), recordId);
                    approvalFlowId = approvalConfig.getApprovalFlowId();
                }
            }

            if (approvalFlowId == null) {
                return null;
            } else {
                ApprovalFlow approvalFlow = super.queryOneCustomPojoById(ApprovalFlow.class, approvalFlowId);
                AssertHelper.isNotNull(approvalFlow, "未找到对应的流程定义flowId=" + approvalFlowId);
                FlowRootNode flowRootNode = JsonHelper.readJsonValue(approvalFlow.getFlowDefinition(), new TypeReference<FlowRootNode>() {
                });
                boolean submitApprovalPermissions = this.isSubmitApprovalPermissions(flowRootNode.getNodeConfig().getNodeRoleType(), flowRootNode.getNodeConfig().getNodeRoleList(), entityRecord, 0);
                if (approvalTask == null) {
                    recordState.setApprovalStatus(0);
                    if (submitApprovalPermissions) {
                        recordState.setStartApproval(true);
                    }
                } else {
                    recordState.setApprovalTaskId(approvalTask.getApprovalTaskId());
                    recordState.setApprovalStatus(approvalTask.getApprovalStatus());
                    boolean isRejectedOrRevoked = approvalTask.getApprovalStatus() == 2 || approvalTask.getApprovalStatus() == 4;
                    if (isRejectedOrRevoked) {
                        recordState.setStartApproval(submitApprovalPermissions);
                    } else {
                        ID[] taskApprover = approvalTask.getApprover();
                        if (taskApprover != null) {
                            Set<ID> approveSet = new HashSet<>(Arrays.asList(taskApprover));
                            boolean containsApproval = approveSet.contains(this.callerContext.getCallerID());
                            recordState.setImApproval(containsApproval);
                        }
                    }
                }

                return recordState;
            }
        }
    }

    public void checkApprovalDelRight(EntityRecord entityRecord) {
        if (entityRecord.getEntity().containsField("approvalStatus")) {
            Integer approvalStatus = entityRecord.getFieldValue("approvalStatus");
            if (approvalStatus != null) {
                if (approvalStatus == 1) {
                    this.log.info("审核中记录不能删除,id={}", entityRecord.id());
                    throw new ServiceException("记录正在审批中，禁止删除");
                } else if (approvalStatus == 3) {
                    this.log.info("审核完成记录不能删除,id={}", entityRecord.id());
                    throw new ServiceException("记录已完成审批，禁止删除");
                }
            }
        }
    }

    public void checkFieldsApprovalRight(EntityRecord entityRecord) {
        Map<String, Object> valuesMap = entityRecord.getValuesMap();
        if (entityRecord.getEntity().containsField("approvalStatus")) {
            Integer approvalStatus = entityRecord.getFieldValue("approvalStatus");
            if (approvalStatus != null) {
                if (approvalStatus == 3) {
                    this.log.info("审核完成记录不能编辑,id={}", entityRecord.id());
                    throw new ServiceException("记录已完成审批，禁止编辑");
                } else if (approvalStatus == 1) {
                    List<Map<String, Object>> ableFields = this.currentShowAbleFields(entityRecord.id());
                    Set<String> modifiedFieldSet = entityRecord.getModifiedFieldSet();
                    Set<String> checkAbleFieldSet = new HashSet<>();
                    Iterator<Map<String, Object>> var7 = ableFields.iterator();

                    Map ableField;
                    String name;
                    Boolean isRequired;
                    do {
                        if (!var7.hasNext()) {
                            modifiedFieldSet.retainAll(checkAbleFieldSet);
                            return;
                        }

                        ableField = var7.next();
                        name = (String) ableField.get("name");
                        checkAbleFieldSet.add(name);
                        isRequired = ableField.get("isRequired") != null && (Boolean) ableField.get("isRequired");
                    } while (!isRequired || !valuesMap.containsKey(name) || entityRecord.getFieldValue(name) != null);

                    String label = (String) ableField.get("label");
                    throw new ServiceException(label + "字段不能为空！");
                }
            }
        }
    }

    public List<Map<String, Object>> currentShowAbleFields(ID entityId) {
        String filter = ApprovalHelper.getFilterByStatus(entityId, 1);
        ApprovalTask approvalTask = super.queryOneCustomPojo(ApprovalTask.class, "ApprovalTask", filter, null, DATE_DESC_STR);
        AssertHelper.isNotNull(approvalTask, "未找到对应的流程记录或者流程已撤销");
        Integer currentNode = approvalTask.getCurrentNode();
        List<TaskChildNode> taskChildNodes = JsonHelper.readJsonValue(approvalTask.getAttrMore(), new TypeReference<List<TaskChildNode>>() {
        });
        TaskChildNode taskChildNode = taskChildNodes.get(currentNode);
        return taskChildNode.getModifiableFields();
    }

    public void approvalRevocation(ID entityId) {
        String filter = ApprovalHelper.getFilterByStatus(entityId, 3);
        ApprovalTask approvalTask = super.queryOneCustomPojo(ApprovalTask.class, "ApprovalTask", filter, null, DATE_DESC_STR);
        AssertHelper.isNotNull(approvalTask, "未找到对应的流程记录");
        if (!Objects.equals(this.callerContext.getCallerId(), "0000021-00000000000000000000000000000001")) {
            AssertHelper.isTrue(this.callerContext.getCallerId().equals(approvalTask.getCreatedBy().getId()), "暂无权限撤销流程！");
        }

        approvalTask.setApprovalStatus(4);
        approvalTask.setStepName("流程已撤销");
        approvalTask.setCurrentNode(-1);
        approvalTask.setApprover(new ID[0]);
        approvalTask.setApprovalOn(new Date());
        super.saveOrUpdateRecord("ApprovalTask", approvalTask.getApprovalTaskId(), approvalTask);
        this.updateEntityApprovalInfo(approvalTask.getEntityId(), null, approvalTask.getApprovalStatus(), "撤销");
        this.saveApprovalHistory(approvalTask, approvalTask.getStepName(), 12, true, null);
        this.revisionHistoryService.insertApprovalHistory(entityId, null);
        this.notificationService.addApprovalNotification(approvalTask.getEntityId(), MessageEnum.APPROVAL_MSG_REVOCATION, approvalTask.getCreatedBy());
        TriggerService triggerService = this.pluginService.getTriggerService();
        if (triggerService != null) {
            triggerService.executeTrigger(TriggerWhenEnum.REVOKED, approvalTask.getEntityId(), triggerService.getTriggerLock());
        }

    }

    public List<ApprovalStepsVO> getTaskDetailsById(ID entityId) {
        List<ApprovalStepsVO> approvalStepsVOList = new ArrayList<>();
        String filter = String.format(" [entityId] = '%s' ", entityId);
        ApprovalTask approvalTask = super.queryOneCustomPojo(ApprovalTask.class, "ApprovalTask", filter, null, DATE_DESC_STR);
        AssertHelper.isNotNull(approvalTask, "未找到对应的流程记录");
        this.addStartSteps(approvalStepsVOList, approvalTask);
        List<TaskChildNode> taskChildNodes = JsonHelper.readJsonValue(approvalTask.getAttrMore(), new TypeReference<List<TaskChildNode>>() {
        });
        String hisFilter = String.format(" [approvalTaskId]= '%s'  and [nodeType] != %d ", approvalTask.getApprovalTaskId(), 2);
        List<ApprovalHistory> historyList = super.queryListCustomPojo(ApprovalHistory.class, "ApprovalHistory", hisFilter, null, null, null);
        this.addApprovalHistorySteps(approvalStepsVOList, taskChildNodes, historyList);
        Integer currentNode = approvalTask.getCurrentNode();
        if (1 == approvalTask.getApprovalStatus()) {
            TaskChildNode taskChildNode = taskChildNodes.get(currentNode);
            ID[] taskApproves = approvalTask.getApprover();
            int var12 = taskApproves.length;

            for (ID id : taskApproves) {
                ApprovalStepsVO approvalStepsVO = this.createApprovalStepForOngoing(taskChildNode, approvalTask, id);
                approvalStepsVOList.add(approvalStepsVO);
            }
        } else {
            ApprovalStepsVO endStepsVO = new ApprovalStepsVO();
            endStepsVO.setStepName("流程已结束");
            endStepsVO.setState(13);
            approvalStepsVOList.add(endStepsVO);
        }
        approvalStepsVOList.sort(Comparator.comparing(ApprovalStepsVO::getState).thenComparing(ApprovalStepsVO::getCreatedOn));
//        approvalStepsVOList.sort(Comparator.comparing((vo) -> vo.getState() == null ? 0 : 1)
//                .thenComparing((vo) -> vo.getCreatedOn() == null ? new Date() : vo.getCreatedOn()));
        return approvalStepsVOList;
    }

    public void taskOperation(ApprovalTaskOperationDTO approvalTaskOperationDTO) {
        String filter = ApprovalHelper.getFilterByStatus(approvalTaskOperationDTO.getEntityId(), 1);
        ApprovalTask approvalTask = super.queryOneCustomPojo(ApprovalTask.class, "ApprovalTask", filter, null, null, new String[0]);
        AssertHelper.isNotNull(approvalTask, "未找到对应的流程记录或者流程已结束");
        Set<ID> approveSet = new HashSet(Arrays.asList(approvalTask.getApprover()));
        List<ID> operationUserList = this.getApprovalUser(approvalTaskOperationDTO.getNodeRoleList());
        Iterator var6 = approveSet.iterator();

        ID id;
        do {
            if (!var6.hasNext()) {
                List<TaskChildNode> taskChildNodes = JsonHelper.readJsonValue(approvalTask.getAttrMore(), new TypeReference<List<TaskChildNode>>() {
                });
                TaskChildNode currentChildNode = taskChildNodes.get(approvalTask.getCurrentNode());
                Object signUserList;
                if (approvalTaskOperationDTO.getType() == 1) {
                    approveSet.remove(ID.valueOf(this.callerContext.getCallerId()));
                    signUserList = new ArrayList();
                    if (!CollectionUtils.isEmpty(currentChildNode.getReferralUserList())) {
                        signUserList = currentChildNode.getReferralUserList();
                    }

                    ((List) signUserList).addAll(approvalTaskOperationDTO.getNodeRoleList());
                    currentChildNode.setReferralUserList((List) signUserList);
                } else if (approvalTaskOperationDTO.getType() == 2) {
                    signUserList = new ArrayList();
                    if (!CollectionUtils.isEmpty(currentChildNode.getSignUserList())) {
                        signUserList = currentChildNode.getSignUserList();
                    }

                    ((List) signUserList).addAll(approvalTaskOperationDTO.getNodeRoleList());
                    currentChildNode.setSignUserList((List) signUserList);
                }

                approveSet.addAll(operationUserList);
                approvalTask.setApprover(approveSet.toArray(new ID[0]));
                approvalTask.setAttrMore(JsonHelper.writeObjectAsString(taskChildNodes));
                super.saveOrUpdateRecord("ApprovalTask", approvalTask.getApprovalTaskId(), approvalTask);
                MessageEnum messageEnum = approvalTaskOperationDTO.getType() == 1 ? MessageEnum.APPROVAL_MSG_REFERRAL : MessageEnum.APPROVAL_MSG_SIGN;
                this.notificationService.addApprovalNotification(operationUserList.toArray(new ID[0]), approvalTask.getEntityId(), messageEnum);
                return;
            }

            id = (ID) var6.next();
        } while (!operationUserList.contains(id));

        throw new ServiceException(this.userService.getUserName(id) + "已存在审核人!");
    }

    public TaskChildNode getApprovalTaskById(ID approvalTaskId) {
        ApprovalTask approvalTask;
        if (approvalTaskId.getEntityCode() == 33) {
            approvalTask = super.queryOneCustomPojoById(ApprovalTask.class, approvalTaskId);
        } else {
            String filter = ApprovalHelper.getFilterByStatus(approvalTaskId, 1);
            approvalTask = super.queryOneCustomPojo(ApprovalTask.class, "ApprovalTask", filter, null, null, new String[0]);
        }

        AssertHelper.isNotNull(approvalTask, "未找到流程任务节点!");
        boolean isReviewStatus = 1 == approvalTask.getApprovalStatus();
        AssertHelper.isTrue(isReviewStatus, "流程已经结束!");
        List<TaskChildNode> taskChildNodes = JsonHelper.readJsonValue(approvalTask.getAttrMore(), new TypeReference<List<TaskChildNode>>() {
        });
        TaskChildNode taskChildNode = taskChildNodes.get(approvalTask.getCurrentNode());
        taskChildNode.setEntityCode(approvalTask.getEntityId().getEntityCode());
        return taskChildNode;
    }

    public List<ApprovalConfigEntityVO> getAllApprovalConfigEntity() {
        List<ApprovalConfig> approvalTaskList = super.queryListCustomPojo(ApprovalConfig.class, "ApprovalConfig", null, null, null, null, "entityCode");
        List<Integer> entityCodeList = approvalTaskList.stream().map(ApprovalConfig::getEntityCode).distinct().collect(Collectors.toList());
        List<ApprovalConfigEntityVO> entityVOList = new ArrayList();
        Iterator var4 = entityCodeList.iterator();

        while (var4.hasNext()) {
            Integer entityCode = (Integer) var4.next();
            Entity entity = this.entityManagerService.getMetadataManager().getEntity(entityCode);
            if (entity != null) {
                ApprovalConfigEntityVO approvalConfigEntityVO = new ApprovalConfigEntityVO();
                approvalConfigEntityVO.setEntityCode(entity.getEntityCode());
                approvalConfigEntityVO.setLabel(entity.getLabel());
                approvalConfigEntityVO.setEntityName(entity.getName());
                entityVOList.add(approvalConfigEntityVO);
            }
        }

        return entityVOList;
    }

    @Transactional(
            propagation = Propagation.NESTED
    )
    public ApprovalTask approvalProcess(ApprovalProcessDTO approvalProcessDTO) {
        String filter = ApprovalHelper.getFilterByStatus(ID.valueOf(approvalProcessDTO.getEntityId()), 1);
        ApprovalTask approvalTask = super.queryOneCustomPojo(ApprovalTask.class, "ApprovalTask", filter, null, null, new String[0]);
        AssertHelper.isNotNull(approvalTask, "未找到对应的流程记录或者流程已结束");
        String hisFilter = String.format(" [currentNode] = '%s'  AND [approver]= '%s' AND [approvalTaskId]= '%s'  ", approvalTask.getCurrentNode(), this.callerContext.getCallerId(), approvalTask.getApprovalTaskId());
        ApprovalHistory history = super.queryOneCustomPojo(ApprovalHistory.class, "ApprovalHistory", hisFilter, null, null, new String[0]);
        AssertHelper.isNull(history, "你已经提交过审核了，请勿重复提交！");
        List<TaskChildNode> taskChildNodes = JsonHelper.readJsonValue(approvalTask.getAttrMore(), new TypeReference<List<TaskChildNode>>() {
        });
        this.processApprovalTask(approvalTask, taskChildNodes, approvalProcessDTO, false);
        return approvalTask;
    }

    @Transactional(
            propagation = Propagation.NESTED
    )
    public ApprovalTask startApproval(ID approvalConfigId, ID entityId) {
        this.isExistApproval(entityId);
        ApprovalConfig approvalConfig = this.getLastApprovalConfig(approvalConfigId, entityId);
        AssertHelper.isNotNull(approvalConfig, "未找到对应的流程配置configId=" + approvalConfigId);
        approvalConfigId = approvalConfig.getApprovalConfigId();
        ApprovalFlow approvalFlow = super.queryOneCustomPojoById(ApprovalFlow.class, approvalConfig.getApprovalFlowId());
        AssertHelper.isNotNull(approvalFlow, "未找到对应的流程定义flowId=" + approvalConfig.getApprovalFlowId());
        FlowRootNode flowRootNode = JsonHelper.readJsonValue(approvalFlow.getFlowDefinition(), new TypeReference<FlowRootNode>() {
        });
        EntityRecord entityRecord = this.findStartNodeRecord(entityId, flowRootNode);
        AssertHelper.isNotNull(entityRecord, "未找到符合条件实体记录entityId=" + entityId);
        AssertHelper.isTrue(this.isSubmitApprovalPermissions(flowRootNode.getNodeConfig().getNodeRoleType(), flowRootNode.getNodeConfig().getNodeRoleList(), entityRecord, 0), "您无权提交审核！");
        ApprovalTask approvalTask = new ApprovalTask();
        approvalTask.setApprovalFlowId(approvalFlow.getApprovalFlowId());
        approvalTask.setEntityId(entityId);
        approvalTask.setApprovalConfigId(approvalConfigId);
        approvalTask.setApprovalUser(ID.valueOf(this.callerContext.getCallerId()));
        ChildNode firstNode = ApprovalHelper.getChildNode(flowRootNode.getNodeConfig().getChildNode(), 0);
        if (firstNode.getType() == 4) {
            ConditionNodes eligibleConditionNode = this.findEligibleConditionNode(firstNode.getConditionNodes(), entityRecord);
            firstNode = eligibleConditionNode == null ? null : eligibleConditionNode.getChildNode();
        }

        if (firstNode == null) {
            this.setNoApprovalNodeAttributes(approvalTask);
        } else {
            this.setApprovalNodeAttributes(approvalTask, firstNode, entityRecord, flowRootNode);
        }

        approvalTask.setApprovalOn(new Date());
        approvalTask.setApprovalTaskId(super.saveOrUpdateRecord("ApprovalTask", null, approvalTask));
        this.statisticsConfigStateTotal(approvalConfigId.toString());
        this.updateEntityApprovalInfo(entityId, approvalConfigId, approvalTask.getApprovalStatus(), null);
        this.notificationService.addApprovalNotification(approvalTask.getApprover(), approvalTask.getEntityId(), MessageEnum.APPROVAL_MSG_REVIEW);
        TriggerService triggerService = this.pluginService.getTriggerService();
        if (triggerService != null) {
            triggerService.executeTrigger(TriggerWhenEnum.SUBMIT, approvalTask.getEntityId(), triggerService.getTriggerLock());
        }

        if (approvalTask.getApprover() == null || approvalTask.getApprover().length == 0) {
            List<TaskChildNode> taskChildNodes = JsonHelper.readJsonValue(approvalTask.getAttrMore(), new TypeReference<List<TaskChildNode>>() {
            });
            this.processApprovalTask(approvalTask, taskChildNodes, new ApprovalProcessDTO(entityId.getId(), "暂无审核人,自动审核同意！", false), true);
        }

        return approvalTask;
    }

    private void setNoApprovalNodeAttributes(ApprovalTask approvalTask) {
        approvalTask.setStepName("无审核节点，流程结束！");
        approvalTask.setApprovalStatus(3);
        approvalTask.setSignType(1);
        approvalTask.setCurrentNode(-1);
        approvalTask.setAttrMore("[{\"type\":1,\"nodeName\":\"无审核节点,审核结束\",\"nodeRoleList\":[],\"nodeRoleType\":1,\"multiPersonApproval\":1}]");
    }

    private void setApprovalNodeAttributes(ApprovalTask approvalTask, ChildNode firstNode, EntityRecord entityRecord, FlowRootNode flowRootNode) {
        approvalTask.setStepName(firstNode.getNodeName());
        List<ID> approverUserIds = this.getApproverUserIdsByNodeType(firstNode.getNodeRoleType(), firstNode.getNodeRoleList(), entityRecord, firstNode.getDeptLevel());
        approvalTask.setApprover(approverUserIds.toArray(new ID[approverUserIds.size()]));
        approvalTask.setSignType(firstNode.getMultiPersonApproval());
        List<TaskChildNode> taskChildNodeList = new ArrayList();
        this.addTaskChildNodes(taskChildNodeList, entityRecord, flowRootNode.getNodeConfig().getChildNode());
        approvalTask.setAttrMore(JsonHelper.writeObjectAsString(taskChildNodeList));
        approvalTask.setCurrentNode(0);
        approvalTask.setApprovalStatus(1);
    }

    public List<ID> getApproverUserIdsByNodeType(int nodeRoleType, List<NodeRole> nodeRoleList, EntityRecord entityRecord, int deptLevel) {
        List<ID> approveUserIds = new ArrayList<>();
        switch (nodeRoleType) {
            case 2:
                approveUserIds.add(entityRecord.getFieldValue("createdBy"));
                break;
            case 3:
                approveUserIds.addAll(this.getApprovalUser(nodeRoleList));
                break;
            case 4:
                Iterator var9 = nodeRoleList.iterator();

                while (var9.hasNext()) {
                    NodeRole nodeRole = (NodeRole) var9.next();
                    this.addDeptOwnerUser(22 == nodeRole.getId().getEntityCode(), nodeRole.getId(), approveUserIds);
                }

                return approveUserIds;
            case 5:
                ID departmentOwnerUser = this.initiatorDepartmentByLevel(entityRecord.id(), deptLevel);
                if (departmentOwnerUser != null) {
                    approveUserIds.add(departmentOwnerUser);
                }
                break;
            case 6:
                ID ownerDepartment = entityRecord.getFieldValue("ownerDepartment");
                ID dataOwnerUser = this.findDeptLevelOwnerUser(ownerDepartment, deptLevel);
                if (dataOwnerUser != null) {
                    approveUserIds.add(dataOwnerUser);
                }
        }

        return approveUserIds;
    }

    private ID initiatorDepartmentByLevel(ID recordId, int deptLevel) {
        String filter = ApprovalHelper.getFilterByStatus(recordId, 1);
        EntityRecord taskRecord = this.queryOneRecord("ApprovalTask", filter, null, DATE_DESC_STR);
        ID recordUserId;
        if (taskRecord != null) {
            recordUserId = taskRecord.getFieldValue("createdBy");
        } else {
            recordUserId = this.callerContext.getCallerID();
        }

        return this.userService.getDeptOwnerUserByLevel(recordUserId, deptLevel);
    }

    private ID findDeptLevelOwnerUser(ID ownerDepartment, int deptLevel) {
        return this.userService.getDeptOwnerUserByLevel(ownerDepartment, deptLevel);
    }

    private void addDeptOwnerUser(boolean shouldAddDeptOwnerUser, ID ownerDepartment, List<ID> approveUserIds) {
        if (shouldAddDeptOwnerUser) {
            ID departmentOwnerUser = this.userService.getDepartmentOwnerUser(ownerDepartment);
            if (departmentOwnerUser != null) {
                approveUserIds.add(departmentOwnerUser);
            }
        }

    }

    public ApprovalConfig getLastApprovalConfig(ID approvalConfigId, ID entityId) {
        if (approvalConfigId == null) {
            String filter = String.format("[entityCode] = '%s' AND isDisabled = 0  AND  approvalFlowId is not null ", entityId.getEntityCode());
            return super.queryOneCustomPojo(ApprovalConfig.class, "ApprovalConfig", filter, null, " [modifiedOn] DESC ", new String[0]);
        } else {
            return super.queryOneCustomPojoById(ApprovalConfig.class, approvalConfigId);
        }
    }

    public void updateEntityApprovalInfo(ID entityId, ID approvalConfigId, Integer approvalStatus, String remark) {
        Entity entity = this.pm.getMetadataManager().getEntity(entityId.getEntityCode());
        EntityRecord entityRecord = super.queryRecordById(entityId);
        ID callerId = ID.valueOf(this.callerContext.getCallerId());
        if (approvalConfigId != null && entity.containsField("approvalConfigId")) {
            entityRecord.setFieldValue("approvalConfigId", approvalConfigId);
        }

        if (entity.containsField("approvalStatus")) {
            entityRecord.setFieldValue("approvalStatus", approvalStatus);
        }

        if (entity.containsField("lastApprovedBy")) {
            entityRecord.setFieldValue("lastApprovedBy", callerId);
        }

        if (entity.containsField("lastApprovedOn")) {
            entityRecord.setFieldValue("lastApprovedOn", new Date());
        }

        if (entity.containsField("lastApprovalRemark")) {
            entityRecord.setFieldValue("lastApprovalRemark", remark);
        }

        this.pm.update(entityRecord);
    }

    public ApprovalFlowNodeVO getFlowDefinitionByConfigId(String configId) {
        ApprovalFlowNodeVO approvalFlowNodeVO = new ApprovalFlowNodeVO();
        ApprovalConfig approvalConfig = super.queryOneCustomPojoById(ApprovalConfig.class, ID.valueOf(configId));
        AssertHelper.isNotNull(approvalConfig, "未找到对应的流程配置id=" + configId);
        approvalFlowNodeVO.setFlowName(approvalConfig.getFlowName());
        approvalFlowNodeVO.setApprovalConfigId(approvalConfig.getApprovalConfigId().toString());
        if (approvalConfig.getApprovalFlowId() != null) {
            ApprovalFlow approvalFlow = super.queryOneCustomPojoById(ApprovalFlow.class, approvalConfig.getApprovalFlowId());
            FlowRootNode flowRootNode = JsonHelper.readJsonValue(approvalFlow.getFlowDefinition(), new TypeReference<FlowRootNode>() {
            });
            approvalFlowNodeVO.setNodeConfig(flowRootNode.getNodeConfig());
        }

        return approvalFlowNodeVO;
    }

    @Transactional(
            propagation = Propagation.NESTED
    )
    public void saveLastApprovalFlow(FlowRootNode flowRootNode) {
        String approvalConfigId = flowRootNode.getApprovalConfigId();
        String flowDefinition = JsonHelper.writeObjectAsString(flowRootNode);
        EntityRecord configRecord = super.queryRecordById(ID.valueOf(approvalConfigId));
        AssertHelper.isNotNull(configRecord, "未找到对应的流程配置id=" + approvalConfigId);
        ID oldApprovalFlowId = configRecord.getFieldValue("approvalFlowId");
        if (ObjectUtils.isNotEmpty(oldApprovalFlowId)) {
            EntityRecord flowRecord = super.queryRecordById(oldApprovalFlowId);
            AssertHelper.isNotNull(flowRecord, "未找到对应的流程定义id=" + oldApprovalFlowId);
            String oldFlowDefinition = flowRecord.getFieldValue("flowDefinition");
            if (oldFlowDefinition == null || oldFlowDefinition.equals(flowDefinition)) {
                return;
            }
        }

        String approvalFlowId = this.saveApprovalFlow(approvalConfigId, flowDefinition);
        this.updateApprovalConfig(configRecord, approvalFlowId);
    }

    public List<ID> getApprovalUser(List<NodeRole> nodeRoleList) {
        if (CollectionUtils.isEmpty(nodeRoleList)) {
            return Collections.emptyList();
        } else {
            Set<ID> userIds = new HashSet();
            Iterator var3 = nodeRoleList.iterator();

            while (var3.hasNext()) {
                NodeRole nodeRole = (NodeRole) var3.next();
                int entityCode = nodeRole.getId().getEntityCode();
                if (21 != entityCode) {
                    userIds.addAll(this.userService.getUserListById(nodeRole.getId()));
                } else {
                    userIds.add(nodeRole.getId());
                }
            }

            return new ArrayList(userIds);
        }
    }

    public void addTaskChildNodes(List<TaskChildNode> taskChildNodeList, EntityRecord entityRecord, ChildNode childNode) {
        if (childNode != null) {
            if (childNode.getType() == 4) {
                List<ConditionNodes> conditionNodes = childNode.getConditionNodes();
                ConditionNodes eligibleConditionNode = this.findEligibleConditionNode(conditionNodes, entityRecord);
                if (eligibleConditionNode != null) {
                    this.addTaskChildNodes(taskChildNodeList, entityRecord, eligibleConditionNode.getChildNode());
                } else {
                    this.addTaskChildNodes(taskChildNodeList, entityRecord, conditionNodes.get(conditionNodes.size() - 1).getChildNode());
                }
            } else {
                TaskChildNode taskChildNode = new TaskChildNode();
                BeanUtils.copyProperties(childNode, taskChildNode);
                taskChildNodeList.add(taskChildNode);
            }

            this.addTaskChildNodes(taskChildNodeList, entityRecord, childNode.getChildNode());
        }
    }

    private ConditionNodes findEligibleConditionNode(List<ConditionNodes> conditionNodes, EntityRecord entityRecord) {
        String entityIdFieldName = entityRecord.getEntity().getIdField().getName();
        String entityName = entityRecord.getEntity().getName();
        Iterator var5 = conditionNodes.iterator();

        ConditionNodes conditionNode;
        List resultList;
        do {
            if (!var5.hasNext()) {
                return null;
            }

            conditionNode = (ConditionNodes) var5.next();
            QuerySchema querySchema = new QuerySchema();
            querySchema.setSelectFields(entityIdFieldName);
            querySchema.setMainEntity(entityName);
            String filter = FilterHelper.toFilter(conditionNode.getFilter());
            String filterStr = StrUtil.isNotBlank(filter) ? filter : "1=1";
            String formattedFilter = String.format(" AND [%s] = '%s'", entityIdFieldName, entityRecord.id());
            querySchema.setFilter(filterStr + formattedFilter);
            resultList = super.queryListMap(querySchema, new Pagination(1, 1));
        } while (resultList.isEmpty());

        return conditionNode;
    }

    private void statisticsConfigStateTotal(String approvalConfigId) {
        Map<String, Object> dataMap = new HashMap();
        String filter = "[approvalConfigId] = '%s' AND [approvalStatus]= %d";
        Pagination pagination = new Pagination(1, 1);
        super.queryListRecord("ApprovalTask", String.format(filter, approvalConfigId, 3), null, null, pagination);
        dataMap.put("completeTotal", pagination.getTotal());
        super.queryListRecord("ApprovalTask", String.format(filter, approvalConfigId, 1), null, null, pagination);
        dataMap.put("runningTotal", pagination.getTotal());
        super.saveOrUpdateRecord("ApprovalConfig", ID.valueOf(approvalConfigId), dataMap);
    }

    public void saveApprovalHistory(ApprovalTask approvalTask, String oldStepName, Integer nodeType, boolean isBacked, ID[] currentCCTo) {
        ApprovalHistory approvalHistory = new ApprovalHistory();
        BeanUtils.copyProperties(approvalTask, approvalHistory);
        approvalHistory.setApprover(ID.valueOf(this.callerContext.getCallerId()));
        approvalHistory.setStepName(oldStepName);
        approvalHistory.setNodeType(nodeType);
        approvalHistory.setIsBacked(isBacked);
        approvalHistory.setCurrentCCTo(currentCCTo);
        super.saveOrUpdateRecord("ApprovalHistory", null, approvalHistory);
    }

    public void saveApprovalHistory(ApprovalTask approvalTask, String oldStepName, Integer nodeType, boolean isBacked, ID[] currentCCTo, ID approver) {
        ApprovalHistory approvalHistory = new ApprovalHistory();
        BeanUtils.copyProperties(approvalTask, approvalHistory);
        approvalHistory.setApprover(approver);
        approvalHistory.setStepName(oldStepName);
        approvalHistory.setNodeType(nodeType);
        approvalHistory.setIsBacked(isBacked);
        approvalHistory.setCurrentCCTo(currentCCTo);
        super.saveOrUpdateRecord("ApprovalHistory", null, approvalHistory);
    }

    private void isExistApproval(ID entityId) {
        Map<String, Object> paramMap = new HashMap();
        paramMap.put("entityId", entityId.toString());
        String filter = String.format("[entityId] = :entityId  AND ([approvalStatus]=%d OR [approvalStatus]=%d ) ", 1, 3);
        List<EntityRecord> entityRecordList = super.queryListRecord("ApprovalTask", filter, paramMap, null, null);
        AssertHelper.isEmpty(entityRecordList, "流程已经审核完成或者正在审批中...");
    }

    private boolean isSubmitApprovalPermissions(int nodeRoleType, List<NodeRole> nodeRoleList, EntityRecord entityRecord, int deptLevel) {
        ID callerId = ID.valueOf(this.callerContext.getCallerId());
        ID recordUserId = entityRecord.getFieldValue("ownerUser");
        switch (nodeRoleType) {
            case 1:
                return true;
            case 2:
                return recordUserId != null && callerId.equals(recordUserId);
            case 3:
                return recordUserId != null && this.getApprovalUser(nodeRoleList).contains(callerId);
            case 4:
                Iterator var10 = nodeRoleList.iterator();

                ID departmentOwnerUser;
                do {
                    if (!var10.hasNext()) {
                        return false;
                    }

                    NodeRole nodeRole = (NodeRole) var10.next();
                    departmentOwnerUser = this.userService.getDepartmentOwnerUser(nodeRole.getId());
                } while (departmentOwnerUser == null || !callerId.equals(departmentOwnerUser));

                return true;
            case 5:
                return callerId.equals(this.initiatorDepartmentByLevel(entityRecord.id(), deptLevel));
            case 6:
                ID ownerDepartment = entityRecord.getFieldValue("ownerDepartment");
                return callerId.equals(this.findDeptLevelOwnerUser(ownerDepartment, deptLevel));
            default:
                return false;
        }
    }

    private String saveApprovalFlow(String approvalConfigId, String flowDefinition) {
        ApprovalFlow approvalFlow = new ApprovalFlow();
        approvalFlow.setApprovalConfigId(ID.valueOf(approvalConfigId));
        approvalFlow.setFlowDefinition(flowDefinition);
        return super.saveOrUpdateRecord("ApprovalFlow", null, approvalFlow).toString();
    }

    public void updateApprovalConfig(EntityRecord configRecord, String approvalFlowId) {
        Integer entityCode = configRecord.getFieldValue("entityCode");
        String flowName = configRecord.getFieldValue("flowName");
        ApprovalConfig approvalConfig = new ApprovalConfig();
        approvalConfig.setApprovalFlowId(ID.valueOf(approvalFlowId));
        approvalConfig.setEntityCode(entityCode);
        approvalConfig.setFlowName(flowName);
        approvalConfig.setIsDisabled(false);
        super.saveOrUpdateRecord("ApprovalConfig", configRecord.id(), approvalConfig);
        Entity entity = this.pm.getMetadataManager().getEntity(entityCode);
        if (!entity.containsField("approvalConfigId")) {
            this.entityManagerService.createApprovalSystemFields(entityCode);
        }

    }

    public void processApprovalTask(ApprovalTask approvalTask, List<TaskChildNode> taskChildNodeList, ApprovalProcessDTO approvalProcessDTO, boolean systemOperation) {
        approvalTask.setRemark(approvalProcessDTO.getRemark());
        approvalTask.setApprovalOn(new Date());
        approvalTask.setApprovalUser(ID.valueOf(this.callerContext.getCallerId()));
        this.addAttrTask(approvalTask, approvalProcessDTO, taskChildNodeList);
        List<ID> currentCCToUserList = this.getApprovalUser(approvalProcessDTO.getCurrentCCToUserList());
        ID[] currentCCtoUserArray = null;
        if (!CollectionUtils.isEmpty(currentCCToUserList)) {
            currentCCtoUserArray = currentCCToUserList.toArray(new ID[0]);
        }

        int historyNodeType = 1;
        if (approvalProcessDTO.getIsBacked()) {
            historyNodeType = 11;
        }

        if (systemOperation) {
            this.saveApprovalHistory(approvalTask, approvalTask.getStepName(), Integer.valueOf(historyNodeType), approvalProcessDTO.getIsBacked(), null, null);
        } else {
            this.saveApprovalHistory(approvalTask, approvalTask.getStepName(), Integer.valueOf(historyNodeType), approvalProcessDTO.getIsBacked(), currentCCtoUserArray);
        }

        if (approvalProcessDTO.getIsBacked()) {
            approvalTask.setStepName("流程已驳回");
            approvalTask.setCurrentNode(-1);
            approvalTask.setApprover(new ID[0]);
            approvalTask.setApprovalStatus(2);
            this.notificationService.addApprovalNotification(approvalTask.getEntityId(), MessageEnum.APPROVAL_MSG_REJECTED, approvalTask.getCreatedBy());
            TriggerService triggerService = this.pluginService.getTriggerService();
            if (triggerService != null) {
                triggerService.executeTrigger(TriggerWhenEnum.REJECTED, approvalTask.getEntityId(), triggerService.getTriggerLock());
            }
        } else {
            boolean turnNext = true;
            TaskChildNode taskChildNode = taskChildNodeList.get(approvalTask.getCurrentNode());
            EntityRecord entityRecord = super.queryRecordById(approvalTask.getEntityId());
            if (approvalTask.getApprover().length != 0 && !this.containsApprover(approvalTask.getEntityId())) {
                AssertHelper.isTrue(this.isSubmitApprovalPermissions(taskChildNode.getNodeRoleType(), taskChildNode.getNodeRoleList(), entityRecord, taskChildNode.getDeptLevel()), "您无权提交审核！");
            }

            if (approvalTask.getSignType() == 1) {
                turnNext = this.counterSignFinish(approvalTask);
            }

            if (turnNext) {
                ApprovalHelper.doStrategy(approvalTask, taskChildNodeList, approvalProcessDTO);
            }

            if (approvalTask.getApprovalStatus() == 3) {
                TriggerService triggerService = this.pluginService.getTriggerService();
                if (triggerService != null) {
                    triggerService.executeTrigger(TriggerWhenEnum.APPROVED, approvalTask.getEntityId(), triggerService.getTriggerLock());
                }
            }
        }

        this.updateEntityApprovalInfo(approvalTask.getEntityId(), null, approvalTask.getApprovalStatus(), approvalProcessDTO.getRemark());
        super.saveOrUpdateRecord("ApprovalTask", approvalTask.getApprovalTaskId(), approvalTask);
        this.revisionHistoryService.insertApprovalHistory(approvalTask.getEntityId(), approvalProcessDTO.getIsBacked());
    }

    private void addAttrTask(ApprovalTask approvalTask, ApprovalProcessDTO approvalProcessDTO, List<TaskChildNode> taskChildNodeList) {
        this.addCcToUser(approvalTask, approvalProcessDTO);
        this.addNextApprovalUser(approvalTask, approvalProcessDTO, taskChildNodeList);
        String nodeListStr = JsonHelper.writeObjectAsString(taskChildNodeList);
        approvalTask.setAttrMore(nodeListStr);
    }

    private void addCcToUser(ApprovalTask approvalTask, ApprovalProcessDTO approvalProcessDTO) {
        List<NodeRole> currentCCToUserList = approvalProcessDTO.getCurrentCCToUserList();
        if (!CollectionUtils.isEmpty(currentCCToUserList)) {
            List<ID> cctoUser = this.getApprovalUser(currentCCToUserList);
            ID[] ccTo = approvalTask.getCcTo();
            if (ccTo == null) {
                ccTo = new ID[0];
            }

            Set<ID> newCCtoUsers = new HashSet(Arrays.asList(ccTo));
            newCCtoUsers.addAll(cctoUser);
            approvalTask.setCcTo(newCCtoUsers.toArray(new ID[0]));
        }
    }

    private void addNextApprovalUser(ApprovalTask approvalTask, ApprovalProcessDTO approvalProcessDTO, List<TaskChildNode> taskChildNodeList) {
        List<NodeRole> nextApprovalUserList = approvalProcessDTO.getNextApprovalUserList();
        if (!CollectionUtils.isEmpty(nextApprovalUserList) && approvalTask.getCurrentNode() != -1 && !approvalProcessDTO.getIsBacked()) {
            int nextNode = approvalTask.getCurrentNode() + 1;

            for (int i = nextNode; i < taskChildNodeList.size(); ++i) {
                TaskChildNode taskChildNode = taskChildNodeList.get(nextNode);
                if (taskChildNode.getType() == 1) {
                    List<NodeRole> nodeRoleList = taskChildNode.getNodeRoleList();
                    nodeRoleList.addAll(nextApprovalUserList);
                    return;
                }
            }

        }
    }

    private boolean counterSignFinish(ApprovalTask approvalTask) {
        ID callerId = ID.valueOf(this.callerContext.getCallerId());
        ID[] approveIds = Arrays.stream(approvalTask.getApprover()).filter((id) -> {
            return !id.equals(callerId);
        }).toArray((x$0) -> {
            return new ID[x$0];
        });
        approvalTask.setApprover(approveIds);
        return approveIds.length == 0;
    }

    private void addApprovalHistorySteps(List<ApprovalStepsVO> approvalStepsVOList, List<TaskChildNode> taskChildNodes, List<ApprovalHistory> historyList) {
        Iterator var4 = historyList.iterator();

        while (true) {
            ApprovalHistory history;
            ApprovalStepsVO hisStep;
            while (true) {
                if (!var4.hasNext()) {
                    return;
                }

                history = (ApprovalHistory) var4.next();
                Integer currentNode = history.getCurrentNode();
                hisStep = new ApprovalStepsVO();
                BeanUtils.copyProperties(history, hisStep);
                if (currentNode < 0) {
                    break;
                }

                TaskChildNode taskChildNode = taskChildNodes.get(currentNode);
                if (2 != taskChildNode.getType()) {
                    hisStep.setSignType(taskChildNode.getMultiPersonApproval());
                    break;
                }
            }

            hisStep.setRecordId(history.getApprovalTaskId());
            String stepUserName;
            if (ObjectUtil.isNull(history.getApprover())) {
                stepUserName = "系统自动处理";
            } else {
                stepUserName = this.userService.getUserName(history.getApprover());
            }

            hisStep.setStepUserName(stepUserName);
            hisStep.setState(history.getNodeType());
            hisStep.setIsBacked(history.getIsBacked());
            hisStep.setCreatedOn(history.getApprovalOn());
            approvalStepsVOList.add(hisStep);
        }
    }

    private ApprovalStepsVO createApprovalStepForOngoing(TaskChildNode taskChildNode, ApprovalTask approvalTask, ID id) {
        ApprovalStepsVO stepVO = new ApprovalStepsVO();
        stepVO.setRecordId(approvalTask.getApprovalTaskId());
        stepVO.setStepName(taskChildNode.getNodeName());
        stepVO.setCurrentNode(approvalTask.getCurrentNode());
        stepVO.setStepUserName(this.userService.getUserName(id));
        stepVO.setSignType(taskChildNode.getMultiPersonApproval());
        stepVO.setCreatedOn(approvalTask.getCreatedOn());
        stepVO.setState(0);
        List<ID> referralUserList = this.getApprovalUser(taskChildNode.getReferralUserList());
        if (referralUserList.contains(id)) {
            stepVO.setOperationState(1);
        }

        List<ID> signUserList = this.getApprovalUser(taskChildNode.getSignUserList());
        if (signUserList.contains(id)) {
            stepVO.setOperationState(2);
        }

        return stepVO;
    }

    private void addStartSteps(List<ApprovalStepsVO> approvalStepsVOList, ApprovalTask approvalTask) {
        ApprovalStepsVO startStep = new ApprovalStepsVO();
        if (approvalTask.getApprovalConfigId() == null) {
            startStep.setStepName("系统自动审批");
            startStep.setNodeType(0);
            startStep.setCreatedOn(approvalTask.getCreatedOn());
            startStep.setStepUserName("系统自动审批");
        } else {
            ApprovalConfig approvalConfig = super.queryOneCustomPojoById(ApprovalConfig.class, approvalTask.getApprovalConfigId());
            startStep.setRecordId(approvalConfig.getApprovalConfigId());
            startStep.setStepName(approvalConfig.getFlowName());
            startStep.setNodeType(0);
            startStep.setCreatedOn(approvalTask.getCreatedOn());
            startStep.setStepUserName(this.userService.getUserName(approvalTask.getCreatedBy()));
        }

        approvalStepsVOList.add(startStep);
    }
}
