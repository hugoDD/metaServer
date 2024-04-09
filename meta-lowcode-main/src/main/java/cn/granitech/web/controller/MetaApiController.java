package cn.granitech.web.controller;

import cn.granitech.aop.annotation.VisitLimit;
import cn.granitech.web.pojo.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RequestMapping({"/metaApi"})
@RestController
public class MetaApiController extends BaseController {
    @Resource
    CrudController crudController;

    @VisitLimit(limit = 1, sec = 30)
    @RequestMapping({"/listQuery"})
    public ResponseBean<ListQueryResult> queryListMap(@RequestBody ListQueryRequestBody requestBody) {
        return this.crudController.queryListMap(requestBody);
    }

    @VisitLimit(limit = 1, sec = 5)
    @RequestMapping({"/queryById"})
    public ResponseBean<Map<String, Object>> queryById(String entityId, String fieldNames) {
        return this.crudController.queryById(entityId, fieldNames);
    }

    @VisitLimit(limit = 1, sec = 5)
    @RequestMapping({"/saveRecord"})
    public ResponseBean<FormQueryResult> saveRecord(@RequestParam("entity") String entityName, @RequestParam("id") String recordId, @RequestBody Map<String, Object> dataMap) {
        return this.crudController.saveRecord(entityName, recordId, dataMap);
    }

    @VisitLimit(limit = 1, sec = 5)
    @RequestMapping({"/deleteRecord"})
    public ResponseBean deleteRecord(@RequestBody BatchOperation param) {
        return this.crudController.deleteRecord(param);
    }
}
