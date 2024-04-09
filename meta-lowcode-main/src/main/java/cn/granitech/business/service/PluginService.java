package cn.granitech.business.service;

import cn.granitech.business.plugins.trigger.TriggerService;

import cn.granitech.trigger.business.service.TriggerServiceImpl;
import cn.granitech.util.SpringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PluginService {
//    @Resource
//    private PluginUser pluginUser;
//    @Resource
//    private PluginOperator pluginOperator;

    @Autowired
    public PluginService() {
    }

    public List<String> getPluginInfo() {
//        List<PluginInfo> pluginInfoList = this.pluginOperator.getPluginInfo();
//        return pluginInfoList.stream()
//                .filter((pluginInfo) -> pluginInfo.getPluginState().equals("STARTED"))
//                .map(PluginInfo::getPluginId).collect(Collectors.toList());
        return Arrays.asList("metaDataCube","mannerReport","metaTrigger");
    }

    public TriggerService getTriggerService() {
        return SpringHelper.getBean(TriggerServiceImpl.class);//(TriggerService) this.pluginUser.getBean("metaTrigger", "triggerServiceImpl");
    }
}
