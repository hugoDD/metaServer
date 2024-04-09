package cn.granitech.trigger.business.trigger.action;


public class CallBack {
    public static final String CALLBACK_URL = "URL";
    public static final String CALLBACK_FUNCTION = "FUNCTION";
    private String callBackType;
    private boolean pushAllData;
    private String hookUrl;
    private String hookSecret;
    private boolean forceSync;
    private String functionName;

    public String getCallBackType() {
        return this.callBackType;
    }

    public void setCallBackType(String callBackType) {
        this.callBackType = callBackType;
    }

    public String getFunctionName() {
        return this.functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getHookUrl() {
        return this.hookUrl;
    }

    public void setHookUrl(String hookUrl) {
        this.hookUrl = hookUrl;
    }

    public String getHookSecret() {
        return this.hookSecret;
    }

    public void setHookSecret(String hookSecret) {
        this.hookSecret = hookSecret;
    }

    public boolean isForceSync() {
        return this.forceSync;
    }

    public void setForceSync(boolean forceSync) {
        this.forceSync = forceSync;
    }

    public boolean isPushAllData() {
        return this.pushAllData;
    }

    public void setPushAllData(boolean pushAllData) {
        this.pushAllData = pushAllData;
    }
}



