package cn.granitech.integration.dingtalk;

public class UserLoginInfo {
    private String mobile;
    private String nick;
    private String openId;
    private String stateCode;
    private String unionId;

    public String getNick() {
        return this.nick;
    }

    public void setNick(String nick2) {
        this.nick = nick2;
    }

    public String getUnionId() {
        return this.unionId;
    }

    public void setUnionId(String unionId2) {
        this.unionId = unionId2;
    }

    public String getOpenId() {
        return this.openId;
    }

    public void setOpenId(String openId2) {
        this.openId = openId2;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile2) {
        this.mobile = mobile2;
    }

    public String getStateCode() {
        return this.stateCode;
    }

    public void setStateCode(String stateCode2) {
        this.stateCode = stateCode2;
    }

    public String toString() {
        return "UserInfo{nick='" + this.nick + '\'' + ", unionId='" + this.unionId + '\'' + ", openId='" + this.openId + '\'' + ", mobile='" + this.mobile + '\'' + ", stateCode='" + this.stateCode + '\'' + '}';
    }
}
