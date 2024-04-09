package cn.granitech.integration;

import cn.granitech.business.service.CrudService;
import cn.granitech.business.service.OptionManagerService;
import cn.granitech.business.service.SystemService;
import cn.granitech.business.service.UserService;
import cn.granitech.business.task.HeavyTask;
import cn.granitech.exception.ServiceException;
import cn.granitech.integration.dingtalk.DingTalkSdk;
import cn.granitech.util.EntityHelper;
import cn.granitech.util.MD5Helper;
import cn.granitech.util.SpringHelper;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.variantorm.pojo.OptionModel;
import cn.granitech.web.pojo.approval.node.NodeRole;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;

import java.util.*;

public abstract class OrganizationSync extends HeavyTask<Integer> {
    private final List<Map<String, String>> accountInfo = new ArrayList();
    private final CrudService crudService;
    private ID defaultRole;
    private final Map<String, String> departmentIdMapping = new HashMap();
    private final List<OptionModel> jobOptionModel;
    private int maxValue = 0;
    private final OptionManagerService optionManagerService;
    private final PersistenceManager pm;
    private final UserService userService;

    public OrganizationSync(ID defaultRole2) {
        this.defaultRole = defaultRole2;
        this.pm = SpringHelper.getBean(PersistenceManager.class);
        this.userService = SpringHelper.getBean(UserService.class);
        this.crudService = SpringHelper.getBean(CrudService.class);
        this.optionManagerService = SpringHelper.getBean(OptionManagerService.class);
        this.jobOptionModel = this.optionManagerService.getOptionList("User", "jobTitle");
    }

    public static String incrementLoginName(String originalName) {
        String prefix = originalName.replaceAll("[0-9]", "");
        return prefix + (Integer.parseInt(originalName.replaceAll("[^0-9]", "")) + 1);
    }

    /* access modifiers changed from: protected */
    public abstract String departmentField();

    /* access modifiers changed from: protected */
    public abstract List<DeptInfoBean> fetchDepts();

    /* access modifiers changed from: protected */
    public abstract List<UserInfoBean> fetchUsers();

    /* access modifiers changed from: protected */
    public abstract void sendMessage(String str, String str2);

    /* access modifiers changed from: protected */
    public abstract String userField();

    /* access modifiers changed from: protected */
    public Integer execute() {
        setTotal(fetchUsers().size());
        syncOrganizationInfo();
        return Integer.valueOf(getSucceeded());
    }

