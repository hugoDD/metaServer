package cn.granitech.web.controller;

import cn.granitech.business.service.DepartmentService;
import cn.granitech.business.service.UserService;
import cn.granitech.util.ResponseHelper;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.web.pojo.FormQueryResult;
import cn.granitech.web.pojo.ResponseBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping({"/department"})
@RestController
public class DepartmentController extends CrudController {
    @Autowired
    DepartmentService departmentService;

    @RequestMapping({"/treeData"})
    public ResponseBean<List<Map<String, Object>>> getDepartmentTree() {
        return new ResponseBean<>(200, null, "success", this.departmentService.getDepartmentTree());
    }

    @RequestMapping(method = {RequestMethod.POST}, value = {"/saveDepartment"})
    public ResponseBean<FormQueryResult> saveDepartment(@RequestParam("entity") String entityName, @RequestParam("id") String recordId, @RequestBody Map<String, Object> dataMap) {
        return ResponseHelper.ok(this.departmentService.saveDepartment(entityName, recordId, dataMap), "success");
    }

    @RequestMapping(method = {RequestMethod.POST}, value = {"/deleteDepartment"})
    public ResponseBean<Boolean> deleteDepartment(@RequestParam String departmentId) {
        return ResponseHelper.ok(this.departmentService.deleteDepartment(ID.valueOf(departmentId)), "success");
    }

    @RequestMapping({"/listDepartment"})
    public ResponseBean<List<Map<String, Object>>> listDepartment(@RequestParam(required = false) String search) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (EntityRecord departmentRecord : this.departmentService.listDepartment(search)) {
            Map<String, Object> departmentMap = new HashMap<>();
            departmentMap.put(UserService.DEPARTMENT_ID, departmentRecord.getFieldValue(UserService.DEPARTMENT_ID).toString());
            departmentMap.put(UserService.DEPARTMENT_NAME, departmentRecord.getFieldValue(UserService.DEPARTMENT_NAME));
            departmentMap.put(UserService.PARENT_DEPARTMENT_ID, departmentRecord.getFieldValue(UserService.PARENT_DEPARTMENT_ID));
            ID dptOwnerUser = departmentRecord.getFieldValue(UserService.DPT_OWNER_USER);
            departmentMap.put(UserService.DPT_OWNER_USER, dptOwnerUser == null ? null : this.crudService.getIDName(dptOwnerUser.getId()));
            resultList.add(departmentMap);
        }
        return ResponseHelper.ok(resultList);
    }
}
