package cn.granitech.variantorm.persistence.license;


public class LicenseInfo {
    private String deviceNo;
    private Integer entityLimit;
    private String licenseDetail;
    private String account;
    private String serialNumber;
    private String productType;
    private String companyName;

    public Integer getEntityLimit() {
        return this.entityLimit;
    }

    public String getAccount() {
        return this.account;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /*
     * WARNING - void declaration
     */
    public static LicenseInfo buildCommercialLicense(String serialNumber, String companyName, String account, String productType, Integer entityLimit, String deviceNo) {
        LicenseInfo licenseInfo = new LicenseInfo();

        licenseInfo.setSerialNumber(serialNumber);
        licenseInfo.setCompanyName(companyName);
        licenseInfo.setAccount(account);
        licenseInfo.setProductType(productType);
        licenseInfo.setEntityLimit(entityLimit);
        licenseInfo.setDeviceNo(deviceNo);
        String licenseInfoStr = "*****************************************************" +
                System.lineSeparator() +
                "授权码：" + serialNumber + System.lineSeparator() +
                companyName + System.lineSeparator() +
                "账号：" + account + System.lineSeparator() +
                "设备号：" + deviceNo + System.lineSeparator() +
                "版本：" + productType + System.lineSeparator() +
                "用户定义实体上限：" + (entityLimit == null ? "99,999" : entityLimit) + System.lineSeparator() +
                "*****************************************************";
        licenseInfo.setLicenseDetail(licenseInfoStr);
        return licenseInfo;
    }

    public String getSerialNumber() {
        return this.serialNumber;
    }

    public static LicenseInfo buildFreeLicense(String deviceNo) {
        LicenseInfo licenseInfo  = new LicenseInfo();
        licenseInfo.setSerialNumber("00000000000000000000000000000000");
        licenseInfo.setCompanyName("上海极昇数科数据技术有限公司");
        licenseInfo.setAccount("00008");
        licenseInfo.setProductType("免费版");
        licenseInfo.setEntityLimit(30);
        licenseInfo.setDeviceNo(deviceNo);
        String sb = "*****************************************************" + System.lineSeparator() +
                "授权码：00000000000000000000000000000000" + System.lineSeparator() +
                "上海极昇数科数据技术有限公司" + System.lineSeparator() +
                "账号：00008" + System.lineSeparator() +
                "设备号：" + deviceNo + System.lineSeparator() +
                "版本：免费版" + System.lineSeparator() +
                "用户定义实体上限：30" + System.lineSeparator() +
                "*****************************************************";
        licenseInfo.setLicenseDetail(sb);
        return licenseInfo;
    }

    public void setLicenseDetail(String licenseDetail) {
        this.licenseDetail = licenseDetail;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public String getLicenseDetail() {
        return this.licenseDetail;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setEntityLimit(Integer entityLimit) {
        this.entityLimit = entityLimit;
    }

    public String getDeviceNo() {
        return this.deviceNo;
    }

    public String getProductType() {
        return this.productType;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
}
