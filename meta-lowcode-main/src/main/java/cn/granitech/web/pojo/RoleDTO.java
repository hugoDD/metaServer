package cn.granitech.web.pojo;

import java.util.List;
import java.util.Map;

public class RoleDTO {
    private String description;
    private Boolean disabled;
    private List<Map<String, Object>> rightEntityList;
    private Map<String, Object> rightValueMap;
    private String roleId;
    private String roleName;

    public String getRoleId() {
        return this.roleId;
    }

    public void setRoleId(String roleId2) {
        this.roleId = roleId2;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public void setRoleName(String roleName2) {
        this.roleName = roleName2;
    }

    public Boolean getDisabled() {
        return this.disabled;
    }

    public void setDisabled(Boolean disabled2) {
        this.disabled = disabled2;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description2) {
        this.description = description2;
    }

    public Map<String, Object> getRightValueMap() {
        return this.rightValueMap;
    }

    public void setRightValueMap(Map<String, Object> rightValueMap2) {
        this.rightValueMap = rightValueMap2;
    }

    public List<Map<String, Object>> getRightEntityList() {
        return this.rightEntityList;
    }

    public void setRightEntityList(List<Map<String, Object>> rightEntityList2) {
        this.rightEntityList = rightEntityList2;
    }
}
