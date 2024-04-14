package cn.granitech.trigger.business.service;

import cn.granitech.business.plugins.trigger.TriggerLock;
import cn.granitech.business.plugins.trigger.TriggerService;
import cn.granitech.business.query.QueryHelper;
import cn.granitech.business.query.SelectStatement;
import cn.granitech.business.service.BaseService;
import cn.granitech.business.service.CrudService;
import cn.granitech.business.timer.CronTaskRegistrar;
import cn.granitech.exception.ServiceException;
import cn.granitech.trigger.business.trigger.BaseTrigger;
import cn.granitech.trigger.business.trigger.TriggerRunnable;
import cn.granitech.trigger.business.trigger.action.*;
import cn.granitech.trigger.exception.TriggerServiceException;
import cn.granitech.util.EntityHelper;
import cn.granitech.util.FilterHelper;
import cn.granitech.util.JsonHelper;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.web.enumration.TriggerWhenEnum;
import cn.granitech.web.pojo.FormQueryResult;
import cn.granitech.web.pojo.filter.Filter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

import static cn.granitech.variantorm.constant.SystemEntities.TriggerLog;

@Service
public class TriggerServiceImpl extends BaseService implements TriggerService {
    private static final Map<Integer, BaseTrigger> TRIGGER_MAP = new HashMap<>();
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final ThreadLocal<Map<String, Object>> updateDataCache = new ThreadLocal<>();
    private final ThreadLocal<List<String>> deleteRecordIdList = new ThreadLocal<>();
    private final ThreadLocal<TriggerLock> triggerLockThreadLocal = new ThreadLocal<>();
    private final ThreadLocal<String> executeTriggerIdThreadLocal = new ThreadLocal<>();
    @Resource
    CrudService crudService;
    @Resource
    CronTaskRegistrar cronTaskRegistrar;
    @Resource
    DataUpdateTrigger dataUpdateTrigger;
    @Resource
    AggregationTrigger aggregationTrigger;
    @Resource
    DataVerificationTrigger dataVerificationTrigger;
    @Resource
    SendMsgTrigger sendMsgTrigger;
    @Resource
    DataDeleteTrigger dataDeleteTrigger;
    @Resource
    CallBackTrigger callBackTrigger;
    @Resource
    AutoApprovalTrigger autoApprovalTrigger;
    @Resource
    AutoRevokeTrigger autoRevokeTrigger;
    @Resource
    AssignTrigger assignTrigger;
    @Resource
    DataShareTrigger dataShareTrigger;
    @Resource
    DataUnShareTrigger dataUnShareTrigger;
    @Resource
    DataAutoCreateTrigger dataAutoCreateTrigger;
    @Resource
    GroupAggregationTrigger groupAggregationTrigger;
    @Resource
    TriggerLogServiceImpl logService;

    @PostConstruct
    public void initializeTriggerMap() {
        TRIGGER_MAP.put(1, this.dataUpdateTrigger);
        TRIGGER_MAP.put(2, this.aggregationTrigger);
        TRIGGER_MAP.put(3, this.groupAggregationTrigger);
        TRIGGER_MAP.put(4, this.dataVerificationTrigger);
        TRIGGER_MAP.put(5, this.sendMsgTrigger);
        TRIGGER_MAP.put(6, this.autoApprovalTrigger);
        TRIGGER_MAP.put(7, this.autoRevokeTrigger);
        TRIGGER_MAP.put(8, this.assignTrigger);
        TRIGGER_MAP.put(9, this.dataShareTrigger);
        TRIGGER_MAP.put(10, this.dataUnShareTrigger);

        TRIGGER_MAP.put(12, this.dataDeleteTrigger);

        TRIGGER_MAP.put(14, this.callBackTrigger);
        TRIGGER_MAP.put(15, this.dataAutoCreateTrigger);
    }

    @Transactional
    public FormQueryResult save(String recordId, Map<String, Object> dataMap) {
        EntityRecord entityRecord = this.pm.newRecord("TriggerConfig");
        EntityHelper.formatFieldValue(entityRecord, dataMap);
        if (StringUtils.isBlank(recordId)) {
            this.pm.insert(entityRecord);
        } else {
            ID callerId = ID.valueOf(this.callerContext.getCallerId());
            entityRecord.setFieldValue(entityRecord.getEntity().getIdField().getName(), ID.valueOf(recordId));
            entityRecord.setFieldValue("modifiedOn", new Date());
            entityRecord.setFieldValue("modifiedBy", callerId);
            this.pm.update(entityRecord);
        }
        EntityRecord savedRecord = this.crudService.queryById(ID.valueOf(recordId));
        saveCronTask(savedRecord);
        return new FormQueryResult(null, null, savedRecord, null, null);
    }


