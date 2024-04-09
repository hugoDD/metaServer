package cn.granitech.web.pojo.application;

import java.io.Serializable;

public class SMSSetting implements Serializable {
    private String appId;
    private String appKey;
    private boolean openStatus;
    private String signature;

    public boolean getOpenStatus() {
        return this.openStatus;
    }

    public void setOpenStatus(boolean openStatus2) {
        this.openStatus = openStatus2;
    }

    public String getSignature() {
        return this.signature;
    }

    public void setSignature(String signature2) {
        this.signature = signature2;
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
}
