package cn.granitech.web.controller;

import cn.granitech.aop.annotation.SystemRight;
import cn.granitech.business.service.PluginService;
import cn.granitech.business.service.SystemService;
import cn.granitech.exception.ServiceException;
import cn.granitech.util.JsonHelper;
import cn.granitech.util.ResponseHelper;
import cn.granitech.variantorm.persistence.license.LicenseInfo;
import cn.granitech.web.enumration.ProductTypeEnum;
import cn.granitech.web.enumration.SystemRightEnum;
import cn.granitech.web.pojo.RegisterLicense;
import cn.granitech.web.pojo.ResponseBean;
import cn.granitech.web.pojo.application.SystemSetting;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RequestMapping({"/setting"})
@RestController
public class SystemSettingController {
    private final String[] publicSetting = {"trialVersionFlag", "appName", "pageFooter", "logo", "logoWhite", "themeColor", "watermark", "appTitle", "appSubtitle", "appIntro"};
    @Resource
    PluginService pluginService;
    @Resource
    SystemSetting setting;
    @Resource
    SystemService systemSettingService;

//    @GetMapping({"/getLicenseInfo"})
//    public ResponseBean<LicenseInfo> getLicenseInfo() {
//        return ResponseHelper.ok(this.systemSettingService.getLicenseInfo());
//    }

    @PostMapping({"/registerLicense"})
    public ResponseBean<LicenseInfo> registerLicense(@RequestBody RegisterLicense registerLicense) {
        if (!this.setting.getTrialVersionFlag()) {
            return ResponseHelper.ok(this.systemSettingService.registerLicense(registerLicense));
        }
        throw new ServiceException("当前为演示版本，功能暂不开放！");
    }

    @RequestMapping({"/info"})
    @SystemRight(SystemRightEnum.ADMIN_ROLE)
    public ResponseBean<SystemSetting> info() {
        return ResponseHelper.ok(this.setting);
    }

    @RequestMapping({"/updateSysSetting"})
    @SystemRight(SystemRightEnum.ADMIN_ROLE)
    public ResponseBean<?> updateSysSetting(@RequestBody SystemSetting systemSetting) {
        if (this.setting.getTrialVersionFlag()) {
            throw new ServiceException("当前为演示版本，功能暂不开放！");
        }
        this.systemSettingService.updateSysSetting(systemSetting);
        return ResponseHelper.ok();
    }

    @RequestMapping({"/queryPublicSetting"})
    public ResponseBean<Map<String, Object>> queryPublicSetting() {
        Map<String, Object> map = JsonHelper.writeObjectAsMap(this.setting);
        Map<String, Object> resultMap = new HashMap<>();
        for (String key : this.publicSetting) {
            resultMap.put(key, map.get(key));
        }
        resultMap.put("pluginIdList", this.pluginService.getPluginInfo());
        resultMap.put("productType", ProductTypeEnum.ULTIMATE);
        return ResponseHelper.ok(resultMap);
    }

    @RequestMapping({"/loadSystemSetting"})
    @SystemRight(SystemRightEnum.ADMIN_ROLE)
    public ResponseBean<?> loadSystemSetting() {
        this.systemSettingService.loadSystemSetting();
        return ResponseHelper.ok();
    }
}
