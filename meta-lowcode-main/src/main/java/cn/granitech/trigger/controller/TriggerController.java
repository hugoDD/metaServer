package cn.granitech.trigger.controller;

import cn.granitech.aop.annotation.SystemRight;
import cn.granitech.business.plugins.trigger.FunctionHelper;
import cn.granitech.business.service.EntityManagerService;
import cn.granitech.trigger.business.service.TriggerServiceImpl;
import cn.granitech.trigger.business.trigger.action.CallBackTrigger;
import cn.granitech.trigger.business.trigger.aviator.AviatorUtils;
import cn.granitech.util.JsonHelper;
import cn.granitech.util.ResponseHelper;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.pojo.IDName;
import cn.granitech.web.enumration.SystemRightEnum;
import cn.granitech.web.pojo.FormQueryResult;
import cn.granitech.web.pojo.ListQueryRequestBody;
import cn.granitech.web.pojo.ListQueryResult;
import cn.granitech.web.pojo.ResponseBean;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static cn.granitech.variantorm.constant.SystemEntities.TriggerLog;


@RestController
@RequestMapping({"/plugins/metaTrigger/trigger"})
public class TriggerController {
    @Resource
    TriggerServiceImpl triggerServiceImpl;
    @Resource
    PersistenceManager pm;
    @Resource
    EntityManagerService entityManagerService;
    @Resource
    CallBackTrigger callBackTrigger;

    @RequestMapping({"/save"})
    public ResponseBean<FormQueryResult> save(@RequestParam("id") String recordId, @RequestBody Map<String, Object> dataMap) {
        FormQueryResult formQueryResult = this.triggerServiceImpl.save(recordId, dataMap);
        return ResponseHelper.ok(formQueryResult, "success");
    }


    @RequestMapping({"/executeTrigger"})
    public ResponseBean<FormQueryResult> executeTrigger(@RequestBody Map<String, Object> dataMap) {
        this.triggerServiceImpl.executeTrigger((String) dataMap.get("triggerConfigId"), (Integer) dataMap.get("entityCode"), (String) dataMap.get("actionFilter"), (String) dataMap.get("actionContent"), (Integer) dataMap.get("actionType"), null);
        return ResponseHelper.ok();
    }


    @RequestMapping({"/log"})
    @SystemRight(SystemRightEnum.TRIGGER_LOG_MANAGE)
    public ResponseBean<ListQueryResult> queryListMap(@RequestBody ListQueryRequestBody requestBody) {
        requestBody.setMainEntity(TriggerLog);
        return new ResponseBean<>(200, null, "success", this.triggerServiceImpl.queryListMap(requestBody));
    }


    @RequestMapping({"/callBackTest"})
    public ResponseBean<String> callBackTest(@RequestBody String actionContent) {
        String trigger = this.callBackTrigger.callBack(null, actionContent);
        return ResponseHelper.ok(trigger);
    }


    @RequestMapping({"/queryFunctionList"})
    public ResponseBean<Set<String>> queryFunctionList() {
        Set<String> functionSet = FunctionHelper.getFunctionMap().keySet();
        return ResponseHelper.ok(functionSet);
    }


    @RequestMapping({"/aggregation/entityList"})
    public ResponseBean<List<Map<String, Object>>> queryEntityListOfAggregation(@RequestParam("entityCode") int entityCode) {
        List<Map<String, Object>> resultList = this.entityManagerService.queryEntityList(entityCode, false, true, false, false, false);

        return ResponseHelper.ok(resultList, "success");
    }


    @RequestMapping({"/dataUpdate/entityList"})
    public ResponseBean<List<Map<String, Object>>> queryEntityListOfDataUpdate(@RequestParam("entityCode") int entityCode) {
        List<Map<String, Object>> resultList = this.entityManagerService.queryEntityList(entityCode, true, true, true, false, false);
        return ResponseHelper.ok(resultList, "success");
    }


    @RequestMapping({"/dataAutoCreate/entityList"})
    public ResponseBean<List<Map<String, Object>>> queryEntityListOfDataAutoCreate(@RequestParam("entityCode") int entityCode) {
        List<Map<String, Object>> resultList = this.entityManagerService.queryEntityList(entityCode, false, true, false, false, false);
        return ResponseHelper.ok(resultList, "success");
    }


    @RequestMapping({"/dataDelete/entityList"})
    public ResponseBean<List<Map<String, Object>>> queryEntityListOfDataDelete(@RequestParam("entityCode") int entityCode) {
        List<Map<String, Object>> resultList = this.entityManagerService.queryEntityList(entityCode, true, true, true, false, false);
        return ResponseHelper.ok(resultList, "success");
    }


    @RequestMapping({"/dataRevoke/entityList"})
    public ResponseBean<List<Map<String, Object>>> queryEntityListOfDataRevoke(@RequestParam("entityCode") int entityCode) {
        List<Map<String, Object>> resultList = this.entityManagerService.queryEntityList(entityCode, true, true, true, false, false);
        return ResponseHelper.ok(resultList, "success");
    }


    @RequestMapping({"/assign/entityList"})
    public ResponseBean<List<Map<String, Object>>> queryEntityListOfAssign(@RequestParam("entityCode") int entityCode) {
        List<Map<String, Object>> resultList = this.entityManagerService.queryEntityList(entityCode, false, false, true, false, false);
        return ResponseHelper.ok(resultList, "success");
    }


    @RequestMapping({"/aviator/validate"})
    public ResponseBean<String> aviatorValidate(@RequestBody Map<String, String> dataMap) {
        String result = AviatorUtils.validate(dataMap.get("expression"));
        return ResponseHelper.ok(result, "success");
    }


    @RequestMapping({"/idToIdName"})
    public ResponseBean<List<IDName>> idToIdName(@RequestBody List<String> idList) {
        List<IDName> resultList = new ArrayList<>();
        for (String id : idList) {
            IDName idName = this.pm.getQueryCache().getIDName(id);
            resultList.add(idName);
        }
        return ResponseHelper.ok(resultList, "success");
    }


    @RequestMapping({"/callBack"})
    public ResponseBean<?> callBack(HttpServletRequest request) {
        System.out.println("===================================in callBack===========================================");
        System.out.println(request.getHeaderNames());
        System.out.println(request.getHeader("HOOK_SECRET"));
        System.out.println(JsonHelper.writeObjectAsString(request.getParameterMap()));
        System.out.println("===================================callBack end===========================================");
        return ResponseHelper.ok("success");
    }
}



