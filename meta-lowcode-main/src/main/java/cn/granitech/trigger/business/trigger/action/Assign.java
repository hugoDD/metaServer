package cn.granitech.trigger.business.trigger.action;

import cn.granitech.web.pojo.Cascade;

import java.util.List;


public class Assign {
    public static final int ASSIGN_RULE_POLLING = 1;
    public static final int ASSIGN_RULE_RANDOM = 2;
    public static final int ASSIGN_TYPE_FOLLOW = 1;
    public static final int ASSIGN_TYPE_CUSTOMIZE = 2;
    private int assignType;
    private List<Cascade> cascades;
    private int assignRule;
    private List<String> assignTo;

    public int getAssignType() {
        return this.assignType;
    }

    public void setAssignType(int assignType) {
        this.assignType = assignType;
    }

    public List<Cascade> getCascades() {
        return this.cascades;
    }

    public void setCascades(List<Cascade> cascades) {
        this.cascades = cascades;
    }

    public int getAssignRule() {
        return this.assignRule;
    }

    public void setAssignRule(int assignRule) {
        this.assignRule = assignRule;
    }

    public List<String> getAssignTo() {
        return this.assignTo;
    }

    public void setAssignTo(List<String> assignTo) {
        this.assignTo = assignTo;
    }
}



