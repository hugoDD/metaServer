package cn.granitech.web.pojo.application;

public class CloudStorageSetting {
    private String accessKey;
    private String bucket;
    private String host;
    private boolean openStatus;
    private String secretKey;

    public boolean isOpenStatus() {
        return this.openStatus;
    }

    public void setOpenStatus(boolean openStatus2) {
        this.openStatus = openStatus2;
    }

    public String getAccessKey() {
        return this.accessKey;
    }

    public void setAccessKey(String accessKey2) {
        this.accessKey = accessKey2;
    }

    public String getSecretKey() {
        return this.secretKey;
    }

    public void setSecretKey(String secretKey2) {
        this.secretKey = secretKey2;
    }

    public String getBucket() {
        return this.bucket;
    }

    public void setBucket(String bucket2) {
        this.bucket = bucket2;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host2) {
        this.host = host2;
    }
}
