package cn.granitech.web.controller;

import cn.granitech.business.service.NotificationService;
import cn.granitech.interceptor.CallerContext;
import cn.granitech.util.FilterHelper;
import cn.granitech.util.ResponseHelper;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.pojo.IDName;
import cn.granitech.web.pojo.ListQueryRequestBody;
import cn.granitech.web.pojo.ListQueryResult;
import cn.granitech.web.pojo.ResponseBean;
import cn.granitech.web.pojo.filter.Filter;
import cn.granitech.web.pojo.filter.FilterItem;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping({"/note"})
@RestController
public class NotificationController {
    @Resource
    CallerContext callerContext;
    @Resource
    NotificationService notificationService;
    @Resource
    PersistenceManager pm;

    @RequestMapping({"/listQuery"})
    public ResponseBean<ListQueryResult> queryListMap(@RequestBody ListQueryRequestBody requestBody) {
        requestBody.setMainEntity("Notification");
        List<FilterItem> filterItemList = new ArrayList<>();
        filterItemList.add(new FilterItem("toUser", "EQ", this.callerContext.getCallerId()));
        requestBody.setAdvFilter(new Filter(FilterHelper.AND, filterItemList));
        ListQueryResult listQueryResult = this.notificationService.queryListMap(requestBody);
        setEntityName(listQueryResult.getDataList());
        return new ResponseBean<>(200, null, "success", listQueryResult);
    }

    private void setEntityName(List<Map<String, Object>> list) {
        for (Map<String, Object> map : list) {
            map.put("entityName", this.pm.getMetadataManager().getEntity(((IDName) map.get("relatedRecord")).getId().getEntityCode()).getName());
        }
    }

    @GetMapping({"/query"})
    public ResponseBean<List<Map<String, Object>>> queryNotification(@RequestParam(required = false) Boolean unread) {
        List<Map<String, Object>> maps = this.notificationService.queryNotification(unread);
        setEntityName(maps);
        return ResponseHelper.ok(maps);
    }

    @GetMapping({"/queryCount"})
    public ResponseBean<Integer> queryNoteCount() {
        return ResponseHelper.ok(Integer.valueOf(this.notificationService.queryNoteCount()));
    }

    @RequestMapping({"/read"})
    public ResponseBean read(@RequestParam("id") String notificationId) {
        this.notificationService.read(notificationId);
        return ResponseHelper.ok();
    }

    @RequestMapping({"/readAll"})
    public ResponseBean readAll() {
        this.notificationService.readAll();
        return ResponseHelper.ok();
    }

    @RequestMapping({"/querySendState"})
    public ResponseBean<Map<String, Boolean>> querySendState() {
        Map<String, Boolean> resultMap = new HashMap<>();
        resultMap.put("smsState", Boolean.valueOf(this.notificationService.checkSMSState()));
        resultMap.put("emailState", Boolean.valueOf(this.notificationService.checkEmailState()));
        resultMap.put("dingState", Boolean.valueOf(this.notificationService.checkDingState()));
        return ResponseHelper.ok(resultMap);
    }
}
