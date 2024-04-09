package cn.granitech.web.controller;

import cn.granitech.aop.annotation.SystemRight;
import cn.granitech.business.service.RevisionHistoryService;
import cn.granitech.util.ResponseHelper;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.web.enumration.SystemRightEnum;
import cn.granitech.web.pojo.ListQueryRequestBody;
import cn.granitech.web.pojo.ListQueryResult;
import cn.granitech.web.pojo.ResponseBean;
import cn.granitech.web.pojo.vo.RevisionHistoryContentVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RequestMapping({"/revisionHistory"})
@RestController
public class RevisionHistoryController {
    @Resource
    RevisionHistoryService revisionHistoryService;

    @GetMapping({"/detailsById"})
    @SystemRight(SystemRightEnum.RECYCLE_HISTORY_MANAGE)
    public ResponseBean<List<RevisionHistoryContentVO>> detailsById(@RequestParam("revisionHistoryId") ID revisionHistoryId) {
        return ResponseHelper.ok(this.revisionHistoryService.detailsById(revisionHistoryId));
    }

    @RequestMapping({"/listQuery"})
    @SystemRight(SystemRightEnum.RECYCLE_HISTORY_MANAGE)
    public ResponseBean<ListQueryResult> queryListMap(@RequestBody ListQueryRequestBody requestBody) {
        requestBody.setMainEntity("RevisionHistory");
        return new ResponseBean<>(200, null, "success", this.revisionHistoryService.queryListMap(requestBody));
    }
}
