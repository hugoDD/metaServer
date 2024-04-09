package cn.granitech.web.controller;

import cn.granitech.business.service.CrudService;
import cn.granitech.business.service.LayoutService;
import cn.granitech.business.service.NotificationService;
import cn.granitech.business.service.ShareAccessService;
import cn.granitech.util.EntityHelper;
import cn.granitech.util.FilterHelper;
import cn.granitech.util.MetadataHelper;
import cn.granitech.util.ResponseHelper;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.pojo.Pagination;
import cn.granitech.variantorm.pojo.QuerySchema;
import cn.granitech.web.pojo.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping({"/crud"})
@RestController
public class CrudController extends BaseController {
    @Autowired
    CrudService crudService;
    @Resource
    LayoutService layoutService;
    @Resource
    NotificationService notificationService;
    @Resource
    ShareAccessService shareAccessService;

    @RequestMapping({"/listQuery"})
    public ResponseBean<ListQueryResult> queryListMap(@RequestBody ListQueryRequestBody requestBody) {
        requestBody.setDefaultFilter(this.layoutService.getDefaultFilter(requestBody.getMainEntity()));
        QuerySchema querySchema = requestBody.querySchema();
        Pagination pagination = requestBody.pagination();
        List<Map<String, Object>> resultList = this.crudService.queryListMap(querySchema, pagination);
        ListQueryResult queryResult = new ListQueryResult();
        queryResult.setDataList(resultList);
        queryResult.setPagination(pagination);
        return new ResponseBean<>(200, null, "success", queryResult);
    }

    @RequestMapping({"/queryById"})
    public ResponseBean<Map<String, Object>> queryById(String entityId, String fieldNames) {
        ID recordId = new ID(entityId);
        String fieldNames2 = MetadataHelper.entityFieldFilter(EntityHelper.getEntity(recordId.getEntityCode()), fieldNames);
        return new ResponseBean<>(200, null, "success", this.crudService.queryMapById(recordId, StringUtils.isBlank(fieldNames2) ? null : fieldNames2.split(",")));
    }

    @RequestMapping({"/initDataList"})
    public ResponseBean<ListQueryResult> initDataListView(@RequestParam("entity") String entityName) throws IllegalAccessException, InstantiationException, JsonProcessingException {
        return new ResponseBean<>(200, null, "success", this.crudService.getDataLiveViewAndData(entityName));
    }

    @RequestMapping({"/refFieldQuery"})
    public ResponseBean<ReferenceQueryResult> queryReferenceFieldRecord(@RequestParam("entity") String entityName, @RequestParam("refField") String refFieldName, @RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize, @RequestParam("queryText") String queryText, @RequestParam(required = false, value = "extraFilter") String extraFilter) {
        return ResponseHelper.ok(this.crudService.queryReferenceFieldRecord(entityName, refFieldName, pageNo, pageSize, queryText, extraFilter), "success");
    }

    @RequestMapping({"/formCreateQuery"})
    public ResponseBean<FormQueryResult> queryFormDataForCreate(@RequestParam("entity") String entityName) throws IllegalAccessException, IOException, InstantiationException {
        return ResponseHelper.ok(this.crudService.queryFormDataForCreate(entityName), "success");
    }

    @RequestMapping({"/formUpdateQuery"})
    public ResponseBean<FormQueryResult> queryFormDataForUpdate(@RequestParam("entity") String entityName, @RequestParam("id") String recordId) {
        return ResponseHelper.ok(this.crudService.queryFormDataForUpdate(entityName, recordId), "success");
    }

    @RequestMapping({"/formViewQuery"})
    public ResponseBean<FormQueryResult> queryFormDataForView(@RequestParam("entity") String entityName, @RequestParam("id") String recordId) throws IllegalAccessException, IOException, InstantiationException {
        return ResponseHelper.ok(this.crudService.queryFormDataForUpdate(entityName, recordId), "success");
    }

    @RequestMapping({"/saveRecord"})
    public ResponseBean<FormQueryResult> saveRecord(@RequestParam("entity") String entityName, @RequestParam(required = false, value = "id") String recordId, @RequestBody Map<String, Object> dataMap) {
        return ResponseHelper.ok(this.crudService.saveRecord(entityName, recordId, dataMap), "success");
    }

    @RequestMapping({"/assignRecord"})
    public ResponseBean<Integer> assignRecord(@RequestBody BatchOperation param) {
        return ResponseHelper.ok(Integer.valueOf(this.crudService.assignRecord(ID.valueOf(param.getToUser()), param.getRecordIds(), param.getCascades())), "success");
    }

    @PostMapping({"/shareRecord"})
    public ResponseBean shareRecord(@RequestBody BatchOperation param) {
        return ResponseHelper.ok(Integer.valueOf(this.shareAccessService.shareRecord(param.getRecordIds(), param.getToUsersId(), param.getCascades(), param.isWithUpdate())));
    }

    @PostMapping({"/cancelShareRecord"})
    public ResponseBean cancelShareRecord(@RequestParam("userType") Integer userType, @RequestBody BatchOperation param) {
        return ResponseHelper.ok(Integer.valueOf(this.shareAccessService.cancelShareRecord(userType, param.getRecordIds(), param.getToUsersId())));
    }

    @RequestMapping({"/deleteRecord"})
    public ResponseBean deleteRecord(@RequestBody BatchOperation param) {
        return ResponseHelper.ok(Integer.valueOf(this.crudService.delete(param.getRecordIds(), param.getCascades(), "手动删除")), "success");
    }

    @GetMapping({"getEntityCodeList"})
    public ResponseBean<List<Map<String, Object>>> getEntityCodeList(String entityName, @RequestParam(defaultValue = "entityCode") String fieldName) {
        return ResponseHelper.ok(this.crudService.queryEntityCodeListByEntity(entityName, fieldName));
    }

    @GetMapping({"queryEntityFields"})
    public ResponseBean<List<Map<String, Object>>> queryEntityFields(int entityCode, @RequestParam(defaultValue = "false") boolean queryReference, @RequestParam(defaultValue = "false") boolean queryReserved, @RequestParam(defaultValue = "false") boolean firstReference) {
        return ResponseHelper.ok(this.crudService.queryEntityFields(entityCode, queryReference, queryReserved, firstReference));
    }

    @RequestMapping({"/testEquation"})
    public ResponseBean<Boolean> testEquation(String equation) {
        if (StringUtils.isBlank(equation) || FilterHelper.validEquation(equation) != null) {
            return ResponseHelper.ok();
        }
        return ResponseHelper.fail("高级表达式格式错误！");
    }

    @RequestMapping({"/checkStatus"})
    public ResponseBean<Map<String, Object>> checkStatus() {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("noteCount", Integer.valueOf(this.notificationService.queryNoteCount()));
        return ResponseHelper.ok(resultMap);
    }
}
