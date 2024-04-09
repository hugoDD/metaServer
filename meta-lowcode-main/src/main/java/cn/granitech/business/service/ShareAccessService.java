//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cn.granitech.business.service;

import cn.granitech.business.plugins.trigger.TriggerService;
import cn.granitech.exception.ServiceException;
import cn.granitech.util.EntityHelper;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.variantorm.pojo.Field;
import cn.granitech.web.enumration.TriggerWhenEnum;
import cn.granitech.web.pojo.Cascade;
import cn.hutool.core.collection.CollUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShareAccessService extends BaseService {
    public static final String SHARE_ENTITY_CODE = "entityCode";
    public static final String SHARE_ENTITY_ID = "entityId";
    public static final String SHARE_TO = "shareTo";
    public static final String SHARE_WITH_UPDATE = "withUpdate";
    public static final int SHARE_WITH_EVERYONE = 1;
    public static final int SHARE_WITH_APPOINT = 2;
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Resource
    CrudService crudService;
    @Resource
    UserService userService;
    @Resource
    PluginService pluginService;

    public ShareAccessService() {
    }

    public String getShareReadRightFilter(int entityCode, Field idField) {
        Entity entity = EntityHelper.getEntity(entityCode);
        return entity.isAuthorizable() ? this.buildShareReadRightFilter(idField) : "";
    }

    private String buildShareReadRightFilter(Field idField) {
        ID callerId = this.callerContext.getCallerID();
        ID departmentID = this.callerContext.getDepartmentID();
        List<ID> roleIds = this.userService.getRoleIDListOfUser(callerId);
        List<ID> teamIds = this.userService.getTeamIDListOfUser(callerId);
        List<ID> shareComponents = new ArrayList();
        shareComponents.add(callerId);
        shareComponents.add(departmentID);
        if (!roleIds.isEmpty()) {
            shareComponents.addAll(roleIds);
        }

        if (!teamIds.isEmpty()) {
            shareComponents.addAll(teamIds);
        }

        String sharesStr = shareComponents.stream().map(ID::getId).collect(Collectors.joining("\",\"", "\"", "\""));
        return String.format("%s IN (SELECT %s FROM %s WHERE %s IN (%s))", idField.getName(), "entityId", "ShareAccess", "shareTo", sharesStr);
    }

    public boolean getShareRight(ID recordId, boolean isUpdate) {
        if (recordId == null) {
            return false;
        } else {
            String callerId = this.callerContext.getCallerId();
            String filter = String.format(" %s = '%s' and %s ='%s ' ", "entityId", recordId.getId(), "shareTo", callerId);
            EntityRecord shareRecord = super.queryOneRecord("ShareAccess", filter, null, null);
            if (shareRecord == null) {
                return false;
            } else {
                return !isUpdate || (Boolean) shareRecord.getFieldValue("withUpdate");
            }
        }
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public int cancelShareRecord(Integer userType, List<String> shareRecordsId, List<String> shareUsersId) {
        if (shareRecordsId == null) {
            throw new ServiceException("参数异常！");
        } else {
            int successCount = 0;

            for (String shareId : shareRecordsId) {
                ID recordId = ID.valueOf(shareId);
                if (userType == 1) {
                    successCount += this.cancelShareRecordForAllUsers(recordId);
                } else {
                    String shareUser;
                    if (userType == 2 && shareUsersId != null) {
                        for (Iterator<String> var8 = shareUsersId.iterator(); var8.hasNext(); successCount += this.cancelShareRecordForUser(recordId, ID.valueOf(shareUser))) {
                            shareUser = var8.next();
                        }
                    }
                }
            }

            return successCount;
        }
    }

    private int cancelShareRecordForAllUsers(ID recordId) {
        String filter = String.format("%s = '%s'", "entityId", recordId.getId());
        List<String> batchDeleteRecord = super.batchDeleteRecord("ShareAccess", filter);
        if (CollUtil.isNotEmpty(batchDeleteRecord)) {
            TriggerService triggerService = this.pluginService.getTriggerService();
            if (triggerService != null) {
                triggerService.executeTrigger(TriggerWhenEnum.UNSHARE, recordId, triggerService.getTriggerLock());
            }
        }

        return batchDeleteRecord.size();
    }

    private int cancelShareRecordForUser(ID recordId, ID shareUserId) {
        EntityRecord entityRecord = super.queryRecordById(recordId, "ownerUser", "ownerDepartment");
        if (entityRecord != null && this.callerContext.checkShareRight(entityRecord)) {
            String filter = String.format("%s = '%s' and %s = '%s'", "entityId", recordId.getId(), "shareTo", shareUserId);
            EntityRecord shareRecord = super.queryOneRecord("ShareAccess", filter, null, null);
            if (shareRecord != null) {
                super.deleteRecord(shareRecord.id());
                TriggerService triggerService = this.pluginService.getTriggerService();
                if (triggerService != null) {
                    triggerService.executeTrigger(TriggerWhenEnum.UNSHARE, recordId, triggerService.getTriggerLock());
                }

                return 1;
            }
        }

        return 0;
    }

    @Transactional
    public int shareRecord(List<String> shareRecordsId, List<String> shareUsersId, List<Cascade> cascades, boolean withUpdate) {
        if (shareRecordsId != null && shareUsersId != null) {
            int successCount = 0;

            for (String shareRecord : shareRecordsId) {
                ID recordId = ID.valueOf(shareRecord);

                for (String shareUser : shareUsersId) {
                    ID shareUserId = ID.valueOf(shareUser);
                    boolean isSuccessful = this.saveShareRecord(recordId, shareUserId, withUpdate);
                    if (!isSuccessful && shareRecordsId.size() == 1) {
                        this.log.info("当前数据异常,record={}", recordId);
                        throw new ServiceException("当前用户权限不足！");
                    }

                    ++successCount;
                    List<EntityRecord> entityRecords = this.crudService.queryCascadeRecordList(recordId, cascades);

                    for (EntityRecord casRecords : entityRecords) {
                        this.saveShareRecord(casRecords.id(), shareUserId, withUpdate);
                    }
                }
            }

            return successCount;
        } else {
            throw new ServiceException("参数异常！");
        }
    }

    private boolean saveShareRecord(ID recordId, ID userId, boolean withUpdate) {
        if (recordId != null && recordId != userId) {
            EntityRecord findRecord = this.crudService.queryById(recordId, "ownerUser", "ownerDepartment");
            if (findRecord != null && this.callerContext.checkShareRight(findRecord)) {
                String filter = String.format(" %s = '%s' and %s = '%s' ", "entityId", recordId.getId(), "shareTo", userId);
                EntityRecord shareRecord = this.crudService.queryOneRecord("ShareAccess", filter, null, null);
                if (shareRecord == null) {
                    EntityRecord newRecord = this.pm.newRecord("ShareAccess");
                    newRecord.setFieldValue("entityCode", findRecord.getEntity().getEntityCode());
                    newRecord.setFieldValue("entityId", recordId.getId());
                    newRecord.setFieldValue("shareTo", userId);
                    newRecord.setFieldValue("withUpdate", withUpdate);
                    super.createRecord(newRecord);
                    TriggerService triggerService = this.pluginService.getTriggerService();
                    if (triggerService != null) {
                        triggerService.executeTrigger(TriggerWhenEnum.SHARE, recordId, triggerService.getTriggerLock());
                    }
                }

                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
