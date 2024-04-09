package cn.granitech.integration.dingtalk;

import cn.granitech.business.service.UserService;
import cn.granitech.integration.DeptInfoBean;
import cn.granitech.integration.OrganizationSync;
import cn.granitech.integration.UserInfoBean;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.web.constant.SessionNames;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DingTalkOrganizationSync extends OrganizationSync {
    public DingTalkOrganizationSync(ID defaultRole) {
        super(defaultRole);
    }

    /* access modifiers changed from: protected */
    public String departmentField() {
        return UserService.DING_DEPARTMENT_ID;
    }

    /* access modifiers changed from: protected */
    public String userField() {
        return SessionNames.DingTalkUserId;
    }

    public List<UserInfoBean> fetchUsers() {
        List<UserInfoBean> userInfoBeanList = new ArrayList<>();
        Set<String> existDingUserId = new HashSet<>();
        for (User dingTalkUser : DingTalkSdk.findAllUser()) {
            String dingTalkUserId = dingTalkUser.getUserid();
            if (!existDingUserId.contains(dingTalkUserId)) {
                UserInfoBean userInfoBean = new UserInfoBean();
                existDingUserId.add(dingTalkUserId);
                userInfoBean.setExternalUserId(dingTalkUser.getUserid());
                userInfoBean.setUserName(dingTalkUser.getName());
                userInfoBean.setEmail(dingTalkUser.getEmail());
                userInfoBean.setMobilePhone(dingTalkUser.getMobile());
                userInfoBean.setJobTitle(dingTalkUser.getTitle());
                List<Long> deptIdList = dingTalkUser.getDept_id_list();
                userInfoBean.setDepartmentId(deptIdList.isEmpty() ? "1" : String.valueOf(deptIdList.get(deptIdList.size() - 1)));
                userInfoBeanList.add(userInfoBean);
            }
        }
        return userInfoBeanList;
    }

    public List<DeptInfoBean> fetchDepts() {
        List<DeptInfoBean> deptInfoBeanList = new ArrayList<>();
        for (Department dingTalkDepartment : DingTalkSdk.findAllDepartment()) {
            DeptInfoBean deptInfoBean = new DeptInfoBean();
            deptInfoBean.setDepartmentName(dingTalkDepartment.getName());
            deptInfoBean.setDescription("钉钉同步部门");
            deptInfoBean.setExternalDepartmentId(String.valueOf(dingTalkDepartment.getDept_id()));
            deptInfoBean.setParentDepartmentId(String.valueOf(dingTalkDepartment.getParent_id()));
            deptInfoBeanList.add(deptInfoBean);
        }
        return deptInfoBeanList;
    }

    /* access modifiers changed from: protected */
    public void sendMessage(String message, String userId) {
        DingTalkSdk.sendMessage(message, userId);
    }
}