    public void syncOrganizationInfo() {
        EntityRecord deptRecord;
        List<DeptInfoBean> deptInfoBeanList = fetchDepts();
        Iterator<DeptInfoBean> it = deptInfoBeanList.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            DeptInfoBean deptInfoBean = it.next();
            if (isInterrupt()) {
                setInterrupted();
                break;
            }
            String externalDepartmentId = deptInfoBean.getExternalDepartmentId();
            String deptId = "0000022-00000000000000000000000000000001";
            if (!"1".equals(externalDepartmentId)) {
                EntityRecord existDeptRecord = findExistDeptRecord(externalDepartmentId);
                if (existDeptRecord == null) {
                    deptId = null;
                } else {
                    deptId = existDeptRecord.id().getId();
                }
            }
            this.departmentIdMapping.put(externalDepartmentId, deptId);
        }
        Iterator<DeptInfoBean> it2 = deptInfoBeanList.iterator();
        while (true) {
            if (!it2.hasNext()) {
                break;
            }
            DeptInfoBean deptInfoBean2 = it2.next();
            if (isInterrupt()) {
                setInterrupted();
                break;
            }
            String externalDepartmentId2 = deptInfoBean2.getExternalDepartmentId();
            if ("1".equals(externalDepartmentId2)) {
                deptRecord = this.crudService.queryRecordById(ID.valueOf("0000022-00000000000000000000000000000001"));
            } else {
                String parentId = deptInfoBean2.getParentDepartmentId();
                deptRecord = this.crudService.newRecord("Department");
                deptRecord.setFieldValue(UserService.PARENT_DEPARTMENT_ID, this.departmentIdMapping.getOrDefault(parentId, "0000022-00000000000000000000000000000001"));
            }
            String deptId2 = this.departmentIdMapping.get(externalDepartmentId2);
            deptRecord.setFieldValue(UserService.DEPARTMENT_NAME, deptInfoBean2.getDepartmentName());
            deptRecord.setFieldValue(departmentField(), deptInfoBean2.getExternalDepartmentId());
            deptRecord.setFieldValue("description", deptInfoBean2.getDescription());
            this.departmentIdMapping.put(externalDepartmentId2, String.valueOf(this.crudService.saveOrUpdateRecord(deptId2 == null ? null : ID.valueOf(deptId2), deptRecord)));
        }
        for (EntityRecord entityRecord : this.crudService.queryListRecord("Department", null, null, "[departmentId]", null)) {
            ID departmentId = entityRecord.id();
            String externalDepartmentId3 = entityRecord.getFieldValue(departmentField());
            if (!this.departmentIdMapping.containsValue(departmentId.getId()) && StrUtil.isNotBlank(externalDepartmentId3)) {
                if (this.crudService.queryListRecord("User", String.format(" departmentId = '%s' ", departmentId.getId()), null, null, null, new String[0]).isEmpty()) {
                    this.crudService.delete(departmentId);
                } else {
                    entityRecord.setFieldValue(departmentField(), null);
                    this.crudService.update(entityRecord);
                }
            }
        }
        Iterator<UserInfoBean> it3 = fetchUsers().iterator();
        while (true) {
            if (!it3.hasNext()) {
                break;
            }
            UserInfoBean userInfoBean = it3.next();
            if (isInterrupt()) {
                setInterrupted();
                break;
            }
            EntityRecord existUserRecord = findExistUserRecord(userInfoBean.getExternalUserId());
            if (existUserRecord == null) {
                EntityRecord existUserRecord2 = this.crudService.newRecord("User");
                setUserInfoFields(existUserRecord2, userInfoBean, true);
                saveUserRole(this.crudService.saveOrUpdateRecord(null, existUserRecord2));
            } else {
                setUserInfoFields(existUserRecord, userInfoBean, false);
                this.crudService.saveOrUpdateRecord(existUserRecord.id(), existUserRecord);
            }
            addCompleted();
        }
        this.userService.reloadCache();
        sendAccountNotification();
    }

    private void saveUserRole(ID userId) {
        if (this.defaultRole == null) {
            List<NodeRole> nodeRoleList = DingTalkSdk.getDingTalkConfig().getNodeRole();
            this.defaultRole = CollectionUtil.isNotEmpty(nodeRoleList) ? nodeRoleList.get(0).getId() : null;
        }
        if (this.defaultRole == null) {
            return;
        }
        if (23 != this.defaultRole.getEntityCode()) {
            throw new ServiceException("传入的默认角色有误！");
        }
        this.userService.updateTeamOrRoleUser(this.defaultRole, userId, true);
    }

    private void sendAccountNotification() {
        for (Map<String, String> accountMap : this.accountInfo) {
            sendMessage(String.format("【账号开通】感谢您选择美乐低代码平台。您的登录账号为：[%s]，初始密码为：[%s]。\n为了保障您的账户安全，建议您尽快登录并修改密码。如需帮助，请联系客服支持团队。\n\n祝您生活愉快！", accountMap.get(UserService.LOGIN_NAME), accountMap.get(UserService.LOGIN_PWD)), accountMap.get("externalUserId"));
        }
    }

    private void setUserInfoFields(EntityRecord userRecord, UserInfoBean userInfoBean, boolean isSave) {
        if (isSave) {
            handleNewUser(userRecord, userInfoBean);
        }
        ID oldDepartmentId = userRecord.getFieldValue(UserService.DEPARTMENT_ID);
        String newDepartmentId = this.departmentIdMapping.get(userInfoBean.getDepartmentId());
        if (oldDepartmentId != null && !oldDepartmentId.equals(newDepartmentId)) {
            refreshDepartment(userRecord.id().getId(), oldDepartmentId.getId(), newDepartmentId);
        }
        userRecord.setFieldValue(UserService.USER_NAME, userInfoBean.getUserName());
        userRecord.setFieldValue(SystemService.InternalObject.EMAIL, userInfoBean.getEmail());
        userRecord.setFieldValue("mobilePhone", userInfoBean.getMobilePhone());
        userRecord.setFieldValue(UserService.DEPARTMENT_ID, newDepartmentId);
        userRecord.setFieldValue(userField(), userInfoBean.getExternalUserId());
        userRecord.setFieldValue(UserService.DISABLED, false);
        if (StrUtil.isNotBlank(userInfoBean.getJobTitle())) {
            userRecord.setFieldValue("jobTitle", getJobTitleValue(userInfoBean.getJobTitle()));
            this.optionManagerService.reloadOptionsByField("User", "jobTitle");
        }
    }

    private void handleNewUser(EntityRecord userRecord, UserInfoBean userInfoBean) {
        String loginName = findExistLoginName(userInfoBean.getUserName().trim());
        String randomPw = RandomUtil.randomString(6);
        String password = MD5Helper.md5HexForSalt(randomPw, loginName);
        userRecord.setFieldValue(UserService.LOGIN_NAME, loginName);
        userRecord.setFieldValue(UserService.LOGIN_PWD, password);
        Map<String, String> userMap = new HashMap<>();
        userMap.put(UserService.LOGIN_NAME, loginName);
        userMap.put(UserService.LOGIN_PWD, randomPw);
        userMap.put("externalUserId", userInfoBean.getExternalUserId());
        this.accountInfo.add(userMap);
    }

    private void refreshDepartment(String ownerUser, String oldDepartmentId, String newDepartmentId) {
        if (!StrUtil.hasEmpty(ownerUser, oldDepartmentId, newDepartmentId)) {
            for (Entity entity : this.pm.getMetadataManager().getEntitySet()) {
                if (entity.isAuthorizable()) {
                    System.out.println("正在冲洗的数据表：" + entity.getName());
                    this.pm.batchAssign(entity.getName(), String.format(" ownerUser='%s' and ownerDepartment='%s' ", ownerUser, oldDepartmentId), ID.valueOf(newDepartmentId), ID.valueOf(ownerUser));
                }
            }
        }
    }

    private String findExistLoginName(String userName) {
        String filter = String.format(" userName ='%s' and %s is null ", userName, userField());
        EntityRecord userRecord = this.crudService.queryOneRecord("User", filter, null, " [loginName] DESC ", UserService.USER_ID, UserService.LOGIN_NAME);
        if (userRecord != null) {
            return incrementLoginName(userRecord.getFieldValue(UserService.LOGIN_NAME));
        }
        return userName;
    }

    private int getJobTitleValue(String jobTitle) {
        int jobTitleValue = findJobTitleValue(jobTitle);
        if (jobTitleValue == -1) {
            return saveNewOption(jobTitle);
        }
        return jobTitleValue;
    }

    private int findJobTitleValue(String jobTitle) {
        for (OptionModel optionModel : this.jobOptionModel) {
            Integer displayOrder = optionModel.getDisplayOrder();
            if (this.maxValue < displayOrder.intValue()) {
                this.maxValue = displayOrder.intValue();
            }
            if (Objects.equals(optionModel.getLabel(), jobTitle)) {
                return optionModel.getValue().intValue();
            }
        }
        return -1;
    }

    public int saveNewOption(String optionKey) {
        this.maxValue++;
        EntityRecord optionRecord = this.crudService.newRecord("OptionItem");
        optionRecord.setFieldValue("entityName", "User");
        optionRecord.setFieldValue(EntityHelper.FIELD_NAME, "jobTitle");
        optionRecord.setFieldValue("label", optionKey);
        optionRecord.setFieldValue("value", Integer.valueOf(this.maxValue));
        optionRecord.setFieldValue("displayOrder", Integer.valueOf(this.maxValue));
        this.crudService.createRecord(optionRecord);
        return this.maxValue;
    }

    private EntityRecord findExistDeptRecord(String externalDeptId) {
        return this.crudService.queryOneRecord("Department", String.format(" %s ='%s' ", departmentField(), externalDeptId), null, null);
    }

    private EntityRecord findExistUserRecord(String externalUserId) {
        return this.crudService.queryOneRecord("User", String.format(" %s ='%s'  ", userField(), externalUserId), null, null);
    }
}