    private void saveCronTask(EntityRecord record) {
        Integer whenNum = record.getFieldValue("whenNum");
        TriggerRunnable triggerRunnable = new TriggerRunnable(record.getFieldValue("entityCode"), record.getFieldValue("triggerConfigId").toString(), record.getFieldValue("actionFilter"), record.getFieldValue("actionContent"), record.getFieldValue("actionType"));
        if (whenNum == null || !TriggerWhenEnum.TIMER.check(whenNum)) {
            this.cronTaskRegistrar.removeCronTask(triggerRunnable);
            return;
        }
        this.cronTaskRegistrar.addCronTask(triggerRunnable, record.getFieldValue("whenCron"));
    }


    public void executeTrigger(TriggerWhenEnum triggerEnum, ID entityId, TriggerLock triggerLock) {
        try {
            int HIERARCHY_NUMBER_MAX = 5;
            if (triggerLock.getHierarchyNumber() > HIERARCHY_NUMBER_MAX) {
                throw new ServiceException(String.format("触发器执行层数过深！当前层级%s", triggerLock.getHierarchyNumber()));
            }


            String filter = String.format("isDisabled = 0 AND (whenNum & %s) AND entityCode = %s", triggerEnum.getMaskValue(), entityId.getEntityCode());
            List<EntityRecord> entityRecords = this.crudService.queryListRecord("TriggerConfig", filter, null, "priority DESC", null, "triggerConfigId", "whenCron", "actionFilter", "name", "priority", "actionType", "actionContent");
            if (entityRecords.isEmpty()) {
                return;
            }


            StringBuffer easySql = getTriggerCheckSql(entityId, entityRecords);

            SelectStatement selectStatement = (new QueryHelper()).compileEasySql(this.pm.getMetadataManager(), easySql.toString());
            Map<String, Object> map = queryMapBySql(selectStatement.toString());


            for (EntityRecord entityRecord : entityRecords) {
                ID triggerConfigId = entityRecord.getFieldValue("triggerConfigId");

                if (!map.containsKey(triggerConfigId.getId()) || map.get(triggerConfigId.getId()) == null || (Long) map.get(triggerConfigId.getId()) == 0L) {
                    return;
                }

                String previousTriggerId = triggerLock.getExecuteTriggerId();

                if (StringUtils.isNotBlank(previousTriggerId)) {
                    if (previousTriggerId.equals(triggerConfigId.getId())) {
                        return;
                    }

                    this.log.info("触发器链式执行：{} ---- {}", previousTriggerId, triggerConfigId.getId());
                } else {
                    this.log.info("触发器执行：{}", triggerConfigId.getId());
                }

                setTriggerLock(triggerLock);

                setUpdateDataCache(triggerLock.getUpdateDataCache());

                setDeleteRecordIdList(triggerLock.getDeleteRecordIdList());

                setExecuteTriggerId(triggerConfigId.getId());

                int actionType = entityRecord.getFieldValue("actionType");
                String actionContent = entityRecord.getFieldValue("actionContent");
                trigger(triggerConfigId.toString(), entityId, actionType, actionContent, triggerEnum);
            }
            setTriggerLock(null);
            setExecuteTriggerId(null);
            setUpdateDataCache(null);
            setDeleteRecordIdList(null);
        } catch (TriggerServiceException e) {
            throw new ServiceException(e.getMessage());
        } catch (Exception e) {

            this.log.error("entityId={}，triggerEnum={} 触发器执行失败:", entityId, triggerEnum.getMaskValue(), e);
        }
    }


    public void executeTrigger(String triggerConfigId, int entityCode, String actionFilter, String actionContent, int actionType, TriggerWhenEnum triggerWhen) {
        Entity entity = this.pm.getMetadataManager().getEntity(entityCode);
        String filter = FilterHelper.toFilter(JsonHelper.readJsonValue(actionFilter, Filter.class));
        List<EntityRecord> query = this.pm.createRecordQuery().query(entity.getName(), filter, null, null, null, entity.getIdField().getName());
        for (EntityRecord entityRecord : query) {
            trigger(triggerConfigId, entityRecord.getFieldValue(entity.getIdField().getName()), actionType, actionContent, triggerWhen);
        }
    }


    public void initScheduledTasks() {
        List<EntityRecord> triggerList = queryTrigger(TriggerWhenEnum.TIMER);
        triggerList.forEach(trigger -> {
            String actionContent = trigger.getFieldValue("actionContent");
            String whenCron = trigger.getFieldValue("whenCron");
            if (StringUtils.isBlank(actionContent) || StringUtils.isBlank(whenCron)) {
                return;
            }
            this.cronTaskRegistrar.addCronTask(new TriggerRunnable(trigger.getFieldValue("entityCode"), trigger.getFieldValue("triggerConfigId").toString(), trigger.getFieldValue("actionFilter"), trigger.getFieldValue("actionContent"), trigger.getFieldValue("actionType")), trigger.getFieldValue("whenCron"));
        });
    }


