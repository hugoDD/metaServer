package cn.granitech.web.controller;

import cn.granitech.business.service.RoleService;
import cn.granitech.util.ResponseHelper;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.web.pojo.ResponseBean;
import cn.granitech.web.pojo.RoleDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping({"/role"})
@RestController
public class RoleController extends BaseController {
    @Autowired
    RoleService roleService;

    @RequestMapping({"/getBlankRoleData"})
    public ResponseBean<RoleDTO> getBlankRoleData() {
        return ResponseHelper.ok(this.roleService.getBlankRoleData(), "success");
    }

    @RequestMapping({"/getRoleData"})
    public ResponseBean<RoleDTO> getRoleData(@RequestParam String roleId) throws JsonProcessingException {
        return ResponseHelper.ok(this.roleService.getRoleData(ID.valueOf(roleId)), "success");
    }

    @RequestMapping({"/saveRole"})
    public ResponseBean<RoleDTO> saveRole(@RequestBody RoleDTO roleDTO) {
        return ResponseHelper.ok(this.roleService.saveRole(roleDTO), "success");
    }

    @RequestMapping({"/deleteRole"})
    public ResponseBean<Boolean> deleteRole(@RequestParam String roleId) {
        return ResponseHelper.ok(this.roleService.deleteRole(ID.valueOf(roleId)), "success");
    }

    @RequestMapping({"/listRole"})
    public ResponseBean<List<Map<String, Object>>> listRole(@RequestParam(required = false) String search) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (EntityRecord roleRecord : this.roleService.listRole(search)) {
            Map<String, Object> roleMap = new HashMap<>();
            roleMap.put("roleId", roleRecord.getFieldValue("roleId").toString());
            roleMap.put("roleName", roleRecord.getFieldValue("roleName"));
            resultList.add(roleMap);
        }
        return ResponseHelper.ok(resultList, "success");
    }
}
