package cn.granitech.integration;

public class DeptInfoBean {
    private String departmentName;
    private String description;
    private String externalDepartmentId;
    private String parentDepartmentId;

    public String getDepartmentName() {
        return this.departmentName;
    }

    public void setDepartmentName(String departmentName2) {
        this.departmentName = departmentName2;
    }

    public String getParentDepartmentId() {
        return this.parentDepartmentId;
    }

    public void setParentDepartmentId(String parentDepartmentId2) {
        this.parentDepartmentId = parentDepartmentId2;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description2) {
        this.description = description2;
    }

    public String getExternalDepartmentId() {
        return this.externalDepartmentId;
    }

    public void setExternalDepartmentId(String externalDepartmentId2) {
        this.externalDepartmentId = externalDepartmentId2;
    }
}
