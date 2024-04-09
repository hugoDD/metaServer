package cn.granitech.web.controller;

import cn.granitech.aop.annotation.SystemRight;
import cn.granitech.business.service.RecycleBinService;
import cn.granitech.util.ResponseHelper;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.web.enumration.SystemRightEnum;
import cn.granitech.web.pojo.ListQueryRequestBody;
import cn.granitech.web.pojo.ListQueryResult;
import cn.granitech.web.pojo.ResponseBean;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RequestMapping({"/rb"})
@RestController
public class RecycleBinController {
    @Resource
    RecycleBinService recycleBinService;

    @RequestMapping({"/restore"})
    @SystemRight(SystemRightEnum.RECYCLE_BIN_MANAGE)
    public ResponseBean restore(@RequestParam("id") String recordId) {
        this.recycleBinService.restore(ID.valueOf(recordId));
        return ResponseHelper.ok("success");
    }

    @RequestMapping({"/listQuery"})
    @SystemRight(SystemRightEnum.RECYCLE_BIN_MANAGE)
    public ResponseBean<ListQueryResult> queryListMap(@RequestBody ListQueryRequestBody requestBody) {
        requestBody.setMainEntity("RecycleBin");
        return new ResponseBean<>(200, null, "success", this.recycleBinService.queryListMap(requestBody));
    }
}
