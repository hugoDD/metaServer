package cn.granitech.integration;

public class UserInfoBean {
    private String departmentId;
    private String email;
    private String externalUserId;
    private String jobTitle;
    private String mobilePhone;
    private String userName;

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName2) {
        this.userName = userName2;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email2) {
        this.email = email2;
    }

    public String getMobilePhone() {
        return this.mobilePhone;
    }

    public void setMobilePhone(String mobilePhone2) {
        this.mobilePhone = mobilePhone2;
    }

    public String getJobTitle() {
        return this.jobTitle;
    }

    public void setJobTitle(String jobTitle2) {
        this.jobTitle = jobTitle2;
    }

    public String getExternalUserId() {
        return this.externalUserId;
    }

    public void setExternalUserId(String externalUserId2) {
        this.externalUserId = externalUserId2;
    }

    public String getDepartmentId() {
        return this.departmentId;
    }

    public void setDepartmentId(String departmentId2) {
        this.departmentId = departmentId2;
    }

    public String toString() {
        return "UserInfoBean{userName='" + this.userName + '\'' + ", email='" + this.email + '\'' + ", mobilePhone='" + this.mobilePhone + '\'' + ", jobTitle='" + this.jobTitle + '\'' + ", externalUserId='" + this.externalUserId + '\'' + ", departmentId='" + this.departmentId + '\'' + '}';
    }
}
