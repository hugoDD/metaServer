package cn.granitech.trigger.business.trigger.action;

import java.util.List;


public class SendMsg {
    public static int TYPE_NOTE = 2;
    public static int TYPE_PHONE = 4;
    public static int TYPE_EMAIL = 8;
    public static int TYPE_DINGDING = 16;


    public static int INSIDER = 1;
    public static int OUTSIDERS = 2;
    public static int DINGROBOT = 3;


    private List<String> sendTo;


    private int type;


    private int userType;


    private String title;


    private String content;


    private String dingdingRobotUrl;


    private String dingdingSign;


    public List<String> getSendTo() {
        return this.sendTo;
    }

    public void setSendTo(List<String> sendTo) {
        this.sendTo = sendTo;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUserType() {
        return this.userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDingdingRobotUrl() {
        return this.dingdingRobotUrl;
    }

    public void setDingdingRobotUrl(String dingdingRobotUrl) {
        this.dingdingRobotUrl = dingdingRobotUrl;
    }

    public String getDingdingSign() {
        return this.dingdingSign;
    }

    public void setDingdingSign(String dingdingSign) {
        this.dingdingSign = dingdingSign;
    }
}



