package cn.granitech.business.service;

import cn.granitech.interceptor.CallerContext;
import cn.granitech.variantorm.constant.SystemEntities;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.variantorm.pojo.IDName;
import cn.granitech.variantorm.pojo.Pagination;
import cn.granitech.variantorm.pojo.QuerySchema;
import cn.granitech.web.enumration.EntityRightEnum;
import cn.granitech.web.enumration.SystemRightEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class RightManager {
    @Resource
    CallerContext callerContext;
    @Resource
    DepartmentService departmentService;
    @Resource
    PersistenceManager pm;
    @Resource
    RoleService roleService;

    private Map<String, Object> getRightValueMap() throws JsonProcessingException {
        return this.roleService.getRightMapOfUser(ID.valueOf(this.callerContext.getCallerId()));
    }

    public Boolean getFunctionRight(SystemRightEnum role) {
        try {
            Map<String, Object> rightValueMap = getRightValueMap();
            String roleCode = role.getCode();
            if (!rightValueMap.containsKey(roleCode)) {
                return false;
            }
            return (Boolean) rightValueMap.get(roleCode);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Integer getEntityRight(String mapKey) throws JsonProcessingException {
        Map<String, Object> rightValueMap = getRightValueMap();
        if (!rightValueMap.containsKey(mapKey)) {
            return 0;
        }
        return (Integer) rightValueMap.get(mapKey);
    }

    public boolean checkEntityRight(EntityRecord entityRecord, EntityRightEnum entityRightEnum) {
        Integer entityRight;
        try {
            Entity entity = entityRecord.getEntity();
            Integer entityCode = entity.getEntityCode();
            if (SystemEntities.isInternalEntity(entity.getName())) {
                return false;
            }
            ID ownerUser = null;
            ID ownerDepartment = null;
            if (entity.isDetailEntityFlag()) {
                if (entity.getMainEntity().isAuthorizable() && entityRightEnum != EntityRightEnum.create) {
                    QuerySchema querySchema = new QuerySchema();
                    querySchema.setMainEntity(entity.getName());
                    querySchema.setFilter(String.format("%s = '%s'", entity.getIdField().getName(), entityRecord.getFieldValue(entity.getIdField().getName())));
                    querySchema.setSelectFields(String.format("%s.ownerUser,%s.ownerDepartment", entity.getMainDetailField().getName(), entity.getMainDetailField().getName()));
                    List<Map<String, Object>> detailList = this.pm.createDataQuery().query(querySchema, new Pagination(1, 1));
                    if (detailList == null || detailList.size() == 0) {
                        return false;
                    }
                    ownerUser = ((IDName) detailList.get(0).get(String.format("%s.ownerUser", entity.getMainDetailField().getName()))).getId();
                    ownerDepartment = ((IDName) detailList.get(0).get(String.format("%s.ownerDepartment", entity.getMainDetailField().getName()))).getId();
                }
                entityRight = getEntityRight(entityRightEnum.getCode(entity.getMainEntity().getEntityCode()));
                if (!entity.getMainEntity().isAuthorizable() && entityRight != 50) {
                    entityRight = 0;
                }
            } else {
                if (entity.isAuthorizable() && entityRightEnum != EntityRightEnum.create) {
                    ownerUser = entityRecord.getFieldValue("ownerUser");
                    ownerDepartment = entityRecord.getFieldValue("ownerDepartment");
                }
                entityRight = getEntityRight(entityRightEnum.getCode(entityCode));
                if (!entity.isAuthorizable() && entityRight != 50) {
                    entityRight = 0;
                }
            }
            if (entityRight == null || entityRight == 0) {
                return false;
            }
            if (entityRight == 10) {
                return this.callerContext.getCallerId().equals(ownerUser.getId());
            }
            if (entityRight == 20) {
                return this.callerContext.getDepartmentId().equals(ownerDepartment.getId());
            }
            if (entityRight == 30) {
                if (this.callerContext.getDepartmentId().equals(ownerDepartment.getId())) {
                    return true;
                }
                List<String> childrenDepartment = this.departmentService.getChildrenDepartment(this.callerContext.getDepartmentId());
                return childrenDepartment != null && childrenDepartment.contains(ownerDepartment.getId());
            } else if (entityRight == 40) {
                if (this.callerContext.getDepartmentId().equals(ownerDepartment.getId())) {
                    return true;
                }
                String parentId = (String) this.departmentService.getDepartmentById(this.callerContext.getDepartmentId()).get("parentId");
                if (StringUtils.isBlank(parentId)) {
                    parentId = this.callerContext.getDepartmentId();
                }
                List<String> childrenDepartment2 = this.departmentService.getChildrenDepartment(parentId);
                return childrenDepartment2 != null && childrenDepartment2.contains(ownerDepartment.getId());
            } else return entityRight == 50;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getReadRightFilter(int entityCode) {
        StringBuilder filter = new StringBuilder();
        try {
            Entity entity = this.pm.getMetadataManager().getEntity(entityCode);
            if (SystemEntities.isInternalEntity(entity.getName())) {
                return String.valueOf(false);
            }
            Integer entityRight = getEntityRight(EntityRightEnum.query.getCode(entityCode));
            String mainField = "";
            if (entity.isDetailEntityFlag()) {
                mainField = entity.getMainEntity().getName() + ".";
            } else if (!entity.isAuthorizable() && entityRight != 50) {
                entityRight = 0;
            }
            if (entityRight == null || entityRight == 0) {
                filter.append(false);
                return filter.toString();
            }
            if (entityRight == 10) {
                filter.append(mainField).append("ownerUser = '").append(this.callerContext.getCallerId()).append("'");
            } else if (entityRight == 20) {
                filter.append(mainField).append("ownerDepartment = '").append(this.callerContext.getDepartmentId()).append("'");
            } else if (entityRight == 30) {
                filter.append(mainField).append("ownerDepartment in ('").append(String.join("','", this.departmentService.getChildrenDepartment(this.callerContext.getDepartmentId()))).append("')");
            } else if (entityRight == 40) {
                String parentId = (String) this.departmentService.getDepartmentById(this.callerContext.getDepartmentId()).get("parentId");
                if (StringUtils.isBlank(parentId)) {
                    parentId = this.callerContext.getDepartmentId();
                }
                filter.append(mainField).append("ownerDepartment in ('").append(String.join("','", this.departmentService.getChildrenDepartment(parentId))).append("')");
            } else {
                filter.append(true);
            }
            return filter.toString();
        } catch (Exception e) {
            filter.append(true);
            e.printStackTrace();
        }
        return filter.toString();
    }
}
