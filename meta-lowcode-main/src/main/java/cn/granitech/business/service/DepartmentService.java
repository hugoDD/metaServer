package cn.granitech.business.service;

import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.web.pojo.FormQueryResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DepartmentService {
    private static final String CHILDREN_STR = "children";
    private static final String ID_STR = "id";
    @Autowired
    CrudService crudService;
    @Autowired
    PersistenceManager pm;
    @Autowired
    UserService userService;

    public static void putChildrenDepartmentId(List<String> departmentIdList, Map<String, Object> department) {
        departmentIdList.add((String) department.get(ID_STR));
        List<Map<String, Object>> children = (List<Map<String, Object>>) department.get(CHILDREN_STR);
        if (children != null) {
            for (Map<String, Object> child : children) {
                putChildrenDepartmentId(departmentIdList, child);
            }
        }
    }

    @Transactional
    public Map<String, Object> getDepartmentById(String departmentId) {
        List<Map<String, Object>> departmentList = this.getDepartmentTree().stream()
                .filter((dep) -> dep.get("id").equals(departmentId)).collect(Collectors.toList());
        return departmentList.size() != 0 ? departmentList.get(0) : null;
    }

    @Transactional
    public List<Map<String, Object>> getDepartmentTree() {
        return this.userService.buildDepartmentTree();
    }

    @Transactional
    public List<String> getChildrenDepartment(String departmentId) {
        List<String> subDepartmentIdList = new ArrayList<>();
        Map<String, Object> department = findDepartment(getDepartmentTree(), departmentId);
        if (department == null || department.size() == 0) {
            return null;
        }
        putChildrenDepartmentId(subDepartmentIdList, department);
        return subDepartmentIdList;
    }

    private Map<String, Object> findDepartment(List<Map<String, Object>> departmentTree, String departmentId) {
        for (Map<String, Object> map : departmentTree) {
            if (map.get(ID_STR).equals(departmentId)) {
                return map;
            }
            Map<String, Object> result = findDepartment((List<Map<String, Object>>) map.get(CHILDREN_STR), departmentId);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    @Transactional
    public FormQueryResult saveDepartment(String entityName, String recordId, Map<String, Object> dataMap) {
        if ("0000022-00000000000000000000000000000001".equals(recordId)) {
            throw new IllegalArgumentException("根部门节点禁止编辑！");
        }
        ID newDptId = StringUtils.isBlank(recordId) ? null : ID.valueOf(recordId);
        if (this.userService.checkSameDepartmentName((String) dataMap.get(UserService.DEPARTMENT_NAME), newDptId)) {
            throw new IllegalArgumentException("存在多个相同的部门名称，请删除重复部门！");
        }
        ID oldParentId = this.userService.getParentDepartmentId(newDptId);
        if (dataMap.containsKey(UserService.PARENT_DEPARTMENT_ID)) {
            Map<String, String> newParentDep = (Map<String, String>) dataMap.get(UserService.PARENT_DEPARTMENT_ID);
            if (!(newDptId == null || oldParentId == null || Objects.equals(oldParentId.getId(), newParentDep.get(ID_STR)))) {
                throw new IllegalArgumentException("上级部门不可修改！");
            }
        }
        FormQueryResult formQueryResult = this.crudService.saveRecord(entityName, recordId, dataMap);
        this.userService.loadDepartmentCache();
        return formQueryResult;
    }

    @Transactional
    public Boolean deleteDepartment(ID departmentId) {
        if (this.userService.getChildrenDepartmentIdList(departmentId).size() > 1) {
            throw new IllegalStateException("当前部门存在子部门，不可删除！");
        } else if ("0000022-00000000000000000000000000000001".equals(departmentId.toString())) {
            throw new IllegalArgumentException("根部门节点禁止删除！");
        } else {
            Boolean result = this.crudService.delete(departmentId);
            if (result) {
                this.userService.loadDepartmentCache();
            }
            return result;
        }
    }

    @Transactional
    public List<EntityRecord> listDepartment(String search) {
        Object[] objArr = new Object[1];
        if (!StringUtils.isNotBlank(search)) {
            search = "";
        }
        objArr[0] = search;
        String filter = String.format("[departmentName] like '%%%s%%' ", objArr);
        return this.crudService.queryListRecord("Department", filter, null, null, null, UserService.DEPARTMENT_ID, UserService.DEPARTMENT_NAME, UserService.PARENT_DEPARTMENT_ID, UserService.DPT_OWNER_USER);
    }
}
