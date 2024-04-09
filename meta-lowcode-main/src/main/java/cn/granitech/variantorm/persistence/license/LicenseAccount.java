package cn.granitech.variantorm.persistence.license;


import cn.granitech.variantorm.util.MD5Helper;

public class LicenseAccount {
    private String licenseCode;
    private String deviceNo;
    private String deviceInfo;
    private String account;
    private Long timestamp;
    private static final String salt = "metaCode_js";
    private String sign;
    private Integer port;

    public void setAccount(String account) {
        this.account = account;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getAccount() {
        return this.account;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }


    public String getSeverSign(String secretKey) {

        String loginPwd = this.timestamp +
                this.deviceNo +
                this.port +
                this.account +
                secretKey;
        return MD5Helper.md5HexForSalt(loginPwd, salt);
    }

    public String getDeviceNo() {
        return this.deviceNo;
    }

    public String getSign() {
        return this.sign;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getPort() {
        return this.port;
    }


    public LicenseAccount(String account, Integer port, String licenseCode, String deviceNo, Long timestamp, String deviceInfo) {
        this.account = account;
        this.port = port;
        this.licenseCode = licenseCode;
        this.deviceNo = deviceNo;
        this.timestamp = timestamp;
        this.deviceInfo = deviceInfo;
    }

    public String getDeviceInfo() {
        return this.deviceInfo;
    }


    public static String getOfflineSign(String secretKey, String offlineDeviceCode) {
        String loginPwd = offlineDeviceCode + "OfflineSign" +secretKey;
        return MD5Helper.md5HexForSalt(loginPwd, salt);
    }

    /*
     * WARNING - void declaration
     */
    public String getClientSign(String secretKey) {
        String loginPwd = this.account + secretKey +
                this.port +
                this.deviceNo +
                this.timestamp;
        return MD5Helper.md5HexForSalt(loginPwd, salt);
    }

    public String getLicenseCode() {
        return this.licenseCode;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public LicenseAccount() {
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public boolean checkOfflineSign(String secretKey, String offlineSign) {
        return offlineSign.equals(LicenseAccount.getOfflineSign(secretKey, this.deviceNo));
    }

    public void setLicenseCode(String licenseCode) {
        this.licenseCode = licenseCode;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
