package cn.granitech.interceptor;

import cn.granitech.business.service.RightManager;
import cn.granitech.business.service.ShareAccessService;
import cn.granitech.business.service.UserService;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.web.enumration.EntityRightEnum;
import cn.granitech.web.enumration.SystemRightEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CallerContext {
    @Resource
    RightManager rightManager;
    @Resource
    ShareAccessService shareAccessService;
    @Resource
    UserService userService;
    private final ThreadLocal<String> callerIdLocal = new ThreadLocal<>();
    private final ThreadLocal<String> departmentIdLocal = new ThreadLocal<>();

    public void cleanThreadLocal() {
        this.callerIdLocal.remove();
        this.departmentIdLocal.remove();
    }

    public String getDepartmentId() {
        if (getCallerId() != null && StringUtils.isBlank(this.departmentIdLocal.get())) {
            this.departmentIdLocal.set(this.userService.getDepartmentIdOfUser(ID.valueOf(this.callerIdLocal.get())).toString());
        }
        return this.departmentIdLocal.get();
    }

    public ID getDepartmentID() {
        return ID.valueOf(getDepartmentId());
    }

    public void setDepartmentIdLocal(String departmentId) {
        this.departmentIdLocal.set(departmentId);
    }

    public String getCallerId() {
        return this.callerIdLocal.get();
    }

    public void setCallerId(String callerId) {
        this.callerIdLocal.set(callerId);
    }

    public ID getCallerID() {
        return ID.valueOf(this.callerIdLocal.get());
    }

    public boolean checkSystemRight(SystemRightEnum role) {
        return this.rightManager.getFunctionRight(role);
    }

    public boolean checkCreateRight(EntityRecord entityRecord) {
        return this.rightManager.checkEntityRight(entityRecord, EntityRightEnum.create);
    }

    public boolean checkUpdateRight(EntityRecord entityRecord) {
        return this.shareAccessService.getShareRight(entityRecord.id(), true) || this.rightManager.checkEntityRight(entityRecord, EntityRightEnum.update);
    }

    public boolean checkDeleteRight(EntityRecord entityRecord) {
        return this.rightManager.checkEntityRight(entityRecord, EntityRightEnum.delete);
    }

    public boolean checkAssignRight(EntityRecord entityRecord) {
        return this.rightManager.checkEntityRight(entityRecord, EntityRightEnum.assign);
    }

    public boolean checkShareRight(EntityRecord entityRecord) {
        return this.rightManager.checkEntityRight(entityRecord, EntityRightEnum.share);
    }

    public boolean checkQueryRight(EntityRecord entityRecord) {
        return this.shareAccessService.getShareRight(entityRecord.id(), false) || this.rightManager.checkEntityRight(entityRecord, EntityRightEnum.query);
    }

    public boolean checkQueryRight(int entityCode) {
        try {
            return this.rightManager.getEntityRight(EntityRightEnum.query.getCode(Integer.valueOf(entityCode))).intValue() != 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
