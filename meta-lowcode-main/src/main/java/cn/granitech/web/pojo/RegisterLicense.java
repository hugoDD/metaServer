package cn.granitech.web.pojo;

public class RegisterLicense {
    private String account;
    private String licenseCode;
    private String offlineSign;
    private String secretKey;

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account2) {
        this.account = account2;
    }

    public String getSecretKey() {
        return this.secretKey;
    }

    public void setSecretKey(String secretKey2) {
        this.secretKey = secretKey2;
    }

    public String getLicenseCode() {
        return this.licenseCode;
    }

    public void setLicenseCode(String licenseCode2) {
        this.licenseCode = licenseCode2;
    }

    public String getOfflineSign() {
        return this.offlineSign;
    }

    public void setOfflineSign(String offlineSign2) {
        this.offlineSign = offlineSign2;
    }
}
