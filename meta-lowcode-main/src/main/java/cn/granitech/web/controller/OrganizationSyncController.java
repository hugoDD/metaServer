package cn.granitech.web.controller;

import cn.granitech.business.task.TaskExecutors;
import cn.granitech.integration.dingtalk.DingTalkOrganizationSync;
import cn.granitech.integration.dingtalk.DingTalkSdk;
import cn.granitech.util.ResponseHelper;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.web.pojo.ResponseBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping({"/organizationSync"})
@RestController
public class OrganizationSyncController {
    @GetMapping({"/dingtalkSyncUser"})
    public ResponseBean dingtalkSyncUser(@RequestParam(name = "defaultRole", required = false) ID defaultRole) {
        DingTalkSdk.getDingTalkConfig();
        return ResponseHelper.ok(TaskExecutors.submit(new DingTalkOrganizationSync(defaultRole), ID.valueOf("0000021-00000000000000000000000000000001")));
    }
}
