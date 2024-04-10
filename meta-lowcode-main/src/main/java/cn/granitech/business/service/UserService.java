package cn.granitech.business.service;

import cn.granitech.exception.ServiceException;
import cn.granitech.interceptor.SessionIPChecker;
import cn.granitech.util.EntityHelper;
import cn.granitech.util.MD5Helper;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.persistence.RecordQuery;
import cn.granitech.variantorm.pojo.IDName;
import cn.granitech.variantorm.pojo.ReferenceListCacheEO;
import cn.granitech.web.pojo.AddTeamOrRoleUserBody;
import cn.granitech.web.pojo.FormQueryResult;
import cn.granitech.web.pojo.vo.UserInfoVO;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService extends BaseService {
    public static final String USER_ID = "userId";
    public static final String USER_NAME = "userName";
    public static final String DISABLED = "disabled";
    public static final String DEPARTMENT_ID = "departmentId";
    public static final String DEPARTMENT_NAME = "departmentName";
    public static final String PARENT_DEPARTMENT_ID = "parentDepartmentId";
    public static final String DPT_OWNER_USER = "departmentOwnerUser";
    public static final String DING_DEPARTMENT_ID = "dingDepartmentId";
    public static final String OWNER_TEAM = "ownerTeam";
    public static final String ROLES = "roles";
    public static final String LOGIN_NAME = "loginName";
    public static final String LOGIN_PWD = "loginPwd";
    private final Map<ID, EntityRecord> _userCache = new LinkedHashMap<>();
    private final Map<ID, EntityRecord> _departmentCache = new LinkedHashMap<>();
    @Autowired
    CrudService crudService;
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    RevisionHistoryService revisionHistoryService;

    public UserService() {
    }

    public EntityRecord getUserById(ID userId) {
        if (userId == null) {
            return null;
        } else {
            if (this._userCache.size() <= 0) {
                this.loadUserCache();
            }

            return this._userCache.get(userId);
        }
    }

    private void loadUserCache() {
        RecordQuery recordQuery = this.pm.createRecordQuery();
        List<EntityRecord> userList = recordQuery.query("User", null, null, null, null);
        this._userCache.clear();

        for (EntityRecord user : userList) {
            this._userCache.put(user.getFieldValue("userId"), user);
        }

    }

    public void loadDepartmentCache() {
        RecordQuery recordQuery = this.pm.createRecordQuery();
        List<EntityRecord> departmentList = recordQuery.query("Department", null, null, "[departmentId]", null);
        this._departmentCache.clear();

        for (EntityRecord department : departmentList) {
            this._departmentCache.put(department.getFieldValue("departmentId"), department);
        }

    }

    public void reloadCache() {
        this.loadUserCache();
        this.loadDepartmentCache();
    }

    public String getUserName(ID userId) {
        if (userId == null) {
            return null;
        } else {
            if (this._userCache.size() <= 0) {
                this.loadUserCache();
            }

            return this._userCache.containsKey(userId) ? (String) this._userCache.get(userId).getFieldValue("userName") : null;
        }
    }

    public ID getDepartmentIdOfUser(ID userId) {
        if (this._userCache.size() <= 0) {
            this.loadUserCache();
        }

        return this._userCache.containsKey(userId) ? (ID) this._userCache.get(userId).getFieldValue("departmentId") : null;
    }

    public String getDepartmentNameOfUser(ID userId) {
        ID departmentId = this.getDepartmentIdOfUser(userId);
        if (departmentId == null) {
            return null;
        } else {
            if (this._departmentCache.size() <= 0) {
                this.loadDepartmentCache();
            }

            return this._departmentCache.containsKey(departmentId) ? (String) this._departmentCache.get(departmentId).getFieldValue("departmentName") : null;
        }
    }

    public List<EntityRecord> getTeamOrRoleUser(ID id) {
        if (this._userCache.size() == 0) {
            this.loadUserCache();
        }

        List<EntityRecord> userRecords = new ArrayList<>();

        for (EntityRecord user : this._userCache.values()) {
            ID[] refIds = new ID[0];
            if (id.getEntityCode() == 24) {
                refIds = user.getFieldValue(OWNER_TEAM);
            } else if (id.getEntityCode() == 23) {
                refIds = user.getFieldValue("roles");
            }

            List<ID> userList = new ArrayList<>();
            if (refIds != null) {
                userList = Arrays.asList(refIds);
            }

            if (userList.contains(id)) {
                userRecords.add(user);
            }
        }

        return userRecords;
    }

    public List<ID> getTeamIDListOfUser(ID userId) {
        if (this._userCache.size() == 0) {
            this.loadUserCache();
        }

        EntityRecord user = this._userCache.get(userId);
        ID[] userTeam = user.getFieldValue(OWNER_TEAM);
        return (userTeam == null ? new ArrayList<>() : Arrays.asList(userTeam));
    }

    public List<ID> getRoleIDListOfUser(ID userId) {
        if (this._userCache.size() <= 0) {
            this.loadUserCache();
        }

        EntityRecord user = this._userCache.get(userId);
        ID[] userRoles = user.getFieldValue("roles");
        return userRoles == null ? new ArrayList<>() : Arrays.asList(userRoles);
    }

    public Set<ID> getUserListById(ID id) {
        if (this._userCache.size() == 0) {
            this.loadUserCache();
        }

        Set<ID> userIdSet = new HashSet<>();

        for (Entry<ID, EntityRecord> entry : this._userCache.entrySet()) {
            EntityRecord user = entry.getValue();
            ID[] userTeams;
            if (23 == id.getEntityCode()) {
                userTeams = user.getFieldValue("roles");
                if (userTeams != null && Arrays.asList(userTeams).contains(id)) {
                    userIdSet.add(user.id());
                }
            } else if (22 == id.getEntityCode()) {
                ID department = user.getFieldValue("departmentId");
                if (id.equals(department)) {
                    userIdSet.add(user.id());
                }
            } else if (24 == id.getEntityCode()) {
                userTeams = user.getFieldValue(OWNER_TEAM);
                if (userTeams != null && Arrays.asList(userTeams).contains(id)) {
                    userIdSet.add(user.id());
                }
            }
        }

        return userIdSet;
    }

    public List<ID> getUserListByIds(List<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        } else {
            Set<ID> userIds = new HashSet<>();

            for (String id : ids) {
                ID recordId = ID.valueOf(id);
                if (21 != recordId.getEntityCode()) {
                    userIds.addAll(this.getUserListById(recordId));
                } else {
                    userIds.add(recordId);
                }
            }

            return new ArrayList<>(userIds);
        }
    }

    private ReferenceListCacheEO buildEventObject() {
        return new ReferenceListCacheEO(this, "User", "roles");
    }

    private boolean checkSameUserName(String userName, ID userId) {
        if (this._userCache.size() <= 0) {
            this.loadUserCache();
        }

        boolean match = this._userCache.values().stream().filter(u -> StringUtils.equalsIgnoreCase(userName, u.getName()))
                .anyMatch(u -> Objects.equals(u.getFieldValue("userId"), userId));

        return !match;


    }

    private boolean checkSameLoginName(String loginName, ID userId) {
        if (this._userCache.size() <= 0) {
            this.loadUserCache();
        }
        return checkSameUserName(loginName, userId);

    }

    public boolean checkSameDepartmentName(String dptName, ID dptId) {
        if (this._departmentCache.size() <= 0) {
            this.loadDepartmentCache();
        }

        return this._departmentCache.values().stream()
                .filter(dptRecord -> StringUtils.equalsIgnoreCase(dptRecord.getFieldValue("departmentName"), dptName))
                .anyMatch(dptRecord -> !dptRecord.getFieldValue("departmentId").equals(dptId));


    }

    @Transactional
    public FormQueryResult saveUser(String entityName, String recordId, Map<String, Object> dataMap) {
        ID newUserId = StringUtils.isBlank(recordId) ? null : ID.valueOf(recordId);
        String newUserName = (String) dataMap.get("userName");
        String newLoginName;
        if (this.checkSameUserName(newUserName, newUserId)) {
            newLoginName = this.pm.getMetadataManager().getEntity("User").getField("userName").getLabel();
            throw new IllegalArgumentException("存在相同的" + newLoginName);
        } else {
            newLoginName = (String) dataMap.get("loginName");
            if (this.checkSameLoginName(newLoginName, newUserId)) {
                String lnFieldLabel = this.pm.getMetadataManager().getEntity("User").getField("loginName").getLabel();
                throw new IllegalArgumentException("存在相同的" + lnFieldLabel);
            } else if (dataMap.containsKey("disabled") && (Boolean) dataMap.get("disabled") && "0000021-00000000000000000000000000000001".equals(recordId)) {
                throw new ServiceException("系统管理账号不能被禁用");
            } else {
                if (newUserId != null) {
                    dataMap.remove("loginPwd");
                    dataMap.remove("loginName");
                } else {
                    dataMap.put("loginPwd", MD5Helper.md5HexForSalt((String) dataMap.get("loginPwd"), (String) dataMap.get("loginName")));
                }

                FormQueryResult result = this.crudService.saveRecord(entityName, recordId, dataMap);
                EntityRecord formData = result.getFormData();
                ID departmentId = formData.getFieldValue("departmentId");
                ID userId = formData.getFieldValue("userId");
                ID ownerUser = formData.getFieldValue("ownerUser");
                ID ownerDepartment = formData.getFieldValue("ownerDepartment");
                if (!userId.equals(ownerUser) || !departmentId.equals(ownerDepartment)) {
                    Map<String, Object> paramMap = new HashMap<>();
                    paramMap.put("ownerUser", userId.toString());
                    paramMap.put("ownerDepartment", departmentId.toString());
                    super.saveOrUpdateRecord(entityName, userId, paramMap);
                }

                this.loadUserCache();
                this.applicationContext.publishEvent(this.buildEventObject());
                return result;
            }
        }
    }

    @Transactional
    public Boolean deleteUser(ID userId) {
        if ("0000021-00000000000000000000000000000001".equals(userId.getId())) {
            throw new ServiceException("系统管理员不能删除!");
        } else if (userId.getId().equals(this.callerContext.getCallerId())) {
            throw new ServiceException("当前用户不能删除！");
        } else {
            Boolean result = this.crudService.delete(userId);
            if (result) {
                this.loadUserCache();
                this.applicationContext.publishEvent(this.buildEventObject());
            }

            return result;
        }
    }

    private List<Map<String, Object>> buildDepartmentChildNodes(ID parentDptId) {
        List<Map<String, Object>> childrenList = new ArrayList<>();

        for (EntityRecord dptRecord : this._departmentCache.values()) {
            ID curParentDptId = dptRecord.getFieldValue("parentDepartmentId");
            ID curDptId = dptRecord.getFieldValue("departmentId");
            if (parentDptId.equals(curParentDptId)) {
                Map<String, Object> nodeTree = new TreeMap<>();
                nodeTree.put("id", curDptId.toString());
                nodeTree.put("label", dptRecord.getFieldValue("departmentName"));
                nodeTree.put("children", this.buildDepartmentChildNodes(curDptId));
                nodeTree.put("dingDepartmentId", dptRecord.getFieldValue("dingDepartmentId"));
                childrenList.add(nodeTree);
            }
        }

        return childrenList;
    }

    public List<Map<String, Object>> buildDepartmentTree() {
        if (this._departmentCache.size() <= 0) {
            this.loadDepartmentCache();
        }

        Map<String, Object> departmentMap = new TreeMap<>();
        String rootDptIdStr = "0000022-00000000000000000000000000000001";
        ID rootDptId = ID.valueOf("0000022-00000000000000000000000000000001");
        EntityRecord rootDptRecord = this._departmentCache.get(rootDptId);
        departmentMap.put("id", rootDptIdStr);
        departmentMap.put("label", rootDptRecord.getFieldValue("departmentName"));
        departmentMap.put("parentId", rootDptRecord.getFieldValue("parentDepartmentId"));
        departmentMap.put("dingDepartmentId", rootDptRecord.getFieldValue("dingDepartmentId"));
        departmentMap.put("children", this.buildDepartmentChildNodes(rootDptId));
        List<Map<String, Object>> resultList = new ArrayList<>();
        resultList.add(departmentMap);
        return resultList;
    }

    public ID getParentDepartmentId(ID dptId) {
        if (dptId == null) {
            return null;
        } else {
            if (this._departmentCache.size() <= 0) {
                this.loadDepartmentCache();
            }

            return !this._departmentCache.containsKey(dptId) ? null : (ID) this._departmentCache.get(dptId).getFieldValue("parentDepartmentId");
        }
    }

    public Set<ID> getChildrenDepartmentIdList(ID dptId) {
        Set<ID> resultSet = new LinkedHashSet<>();
        if (dptId != null) {
            resultSet.add(dptId);

            for (EntityRecord dptRecord : this._departmentCache.values()) {
                ID parentId = dptRecord.getFieldValue("parentDepartmentId");
                ID curDptId = dptRecord.getFieldValue("departmentId");
                if (dptId.equals(parentId)) {
                    resultSet.add(curDptId);
                    resultSet.addAll(this.getChildrenDepartmentIdList(curDptId));
                }
            }

        }
        return resultSet;
    }

    public Set<ID> getParentDepartmentIdList(ID dptId) {
        Set<ID> resultSet = new LinkedHashSet<>();
        if (dptId != null) {
            resultSet.add(dptId);

            for (EntityRecord dptRecord : this._departmentCache.values()) {
                ID curDptId = dptRecord.getFieldValue("departmentId");
                ID parentId = dptRecord.getFieldValue("parentDepartmentId");
                if (curDptId.equals(dptId) && parentId != null) {
                    resultSet.add(parentId);
                    resultSet.addAll(this.getParentDepartmentIdList(parentId));
                }
            }

        }
        return resultSet;
    }

    @Transactional
    public List<EntityRecord> listUser(String search) {
        String filter = String.format(" [disabled]=0 and [userName] like '%%%s%%' ", StringUtils.isNotBlank(search) ? search : "");
        return this.crudService.queryListRecord("User", filter, null, null, null, "userId", "userName");
    }

    public void resetPassword(ID userId, String password) {
        EntityRecord entityRecord = this.queryRecordById(userId, "ownerUser", "ownerDepartment", "loginName");
        if (!this.callerContext.checkUpdateRight(entityRecord)) {
            throw new ServiceException("当前登录用户没有权限！");
        } else {
            String loginName = entityRecord.getFieldValue("loginName");
            entityRecord.setFieldValue("loginPwd", MD5Helper.md5HexForSalt(password, loginName));
            super.saveOrUpdateRecord(userId, entityRecord);
        }
    }

    public ID updateLoginUser(ID recordId, Map<String, Object> dataMap) {
        String[] openFields = new String[]{"userName", "mobilePhone", "email", "avatar"};
        List<String> openFieldList = Arrays.asList(openFields);
        EntityRecord user = super.queryRecordById(recordId, openFields);
        EntityRecord entityRecord = this.pm.newRecord(String.valueOf(this.pm.getMetadataManager().getEntity(recordId.getEntityCode()).getName()));
        dataMap.keySet().stream().filter(openFieldList::contains).collect(Collectors.toList()).forEach((key) -> entityRecord.setFieldValue(key, dataMap.get(key)));
        ID id = super.saveOrUpdateRecord(ID.valueOf(recordId), entityRecord);
        Map<String, Object> updateMap = EntityHelper.getUpdateMap(entityRecord.getEntity(), entityRecord.getValuesMap(), user.getValuesMap());
        this.revisionHistoryService.recordHistory(recordId, 1, user.getValuesMap(), updateMap);
        this.loadUserCache();
        return id;
    }

    public List<UserInfoVO> teamOrRoleUsers(ID id) {
        List<EntityRecord> roleUser = this.getTeamOrRoleUser(id);
        List<UserInfoVO> userInfoVOS = new ArrayList<>();

        for (EntityRecord user : roleUser) {
            UserInfoVO userInfoVO = new UserInfoVO();
            ID userId = user.id();
            userInfoVO.setUserId(userId);
            userInfoVO.setDepartmentName(this.getDepartmentNameOfUser(userId));
            userInfoVO.setUserName(this.getUserName(userId));
            userInfoVO.setUserName(user.getName());
            userInfoVO.setTeamIds(this.getTeamIDListOfUser(userId));
            userInfoVO.setRoleIds(this.getRoleIDListOfUser(userId));
            userInfoVOS.add(userInfoVO);
        }

        return userInfoVOS;
    }

    public void addTeamOrRoleUsers(AddTeamOrRoleUserBody addTeamOrRoleUserBody) {
        List<String> idList = addTeamOrRoleUserBody.getNodeRoleList().stream().map((nodeRole) -> nodeRole.getId().getId()).collect(Collectors.toList());
        List<ID> userListByIds = this.getUserListByIds(idList);

        for (ID userListById : userListByIds) {
            this.updateTeamOrRoleUser(addTeamOrRoleUserBody.getId(), userListById, true);
        }

        this.reloadCache();
    }

    public void addUserRole(AddTeamOrRoleUserBody addTeamOrRoleUserBody) {
        List<String> roleIdList = addTeamOrRoleUserBody.getNodeRoleList().stream().map((nodeRole) -> nodeRole.getId().getId()).collect(Collectors.toList());

        for (String roleId : roleIdList) {
            this.updateTeamOrRoleUser(ID.valueOf(roleId), addTeamOrRoleUserBody.getId(), true);
        }

        this.reloadCache();
    }

    public void updateTeamOrRoleUser(ID id, ID userId, boolean add) {
        if ("0000021-00000000000000000000000000000001".equals(userId.getId())) {
            throw new ServiceException("系统管理员不能修改！");
        } else {
            EntityRecord userRecord = this.queryRecordById(userId);
            ID[] oldIds = new ID[0];
            String updateKey = null;
            int entityCode = id.getEntityCode();
            if (24 == entityCode) {
                oldIds = userRecord.getFieldValue(OWNER_TEAM);
                updateKey = OWNER_TEAM;
            } else if (23 == entityCode) {
                oldIds = userRecord.getFieldValue("roles");
                updateKey = "roles";
            }

            Set<ID> oldIdList = new HashSet<>();
            if (oldIds != null) {
                oldIdList = new HashSet<>(Arrays.asList(oldIds));
            }

            if (add) {
                oldIdList.add(id);
            } else {
                oldIdList.remove(id);
            }

            if (StrUtil.isNotBlank(updateKey)) {
                userRecord.setFieldValue(updateKey, oldIdList.toArray(new ID[0]));
                this.updateRecord(userRecord);
            }

        }
    }

    public void delTeamOrRoleUser(ID id, ID userId) {
        this.updateTeamOrRoleUser(id, userId, false);
        this.reloadCache();
    }

    public List<Map<String, Object>> getUserRole(ID userId) {
        List<ID> listOfUser = this.getRoleIDListOfUser(userId);
        List<Map<String, Object>> resultList = new ArrayList<>();

        for (ID roleId : listOfUser) {
            EntityRecord entityRecord = this.queryRecordById(roleId, "roleId", "roleName", "description");
            resultList.add(entityRecord.getValuesMap());
        }

        return resultList;
    }

    public void saveLoginLog(String userId, HttpServletRequest request) {
        String userAgentString = ServletUtil.getHeader(request, "User-Agent", "UTF-8");
        UserAgent userAgent = UserAgentUtil.parse(userAgentString);
        String sessionId = request.getSession().getId();
        EntityRecord entityRecord = this.pm.newRecord("LoginLog");
        entityRecord.setFieldValue("loginUser", userId);
        entityRecord.setFieldValue("sessionId", sessionId);
        entityRecord.setFieldValue("ip", ServletUtil.getClientIP(request));
        entityRecord.setFieldValue("browserName", userAgent.getBrowser().getName());
        entityRecord.setFieldValue("logout", false);
        this.saveOrUpdateRecord(null, entityRecord);
    }

    public void handleUserSession(HttpServletRequest request, String userId) {
        this.callerContext.setCallerId(userId);
        HttpSession session = request.getSession(false);
        if (session != null) {
            this.updateLoginLog(session.getId(), "登录挤出");
            request.changeSessionId();
        } else {
            session = request.getSession(true);
        }

        session.setAttribute("loginUserId", userId);
        SessionIPChecker.addSessionIdIP(request, session.getId());
        this.saveLoginLog(userId, request);
        String dingTalkUserId = (String) request.getSession().getAttribute("dingTalkUserId");
        if (StrUtil.isNotBlank(dingTalkUserId)) {
            EntityRecord userRecord = this.newRecord("User");
            userRecord.setFieldValue("dingTalkUserId", dingTalkUserId);
            this.crudService.saveOrUpdateRecord(ID.valueOf(userId), userRecord);
        }

    }

    public void updateLoginLog(String sessionId, String logoutType) {
        EntityRecord entityRecord = super.queryOneRecord("LoginLog", String.format("sessionId = '%s'", sessionId), null, null, "loginLogId");
        if (entityRecord != null) {
            entityRecord.setFieldValue("logout", true);
            entityRecord.setFieldValue("logoutType", logoutType);
            entityRecord.setFieldValue("logoutTime", new Date());
            entityRecord.setFieldValue("modifiedOn", new Date());
            entityRecord.setFieldValue("modifiedBy", "0000021-00000000000000000000000000000001");
            this.pm.update(entityRecord);
        }

    }

    public IDName getLoginUserId(String loginName, String password) {
        String filter = String.format(" %s = '%s' and %s ='%s'", "loginName", loginName, "loginPwd", MD5Helper.md5HexForSalt(password, loginName));
        EntityRecord entityRecord = this.crudService.queryOneRecord("User", filter, null, null, "userId", "userName", "disabled");
        if (ObjectUtil.isNotNull(entityRecord)) {
            Boolean disable = entityRecord.getFieldValue("disabled");
            if (disable) {
                throw new ServiceException("账号已被禁用，请联系管理员！");
            } else {
                ID userID = entityRecord.getFieldValue("userId");
                String userName = entityRecord.getFieldValue("userName");
                return new IDName(userID, userName);
            }
        } else {
            return null;
        }
    }

    public ID getDepartmentOwnerUser(ID id) {
        if (id == null) {
            return null;
        } else {
            if (this._departmentCache.isEmpty()) {
                this.loadDepartmentCache();
            }

            ID ownerDepartment = null;
            EntityRecord entityRecord;
            if (id.getEntityCode() == 21) {
                entityRecord = this.getUserById(id);
                ownerDepartment = entityRecord.getFieldValue("departmentId");
            }

            if (id.getEntityCode() == 22) {
                ownerDepartment = id;
            }

            entityRecord = this._departmentCache.get(ownerDepartment);
            return entityRecord != null ? (ID) entityRecord.getFieldValue("departmentOwnerUser") : null;
        }
    }

    public ID getDeptOwnerUserByLevel(ID id, int level) {
        if (id == null) {
            return null;
        } else {
            ID department;
            EntityRecord departmentRecord;
            if (id.getEntityCode() == 21) {
                departmentRecord = this.getUserById(id);
                department = departmentRecord.getFieldValue("departmentId");
            } else {
                if (id.getEntityCode() != 22) {
                    return null;
                }

                department = id;
            }

            if (this._departmentCache.isEmpty()) {
                this.loadDepartmentCache();
            }

            departmentRecord = this.getDepartmentByLevel(department, level);
            return departmentRecord != null ? (ID) departmentRecord.getFieldValue("departmentOwnerUser") : null;
        }
    }

    private EntityRecord getDepartmentByLevel(ID deptId, int level) {
        EntityRecord entityRecord = this._departmentCache.get(deptId);
        if (entityRecord == null || level > 0 && !Objects.equals(entityRecord.id().getId(), "0000022-00000000000000000000000000000001")) {
            assert entityRecord != null;
            ID parentDepartmentId = entityRecord.getFieldValue("parentDepartmentId");
            return parentDepartmentId != null ? this.getDepartmentByLevel(parentDepartmentId, level - 1) : null;
        } else {
            return entityRecord;
        }
    }
}
