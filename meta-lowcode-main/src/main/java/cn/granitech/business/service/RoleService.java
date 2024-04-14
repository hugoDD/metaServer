package cn.granitech.business.service;

import cn.granitech.util.JsonHelper;
import cn.granitech.variantorm.constant.SystemEntities;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.persistence.RecordQuery;
import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.web.enumration.EntityRightEnum;
import cn.granitech.web.enumration.SystemRightEnum;
import cn.granitech.web.pojo.RoleDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

import static cn.granitech.variantorm.constant.SystemEntityCodes.ADMIN_ROLE_ID;

@Service
public class RoleService extends BaseService {
    private final Map<ID, Map<String, Object>> _roleCache = new LinkedHashMap<>();
    @Resource
    EntityManagerService entityManagerService;
    @Resource
    PersistenceManager persistenceManager;
    @Resource
    CrudService crudService;
    @Resource
    UserService userService;

    public RoleService() {
    }

    public void loadRoleCache() {
        RecordQuery recordQuery = this.persistenceManager.createRecordQuery();
        List<EntityRecord> roleList = recordQuery.query("Role", null, null, null, null);
        this._roleCache.clear();

        for (EntityRecord role : roleList) {
            ID roleId = role.getFieldValue("roleId");
            if (roleId.getId().equals(ADMIN_ROLE_ID)) {
                this._roleCache.put(roleId, this.buildRightValueMap(true));
            } else {
                Map<String, Object> rightValueMap = new HashMap<>();
                String rightJson = role.getFieldValue("rightJson");
                if (StringUtils.isNotBlank(rightJson)) {
                    rightValueMap = JsonHelper.readJsonValue(rightJson, new TypeReference<Map<String, Object>>() {
                    });
                }

                assert rightValueMap != null;
                rightValueMap.put(SystemRightEnum.ADMIN_ROLE.getCode(), false);
                this._roleCache.put(roleId, rightValueMap);
            }
        }

    }

    private Map<String, Object> mergeRightMap(Map<String, Object> map1, Map<String, Object> map2) {

        for (String mapKey : map2.keySet()) {
            if (!map1.containsKey(mapKey)) {
                map1.put(mapKey, map2.get(mapKey));
            } else {
                if (map2.get(mapKey) instanceof Integer) {
                    Integer map1Integer = (Integer) map1.get(mapKey);
                    Integer map2Integer = (Integer) map2.get(mapKey);
                    if (map2Integer > map1Integer) {
                        map1.put(mapKey, map2Integer);
                    }
                }

                if (map2.get(mapKey) instanceof Boolean) {
                    boolean map2Boolean = (Boolean) map2.get(mapKey);
                    if (map2Boolean) {
                        map1.put(mapKey, true);
                    }
                }
            }
        }

        return map1;
    }

    public Map<String, Object> getRightMapOfUser(ID userId) throws JsonProcessingException {
        List<ID> roleList = this.userService.getRoleIDListOfUser(userId);
        if (roleList.isEmpty()) {
            return new HashMap<>();
        } else {
            if (this._roleCache.isEmpty()) {
                this.loadRoleCache();
            }

            Map<String, Object> resultMap = this._roleCache.get(roleList.get(0));

            for (int i = 1; i < roleList.size(); ++i) {
                Map<String, Object> newMap = this._roleCache.get(roleList.get(i));
                if (newMap != null) {
                    this.mergeRightMap(resultMap, newMap);
                }
            }

            return resultMap;
        }
    }

    private Map<String, Object> buildRightValueMap(boolean buildAdminRole) {
        int roleNum = buildAdminRole ? 50 : 0;
        Map<String, Object> rightMap = new LinkedHashMap<>();
        Collection<Entity> entitySet = this.entityManagerService.getMetadataManager().getEntitySet();
        entitySet.forEach((entity) -> {
            if (!entity.isDetailEntityFlag() && !SystemEntities.isInternalEntity(entity.getName())) {
                Integer entityCode = entity.getEntityCode();
                EntityRightEnum[] var4 = EntityRightEnum.values();

                for (EntityRightEnum entityRightEnum : var4) {
                    rightMap.put(entityRightEnum.getCode(entityCode), roleNum);
                }
            }

        });
        SystemRightEnum[] var5 = SystemRightEnum.values();

        for (SystemRightEnum role : var5) {
            rightMap.put(role.getCode(), buildAdminRole);
        }

        return rightMap;
    }

    private List<Map<String, Object>> buildRightEntityList() {
        List<Map<String, Object>> resultList = new ArrayList<>();
        Collection<Entity> entitySet = this.entityManagerService.getMetadataManager().getEntitySet();
        entitySet.forEach((entity) -> {
            if (!entity.isDetailEntityFlag() && !SystemEntities.isInternalEntity(entity.getName())) {
                Map<String, Object> entityMap = new HashMap<>();
                entityMap.put("name", entity.getName());
                entityMap.put("label", entity.getLabel());
                entityMap.put("entityCode", entity.getEntityCode());
                entityMap.put("authorizable", entity.isAuthorizable());
                resultList.add(entityMap);
            }

        });
        return resultList;
    }

    public RoleDTO getBlankRoleData() {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRoleName("");
        roleDTO.setDisabled(false);
        roleDTO.setDescription("");
        roleDTO.setRightValueMap(this.buildRightValueMap(false));
        roleDTO.setRightEntityList(this.buildRightEntityList());
        return roleDTO;
    }

