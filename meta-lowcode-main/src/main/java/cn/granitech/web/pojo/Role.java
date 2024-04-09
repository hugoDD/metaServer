package cn.granitech.web.pojo;

public class Role extends BasePojo {
    private String description;
    private Boolean disabled;
    private RightJsonObject rightJson;
    private String roleName;

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

    public RightJsonObject getRightJson() {
        return this.rightJson;
    }

    public void setRightJson(RightJsonObject rightJson2) {
        this.rightJson = rightJson2;
    }
}
