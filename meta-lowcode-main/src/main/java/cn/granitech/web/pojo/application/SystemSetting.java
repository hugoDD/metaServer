package cn.granitech.web.pojo.application;

import cn.granitech.util.JsonHelper;
import cn.granitech.variantorm.persistence.license.LicenseInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class SystemSetting {
    private final String version = "1.2.0";
    private String account;
    private String appIntro;
    private String appName;
    private String appSubtitle;
    private String appTitle;
    private Boolean autoBackup;
    private String backupCycle;
    private String backupOverdueDay;
    private String cloudStorage;
    private CloudStorageSetting cloudStorageSetting;
    private String dateFormat;
    private String dateTimeFormat;
    private String dbVersion;
    private String dingTalk;
    private DingTalkSetting dingTalkSetting;
    private String email;
    private EmailSetting emailSetting;
    private String homeURL;
    private String licenseCode;
    private LicenseInfo licenseInfo;
    private String logo;
    private String logoWhite;
    private String offlineSign;
    private String pageFooter;
    private String secretKey;
    private String sms;
    private SMSSetting smsSetting;
    private String sqlVersion;
    private String themeColor;
    private String todoTaskCorn;
    private Boolean trialVersionFlag;
    private Boolean watermark;

    public void cleanObjectInfo() {
        this.smsSetting = null;
        this.emailSetting = null;
        this.cloudStorageSetting = null;
        this.dingTalkSetting = null;
    }

    public String getBackupCycle() {
        return this.backupCycle;
    }

    public void setBackupCycle(String backupCycle2) {
        this.backupCycle = backupCycle2;
    }

    public String getHomeURL() {
        return this.homeURL;
    }

    public void setHomeURL(String homeURL2) {
        this.homeURL = homeURL2;
    }

    public Boolean getAutoBackup() {
        return this.autoBackup != null && this.autoBackup;
    }

    public void setAutoBackup(Boolean autoBackup2) {
        this.autoBackup = autoBackup2;
    }

    public String getBackupOverdueDay() {
        return this.backupOverdueDay;
    }

    public void setBackupOverdueDay(String backupOverdueDay2) {
        this.backupOverdueDay = backupOverdueDay2;
    }

    public String getVersion() {
        return version;
    }

    public String getCloudStorage() {
        return this.cloudStorage;
    }

    public void setCloudStorage(String cloudStorage2) {
        this.cloudStorage = cloudStorage2;
    }

    public String getDingTalk() {
        return this.dingTalk;
    }

    public void setDingTalk(String dingTalk2) {
        this.dingTalk = dingTalk2;
    }

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

    public String getSqlVersion() {
        return this.sqlVersion;
    }

    public void setSqlVersion(String sqlVersion2) {
        this.sqlVersion = sqlVersion2;
    }

    public String getAppTitle() {
        return this.appTitle;
    }

    public void setAppTitle(String appTitle2) {
        this.appTitle = appTitle2;
    }

    public String getAppSubtitle() {
        return this.appSubtitle;
    }

    public void setAppSubtitle(String appSubtitle2) {
        this.appSubtitle = appSubtitle2;
    }

    public String getAppIntro() {
        return this.appIntro;
    }

    public void setAppIntro(String appIntro2) {
        this.appIntro = appIntro2;
    }

    public Boolean getWatermark() {
        return this.watermark;
    }

    public void setWatermark(Boolean watermark2) {
        this.watermark = watermark2;
    }

    public String getThemeColor() {
        return this.themeColor;
    }

    public void setThemeColor(String themeColor2) {
        this.themeColor = themeColor2;
    }

    public String getDbVersion() {
        return this.dbVersion;
    }

    public void setDbVersion(String dbVersion2) {
        this.dbVersion = dbVersion2;
    }

    public String getDateFormat() {
        return this.dateFormat;
    }

    public void setDateFormat(String dateFormat2) {
        this.dateFormat = dateFormat2;
    }

    public String getDateTimeFormat() {
        return this.dateTimeFormat;
    }

    public void setDateTimeFormat(String dateTimeFormat2) {
        this.dateTimeFormat = dateTimeFormat2;
    }

    public String getSms() {
        return this.sms;
    }

    public void setSms(String sms2) {
        this.sms = sms2;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email2) {
        this.email = email2;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String appName2) {
        this.appName = appName2;
    }

    public String getPageFooter() {
        return this.pageFooter;
    }

    public void setPageFooter(String pageFooter2) {
        this.pageFooter = pageFooter2;
    }

    public String getLogo() {
        return this.logo;
    }

    public void setLogo(String logo2) {
        this.logo = logo2;
    }

    public String getLogoWhite() {
        return this.logoWhite;
    }

    public void setLogoWhite(String logoWhite2) {
        this.logoWhite = logoWhite2;
    }

    public Boolean getTrialVersionFlag() {
        return this.trialVersionFlag != null && this.trialVersionFlag;
    }

    public void setTrialVersionFlag(Boolean trialVersionFlag2) {
        this.trialVersionFlag = trialVersionFlag2;
    }

    public SMSSetting getSmsSetting() {
        if (this.smsSetting == null && StringUtils.isNotBlank(this.sms)) {
            this.smsSetting = JsonHelper.readJsonValue(this.sms, SMSSetting.class);
//            SMSSetting) JSON.parseObject(this.sms, SMSSetting.class);
        }
        return this.smsSetting;
    }

    public EmailSetting getEmailSetting() {
        if (this.emailSetting == null && StringUtils.isNotBlank(this.email)) {
            this.emailSetting = JsonHelper.readJsonValue(this.email, EmailSetting.class);
        }
        return this.emailSetting;
    }

    public CloudStorageSetting getCloudStorageSetting() {
        if (this.cloudStorageSetting == null && StringUtils.isNotBlank(this.cloudStorage)) {
            this.cloudStorageSetting = JsonHelper.readJsonValue(this.cloudStorage, CloudStorageSetting.class);
        }
        return this.cloudStorageSetting;
    }

    public DingTalkSetting getDingTalkSetting() {
        if (this.dingTalkSetting == null && StringUtils.isNotBlank(this.dingTalk)) {
            this.dingTalkSetting = JsonHelper.readJsonValue(this.dingTalk, DingTalkSetting.class);
        }
        return this.dingTalkSetting;
    }

    public void setDingTalkSetting(DingTalkSetting dingTalkSetting2) {
        this.dingTalkSetting = dingTalkSetting2;
    }

    public String getOfflineSign() {
        return this.offlineSign;
    }

    public void setOfflineSign(String offlineSign2) {
        this.offlineSign = offlineSign2;
    }

    public LicenseInfo getLicenseInfo() {
        return this.licenseInfo;
    }

    public void setLicenseInfo(LicenseInfo licenseInfo2) {
        this.licenseInfo = licenseInfo2;
    }

    public String getTodoTaskCorn() {
        return this.todoTaskCorn;
    }

    public void setTodoTaskCorn(String todoTaskCorn2) {
        this.todoTaskCorn = todoTaskCorn2;
    }
}
