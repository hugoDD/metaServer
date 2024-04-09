package cn.granitech.integration.dingtalk;

public class Department {
    private boolean auto_add_user;
    private boolean create_dept_group;
    private int dept_id;
    private String name;
    private int parent_id;

    public boolean isAuto_add_user() {
        return this.auto_add_user;
    }

    public void setAuto_add_user(boolean auto_add_user2) {
        this.auto_add_user = auto_add_user2;
    }

    public boolean isCreate_dept_group() {
        return this.create_dept_group;
    }

    public void setCreate_dept_group(boolean create_dept_group2) {
        this.create_dept_group = create_dept_group2;
    }

    public int getDept_id() {
        return this.dept_id;
    }

    public void setDept_id(int dept_id2) {
        this.dept_id = dept_id2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public int getParent_id() {
        return this.parent_id;
    }

    public void setParent_id(int parent_id2) {
        this.parent_id = parent_id2;
    }

    public String toString() {
        return "Department{auto_add_user=" + this.auto_add_user + ", create_dept_group=" + this.create_dept_group + ", dept_id=" + this.dept_id + ", name='" + this.name + '\'' + ", parent_id=" + this.parent_id + '}';
    }
}