    @Transactional
    public RoleDTO getRoleData(ID roleId) {
        RoleDTO roleDTO = new RoleDTO();
        EntityRecord roleRecord = this.crudService.queryById(roleId);
        roleDTO.setRoleId(roleRecord.getFieldValue("roleId").toString());
        roleDTO.setRoleName(roleRecord.getFieldValue("roleName"));
        roleDTO.setDisabled(roleRecord.getFieldValue("disabled"));
        roleDTO.setDescription(roleRecord.getFieldValue("description"));
        roleDTO.setRightEntityList(this.buildRightEntityList());
        if (ADMIN_ROLE_ID.equals(roleDTO.getRoleId())) {
            roleDTO.setRightValueMap(this.buildRightValueMap(true));
        } else {
            String rightJson = roleRecord.getFieldValue("rightJson");
            Map<String, Object> rightValueMap = new LinkedHashMap<>();
            if (StringUtils.isNotBlank(rightJson)) {
                rightValueMap = JsonHelper.readJsonValue(rightJson, new TypeReference<Map<String, Object>>() {
                });
            }

            roleDTO.setRightValueMap(rightValueMap);
        }

        return roleDTO;
    }

    @Transactional
    public RoleDTO saveRole(final RoleDTO roleDTO) {
        Map<String, Object> paramMap = new HashMap<String, Object>() {
            {
                this.put("roleName", roleDTO.getRoleName());
            }
        };
        List<EntityRecord> oldRoleList = this.crudService.queryListRecord("Role", "[roleName]=:roleName", paramMap, null, null);

        boolean foundFlag = oldRoleList.stream().filter(r -> Objects.equals(r.getFieldValue("roleName"), roleDTO.getRoleName()))
                .anyMatch(r -> !Objects.equals(r.getFieldValue("roleId"), roleDTO.getRoleId()));


        if (foundFlag) {
            throw new IllegalArgumentException("角色名称重复！");
        } else {
            String roleIdStr = roleDTO.getRoleId();
            if (StringUtils.isBlank(roleIdStr)) {
                EntityRecord oldRoleRecord = this.crudService.newRecord("Role");
                oldRoleRecord.setFieldValue("roleName", roleDTO.getRoleName());
                oldRoleRecord.setFieldValue("disabled", roleDTO.getDisabled());
                oldRoleRecord.setFieldValue("description", roleDTO.getDescription());
                if (roleDTO.getRightValueMap() != null) {
                    oldRoleRecord.setFieldValue("rightJson", JsonHelper.writeObjectAsString(roleDTO.getRightValueMap()));
                }

                ID newRoleId = this.crudService.create(oldRoleRecord);
                roleDTO.setRoleId(newRoleId.toString());
            } else {
                if (ADMIN_ROLE_ID.equals(roleDTO.getRoleId())) {
                    throw new IllegalArgumentException("管理员角色禁止修改");
                }

                EntityRecord oldRoleRecord = this.crudService.queryById(ID.valueOf(roleDTO.getRoleId()));
                oldRoleRecord.setFieldValue("roleName", roleDTO.getRoleName());
                oldRoleRecord.setFieldValue("disabled", roleDTO.getDisabled());
                oldRoleRecord.setFieldValue("description", roleDTO.getDescription());
                if (roleDTO.getRightValueMap() != null) {
                    oldRoleRecord.setFieldValue("rightJson", JsonHelper.writeObjectAsString(roleDTO.getRightValueMap()));
                }

                this.crudService.update(oldRoleRecord);
            }

            this.loadRoleCache();
            return roleDTO;
        }
    }

    @Transactional
    public Boolean deleteRole(ID roleId) {
        if (ADMIN_ROLE_ID.equals(roleId.toString())) {
            throw new IllegalArgumentException("管理员角色禁止删除");
        } else {
            this.loadRoleCache();
            return this.crudService.delete(roleId);
        }
    }

    public Boolean deleteEntityRight(int entityCode) {
        List<EntityRecord> roleList = this.crudService.queryListRecord("Role", null, null, null, null);

        for (EntityRecord roleRecord : roleList) {
            String rightJson = roleRecord.getFieldValue("rightJson");
            if (StringUtils.isNotBlank(rightJson)) {
                Map<String, Object> rightValueMap = JsonHelper.readJsonValue(rightJson, new TypeReference<Map<String, Object>>() {
                });
                boolean foundFlag = this.deleteEntityRightFromMap(rightValueMap, entityCode);
                if (foundFlag) {
                    roleRecord.setFieldValue("rightJson", JsonHelper.writeObjectAsString(rightValueMap));
                    this.persistenceManager.update(roleRecord);
                }
            }
        }

        return true;
    }

    private boolean deleteEntityRightFromMap(Map<String, Object> rightValueMap, int entityCode) {
        boolean result = false;
        String mapPrefix = "r" + entityCode + "-";

        for (int i = 1; i < 5; ++i) {
            String mapKey = mapPrefix + i;
            if (rightValueMap.containsKey(mapKey)) {
                result = true;
                rightValueMap.remove(mapKey);
            }
        }

        return result;
    }

    @Transactional
    public List<EntityRecord> listRole(String search) {
        String filter = String.format(" [disabled]=0 and [roleName] like '%%%s%%' ", StringUtils.isNotBlank(search) ? search : "");
        return this.crudService.queryListRecord("Role", filter, null, null, null, "roleId", "roleName");
    }
}