    public List<EntityRecord> queryTrigger(TriggerWhenEnum triggerEnum) {
        String filter = String.format("isDisabled = 0 AND (whenNum & %s)", triggerEnum.getMaskValue());
        return this.crudService.queryListRecord("TriggerConfig", filter, null, "priority DESC", null, "triggerConfigId", "whenCron", "actionFilter", "entityCode", "name", "priority", "actionType", "actionContent");
    }


    private boolean trigger(String triggerConfigId, ID entityId, int actionType, String actionContent, TriggerWhenEnum triggerWhen) {
        String[] callerArray = setAdministrator();


        if (actionType == 6 || actionType == 7) {
            restoreCaller(callerArray);
        }

        boolean flag = false;
        String errorLog = null;
        try {
            BaseTrigger trigger = TRIGGER_MAP.get(actionType);
            if (trigger == null) {
                throw new ServiceException(String.format("没找到[actionType:%s]对应的触发器实现类", actionType));
            }
            flag = trigger.trigger(entityId, actionContent);
        } catch (TriggerServiceException e) {
            flag = true;
            throw e;
        } catch (Exception e) {

            try {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                errorLog = sw.toString();
            } catch (Exception e2) {
                this.log.error("获取触发器异常信息失败", e2);
            }
            this.log.error("触发器执行异常", e);
        } finally {
            EntityRecord entityRecord = this.pm.newRecord(TriggerLog);
            entityRecord.setFieldValue("triggerReason", (triggerWhen == null) ? "" : triggerWhen.getMaskLabel());
            entityRecord.setFieldValue("triggerConfigId", triggerConfigId);
            entityRecord.setFieldValue("actionType", actionType);
            entityRecord.setFieldValue("recordId", entityId.toString());
            entityRecord.setFieldValue("executeFlag", flag);
            entityRecord.setFieldValue("errorLog", errorLog);
//            saveOrUpdateRecord(null, entityRecord);
            String callerId = this.callerContext.getCallerId();
            String departmentId = this.callerContext.getDepartmentId();
            logService.log(entityRecord, callerId, departmentId);

        }

        restoreCaller(callerArray);
        return flag;
    }


    private StringBuffer getTriggerCheckSql(ID entityId, List<EntityRecord> triggerList) {
        Entity entity = this.pm.getMetadataManager().getEntity(entityId.getEntityCode());
        StringBuffer easySql = new StringBuffer("SELECT ");
        triggerList.forEach(entityRecord -> {
            String filterJson = entityRecord.getFieldValue("actionFilter");

            if (StringUtils.isBlank(filterJson)) {
                easySql.append(" TRUE AS ");
            } else {
                easySql.append(" ( ").append(FilterHelper.toFilter(JsonHelper.readJsonValue(filterJson, Filter.class))).append(" ) AS ");
            }
            easySql.append("'").append(entityRecord.getFieldValue("triggerConfigId").toString()).append("' ,");
        });
        easySql.delete(easySql.length() - 1, easySql.length());
        easySql.append(" FROM ").append(entity.getName()).append(" WHERE ").append(entity.getIdField().getName()).append(" = ").append("'").append(entityId.getId()).append("'");
        return easySql;
    }


    private String[] setAdministrator() {
        String[] callerArray = {this.callerContext.getCallerId(), this.callerContext.getDepartmentId()};
        this.callerContext.setCallerId("0000021-00000000000000000000000000000001");
        this.callerContext.setDepartmentIdLocal(null);
        return callerArray;
    }


    private void restoreCaller(String[] callerArray) {
        if (callerArray == null) {
            return;
        }
        this.callerContext.setCallerId(callerArray[0]);
        this.callerContext.setDepartmentIdLocal(callerArray[1]);
    }


    public TriggerLock getTriggerLock() {
        TriggerLock triggerLock = this.triggerLockThreadLocal.get();
        if (triggerLock == null) {
            triggerLock = new TriggerLock();
        } else {
            triggerLock = triggerLock.getNextHierarchy();
        }
        triggerLock.setExecuteTriggerId(getExecuteTriggerId());
        return triggerLock;
    }

    public void setTriggerLock(TriggerLock triggerLock) {
        this.triggerLockThreadLocal.set(triggerLock);
    }

    public Map<String, Object> getUpdateDataCache() {
        return this.updateDataCache.get();
    }

    public void setUpdateDataCache(Map<String, Object> updateDataCache) {
        this.updateDataCache.set(updateDataCache);
    }


    public List<String> getDeleteRecordIdList() {
        if (this.deleteRecordIdList.get() == null) {
            this.deleteRecordIdList.set(new ArrayList<>());
        }
        return this.deleteRecordIdList.get();
    }

    public void setDeleteRecordIdList(List<String> deleteRecordIdList) {
        this.deleteRecordIdList.set(deleteRecordIdList);
    }

    public String getExecuteTriggerId() {
        return this.executeTriggerIdThreadLocal.get();
    }

    public void setExecuteTriggerId(String executeTriggerId) {
        this.executeTriggerIdThreadLocal.set(executeTriggerId);
    }
}



