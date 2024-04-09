package cn.granitech.web.pojo;

import cn.granitech.business.service.UserService;
import cn.granitech.variantorm.metadata.ID;

public class Department extends BasePojo {
    private ID departmentId;
    private String departmentName;
    private ID parentDepartmentId;

    public ID getDepartmentId() {
        this.departmentId = this._entityRecord.getFieldValue(UserService.DEPARTMENT_ID);
        return this.departmentId;
    }

    public void setDepartmentId(ID departmentId2) {
        this.departmentId = departmentId2;
        this._entityRecord.setFieldValue(UserService.DEPARTMENT_ID, departmentId2);
    }

    public ID getParentDepartmentId() {
        this.parentDepartmentId = this._entityRecord.getFieldValue(UserService.PARENT_DEPARTMENT_ID);
        return this.parentDepartmentId;
    }

    public void setParentDepartmentId(ID parentDepartmentId2) {
        this.parentDepartmentId = parentDepartmentId2;
        this._entityRecord.setFieldValue(UserService.PARENT_DEPARTMENT_ID, parentDepartmentId2);
    }

    public String getDepartmentName() {
        this.departmentName = this._entityRecord.getFieldValue(UserService.DEPARTMENT_NAME);
        return this.departmentName;
    }

    public void setDepartmentName(String departmentName2) {
        this.departmentName = departmentName2;
        this._entityRecord.setFieldValue(UserService.DEPARTMENT_NAME, departmentName2);
    }
}
