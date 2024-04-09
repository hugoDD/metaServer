package cn.granitech.web.pojo.vo;

import cn.granitech.variantorm.metadata.ID;

import java.util.List;

public class UserInfoVO {
    private String departmentName;
    private List<ID> roleIds;
    private List<ID> teamIds;
    private String userAvatar;
    private ID userId;
    private String userName;

    public ID getUserId() {
        return this.userId;
    }

    public void setUserId(ID userId2) {
        this.userId = userId2;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName2) {
        this.userName = userName2;
    }

    public List<ID> getTeamIds() {
        return this.teamIds;
    }

    public void setTeamIds(List<ID> teamIds2) {
        this.teamIds = teamIds2;
    }

    public List<ID> getRoleIds() {
        return this.roleIds;
    }

    public void setRoleIds(List<ID> roleIds2) {
        this.roleIds = roleIds2;
    }

    public String getDepartmentName() {
        return this.departmentName;
    }

    public void setDepartmentName(String departmentName2) {
        this.departmentName = departmentName2;
    }

    public String getUserAvatar() {
        return this.userAvatar;
    }

    public void setUserAvatar(String userAvatar2) {
        this.userAvatar = userAvatar2;
    }
}
