package cn.granitech.interceptor;

import cn.granitech.web.controller.CrudController;
import cn.granitech.web.pojo.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RequestMapping({"/meta/api"})
@RestController
@CrossOrigin
public class MetaServerApiController {
    @Resource
    CrudController crudController;

    @RequestMapping({"/listQuery"})
    public ResponseBean<ListQueryResult> queryListMap(@RequestBody ListQueryRequestBody requestBody) {
        return this.crudController.queryListMap(requestBody);
    }

    @RequestMapping({"/queryById"})
    public ResponseBean<Map<String, Object>> queryById(String entityId, String fieldNames) {
        return this.crudController.queryById(entityId, fieldNames);
    }

    @RequestMapping({"/refFieldQuery"})
    public ResponseBean<ReferenceQueryResult> queryReferenceFieldRecord(@RequestParam("entity") String entityName, @RequestParam("refField") String refFieldName, @RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize, @RequestParam("queryText") String queryText) {
        return this.crudController.queryReferenceFieldRecord(entityName, refFieldName, pageNo, pageSize, queryText, "");
    }

    @RequestMapping({"/saveRecord"})
    public ResponseBean<FormQueryResult> saveRecord(@RequestParam("entity") String entityName, @RequestParam("id") String recordId, @RequestBody Map<String, Object> dataMap) {
        return this.crudController.saveRecord(entityName, recordId, dataMap);
    }

    @RequestMapping({"/deleteRecord"})
    public ResponseBean deleteRecord(@RequestBody BatchOperation param) {
        return this.crudController.deleteRecord(param);
    }

    @GetMapping({"getEntityCodeList"})
    public ResponseBean<List<Map<String, Object>>> getEntityCodeList(@RequestParam(defaultValue = "entityName") String entityName, @RequestParam(defaultValue = "entityCode") String fieldName) {
        return this.crudController.getEntityCodeList(entityName, fieldName);
    }

    @GetMapping({"queryEntityFields"})
    public ResponseBean<List<Map<String, Object>>> queryEntityFields(int entityCode, @RequestParam(defaultValue = "false") boolean queryReference, @RequestParam(defaultValue = "false") boolean queryReserved, @RequestParam(defaultValue = "false") boolean firstReference) {
        return this.crudController.queryEntityFields(entityCode, queryReference, queryReserved, firstReference);
    }
}
