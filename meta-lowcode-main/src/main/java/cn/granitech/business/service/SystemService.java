package cn.granitech.business.service;

import cn.granitech.aop.annotation.SystemRight;
import cn.granitech.exception.ServiceException;
import cn.granitech.util.JsonHelper;
import cn.granitech.util.ObjectHelper;
import cn.granitech.variantorm.exception.LicenseException;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.persistence.license.LicenseInfo;
import cn.granitech.web.enumration.ProductTypeEnum;
import cn.granitech.web.enumration.SystemRightEnum;
import cn.granitech.web.pojo.RegisterLicense;
import cn.granitech.web.pojo.application.ServiceInfo;
import cn.granitech.web.pojo.application.SystemSetting;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class SystemService extends BaseService {
    public static final String DEFAULT_VALUE = "defaultValue";
    public static final String LICENSE_CODE_KEY = "licenseCode";
    public static final String[] LICENSE_VALUES = {"account", "secretKey", LICENSE_CODE_KEY, "offlineSign"};
    public static final String SETTING_NAME = "settingName";
    public static final String SETTING_VALUE = "settingValue";
    public static final String[] STATUS_VALUES = {"watermark", "trialVersionFlag", "autoBackup"};
    public static final String SYSTEM_SETTING_ID = "systemSettingId";
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final Map<String, EntityRecord> settingCache = new LinkedHashMap<>();
    @Resource
    ServiceInfo serviceInfo;
    @Resource
    SystemSetting systemSetting;

    public void updateSysSetting(SystemSetting systemSetting2) {
        Map<String, Object> userUpdateMap = JsonHelper.writeObjectAsMap(systemSetting2);
        updateInternalSetting(userUpdateMap, InternalObject.EMAIL, systemSetting2.getEmailSetting());
        updateInternalSetting(userUpdateMap, InternalObject.SMS, systemSetting2.getSmsSetting());
        updateInternalSetting(userUpdateMap, InternalObject.CLOUD_STORAGE, systemSetting2.getCloudStorageSetting());
        updateInternalSetting(userUpdateMap, InternalObject.DING_TALK, systemSetting2.getDingTalkSetting());
        for (String key : userUpdateMap.keySet()) {
            EntityRecord entityRecord = this.settingCache.get(key);
            if (entityRecord != null) {
                entityRecord.setFieldValue(SETTING_VALUE, userUpdateMap.get(key).toString());
                updateRecord(entityRecord);
            }
        }
        loadSystemSetting();
    }

    private void updateInternalSetting(Map<String, Object> userUpdateMap, String key, Object setting) {
        if (setting != null) {
            userUpdateMap.put(key, JsonHelper.writeObjectAsString(setting));
        }
    }

    public void loadSystemSetting() {
        this.settingCache.clear();
        this.systemSetting.cleanObjectInfo();
        List<EntityRecord> settingList = queryListRecord("SystemSetting", null, null, null, null, SYSTEM_SETTING_ID, SETTING_NAME, "settingValue", DEFAULT_VALUE);
        Map<String, Object> settingMap = new HashMap<>();
        for (EntityRecord entityRecord : settingList) {
            String name = entityRecord.getFieldValue(SETTING_NAME);
            String setValue = entityRecord.getFieldValue("settingValue");
            if (Arrays.asList(STATUS_VALUES).contains(name)) {
                settingMap.put(name, Boolean.parseBoolean(setValue));
            } else {
                settingMap.put(name, setValue);
            }
            this.settingCache.put(name, entityRecord);
        }
        ObjectHelper.mapToObj(settingMap, this.systemSetting);
        cleanLowProductInfo();
    }

    private void cleanLowProductInfo() {
        LicenseInfo licenseInfo = this.systemSetting.getLicenseInfo();
        if (licenseInfo != null) {
            licenseInfo.setProductType("旗舰版");
            ProductTypeEnum.getTypeByName(licenseInfo.getProductType()).cleanLowProductInfo(this.systemSetting);
        }
    }

    @SystemRight(SystemRightEnum.ADMIN_ROLE)
    public LicenseInfo registerLicense(RegisterLicense registerLicense) {
        String account = StrUtil.trim(registerLicense.getAccount());
        String secretKey = StrUtil.trim(registerLicense.getSecretKey());
        String licenseCode = StrUtil.trim(registerLicense.getLicenseCode());
        String offlineSign = StrUtil.trim(registerLicense.getOfflineSign());
        try {
            LicenseInfo licenseInfo = this.pm.validateLicense(this.systemSetting.getAppName(), account, secretKey, licenseCode, this.serviceInfo.getPort(), offlineSign);
            this.systemSetting.setLicenseInfo(licenseInfo);
            SystemSetting systemSetting2 = new SystemSetting();
            if (StrUtil.isNotBlank(registerLicense.getOfflineSign())) {
                systemSetting2.setOfflineSign(offlineSign);
            } else {
                systemSetting2.setAccount(account);
                systemSetting2.setSecretKey(secretKey);
                systemSetting2.setLicenseCode(licenseCode);
                systemSetting2.setOfflineSign(null);
            }
            if (!this.settingCache.containsKey(LICENSE_CODE_KEY)) {
                for (String licenseValue : LICENSE_VALUES) {
                    EntityRecord entityRecord = newRecord(7);
                    entityRecord.setFieldValue(SETTING_NAME, licenseValue);
                    createRecord(entityRecord);
                    this.settingCache.put(licenseValue, entityRecord);
                }
            }
            updateSysSetting(systemSetting2);
            return licenseInfo;
        } catch (LicenseException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public LicenseInfo getLicenseInfo() {
        return this.pm.getLicenseInfo();
    }

    public LicenseInfo license() {
        this.log.info("【Account：{},SecretKey：{},LicenseCode：{},Port：{},OfflineSign:{}】", this.systemSetting.getAccount(), this.systemSetting.getSecretKey(), this.systemSetting.getLicenseCode(), Integer.valueOf(this.serviceInfo.getPort()), this.systemSetting.getOfflineSign());
        LicenseInfo licenseInfo = this.pm.validateLicense(this.systemSetting.getAppName(), this.systemSetting.getAccount(), this.systemSetting.getSecretKey(), this.systemSetting.getLicenseCode(), Integer.valueOf(this.serviceInfo.getPort()), this.systemSetting.getOfflineSign());
        this.log.info("设备信息:\n{}", licenseInfo.getLicenseDetail());
        this.systemSetting.setLicenseInfo(licenseInfo);
        cleanLowProductInfo();
        return licenseInfo;
    }

    public static class InternalObject {
        public static final String CLOUD_STORAGE = "cloudStorage";
        public static final String DING_TALK = "dingTalk";
        public static final String EMAIL = "email";
        public static final String SMS = "sms";
    }
}
