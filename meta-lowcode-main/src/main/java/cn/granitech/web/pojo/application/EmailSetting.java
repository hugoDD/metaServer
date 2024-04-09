package cn.granitech.web.pojo.application;

import java.io.Serializable;

public class EmailSetting implements Serializable {
    private String appId;
    private String appKey;
    private String cc;
    private String from;
    private String fromName;
    private boolean openStatus;

    public boolean getOpenStatus() {
        return this.openStatus;
    }

    public void setOpenStatus(boolean openStatus2) {
        this.openStatus = openStatus2;
    }

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String appId2) {
        this.appId = appId2;
    }

    public String getAppKey() {
        return this.appKey;
    }

    public void setAppKey(String appKey2) {
        this.appKey = appKey2;
    }

    public String getFrom() {
        return this.from;
    }

    public void setFrom(String from2) {
        this.from = from2;
    }

    public String getFromName() {
        return this.fromName;
    }

    public void setFromName(String fromName2) {
        this.fromName = fromName2;
    }

    public String getCc() {
        return this.cc;
    }

    public void setCc(String cc2) {
        this.cc = cc2;
    }
}
