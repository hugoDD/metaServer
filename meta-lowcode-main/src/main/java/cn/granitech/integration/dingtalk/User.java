package cn.granitech.integration.dingtalk;

import java.util.List;

public class User {
    private boolean active;
    private boolean admin;
    private String avatar;
    private boolean boss;
    private List<Long> dept_id_list;
    private long dept_order;
    private String email;
    private boolean exclusive_account;
    private String extension;
    private boolean hide_mobile;
    private boolean leader;
    private String mobile;
    private String name;
    private String state_code;
    private String title;
    private String unionid;
    private String userid;

    public long getDept_order() {
        return this.dept_order;
    }

    public void setDept_order(long dept_order2) {
        this.dept_order = dept_order2;
    }

    public boolean isLeader() {
        return this.leader;
    }

    public void setLeader(boolean leader2) {
        this.leader = leader2;
    }

    public String getExtension() {
        return this.extension;
    }

    public void setExtension(String extension2) {
        this.extension = extension2;
    }

    public boolean isBoss() {
        return this.boss;
    }

    public void setBoss(boolean boss2) {
        this.boss = boss2;
    }

    public String getUnionid() {
        return this.unionid;
    }

    public void setUnionid(String unionid2) {
        this.unionid = unionid2;
    }

    public boolean isExclusive_account() {
        return this.exclusive_account;
    }

    public void setExclusive_account(boolean exclusive_account2) {
        this.exclusive_account = exclusive_account2;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile2) {
        this.mobile = mobile2;
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean active2) {
        this.active = active2;
    }

    public boolean isAdmin() {
        return this.admin;
    }

    public void setAdmin(boolean admin2) {
        this.admin = admin2;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String avatar2) {
        this.avatar = avatar2;
    }

    public boolean isHide_mobile() {
        return this.hide_mobile;
    }

    public void setHide_mobile(boolean hide_mobile2) {
        this.hide_mobile = hide_mobile2;
    }

    public String getUserid() {
        return this.userid;
    }

    public void setUserid(String userid2) {
        this.userid = userid2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public List<Long> getDept_id_list() {
        return this.dept_id_list;
    }

    public void setDept_id_list(List<Long> dept_id_list2) {
        this.dept_id_list = dept_id_list2;
    }

    public String getState_code() {
        return this.state_code;
    }

    public void setState_code(String state_code2) {
        this.state_code = state_code2;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email2) {
        this.email = email2;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public String toString() {
        return "User{userid='" + this.userid + '\'' + ", name='" + this.name + '\'' + ", extension='" + this.extension + '\'' + ", boss=" + this.boss + ", unionid='" + this.unionid + '\'' + ", exclusive_account=" + this.exclusive_account + ", mobile='" + this.mobile + '\'' + ", active=" + this.active + ", admin=" + this.admin + ", leader=" + this.leader + ", avatar='" + this.avatar + '\'' + ", hide_mobile=" + this.hide_mobile + ", dept_id_list=" + this.dept_id_list + ", dept_order=" + this.dept_order + ", state_code='" + this.state_code + '\'' + ", email='" + this.email + '\'' + ", title='" + this.title + '\'' + '}';
    }
}
