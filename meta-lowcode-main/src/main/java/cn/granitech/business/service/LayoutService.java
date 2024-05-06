package cn.granitech.business.service;

import cn.granitech.aop.annotation.SystemRight;
import cn.granitech.exception.ServiceException;
import cn.granitech.util.EntityHelper;
import cn.granitech.util.JsonHelper;
import cn.granitech.util.CacheUtil;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.web.enumration.RedisKeyEnum;
import cn.granitech.web.enumration.SystemRightEnum;
import cn.granitech.web.pojo.FormQueryResult;
import cn.granitech.web.pojo.filter.Filter;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LayoutService extends BaseService {
    public final String APPLY_TYPE_NAV = "NAV";
    public final String APPLY_TYPE_LIST = "LIST";
    public final String APPLY_TYPE_FILTER = "FILTER";
    public final String APPLY_TYPE_SEARCH = "SEARCH";
    public final String APPLY_TYPE_TAB = "TAB";
    public final String APPLY_TYPE_ADD = "ADD";
    public final String APPLY_TYPE_DEFAULT_FILTER = "DEFAULT_FILTER";
    public final String APPLY_TYPE_TOP_NAV = "TOP_NAV";
    public final String SHARE_TO_ALL = "ALL";
    public final String SHARE_TO_SELF = "SELF";
    @Resource
    UserService userService;
    @Resource
    CacheUtil redisUtil;

    public LayoutService() {
    }

    public FormQueryResult saveLayoutConfig(String recordId, String applyType, Map<String, Object> dataMap) {
        dataMap.put("applyType", applyType);
        if ("NAV".equals(applyType)) {
            return this.saveNavigationConfig(recordId, dataMap);
        } else if ("TOP_NAV".equals(applyType)) {
            return this.saveTopNavigationConfig(recordId, dataMap);
        } else if ("LIST".equals(applyType)) {
            return this.saveListConfig(recordId, dataMap);
        } else if ("FILTER".equals(applyType)) {
            return this.saveFilterConfig(recordId, dataMap);
        } else if (!"SEARCH".equals(applyType) && !"TAB".equals(applyType) && !"ADD".equals(applyType) && !"DEFAULT_FILTER".equals(applyType)) {
            throw new ServiceException("未知的applyType！");
        } else {
            return this.saveEntityLayoutConfig(recordId, dataMap);
        }
    }

    @Transactional
    public FormQueryResult saveOrUpdateLayoutConfig(String recordId, Map<String, Object> dataMap) {
        EntityRecord entityRecord = this.pm.newRecord("LayoutConfig");
        EntityHelper.formatFieldValue(entityRecord, dataMap);
        ID recordID = StringUtils.isBlank(recordId) ? null : ID.valueOf(recordId);
        ID id = super.saveOrUpdateRecord(recordID, entityRecord);
        EntityRecord savedRecord = super.queryRecordById(ID.valueOf(id), (String[]) null);
        return new FormQueryResult(null, null, savedRecord, null, null);
    }

    private FormQueryResult saveNavigationConfig(String recordId, Map<String, Object> dataMap) {
        if (!this.callerContext.checkSystemRight(SystemRightEnum.NAVIGATION_MANAGE)) {
            throw new ServiceException("当前用户没有配置导航的权限!");
        } else if (StringUtils.isBlank(recordId) && StringUtils.isBlank((String) dataMap.get("configName"))) {
            throw new ServiceException("请填写导航名称!");
        } else {
            return this.saveOrUpdateLayoutConfig(recordId, dataMap);
        }
    }

    @SystemRight(SystemRightEnum.NAVIGATION_MANAGE)
    private FormQueryResult saveTopNavigationConfig(String recordId, Map<String, Object> dataMap) {
        dataMap.put("shareTo", "ALL");
        return this.saveOrUpdateLayoutConfig(recordId, dataMap);
    }

    private FormQueryResult saveListConfig(String recordId, Map<String, Object> dataMap) {
        if (!StringUtils.isBlank(recordId)) {
            dataMap.remove("shareTo");
            EntityRecord entityRecord = super.queryRecordById(ID.valueOf(recordId), "shareTo", "createdBy");
            if ("SELF".equals(entityRecord.getFieldValue("shareTo"))) {
                if (!this.callerContext.getCallerId().equals(((ID) entityRecord.getFieldValue("createdBy")).getId())) {
                    throw new ServiceException("不能修改其他用户的 显示列!");
                }
            } else if (!this.callerContext.checkSystemRight(SystemRightEnum.ENTITY_LAYOUT_MANAGE)) {
                throw new ServiceException("当前用户没有修改实体布局的权限!");
            }

            System.out.println(dataMap.get("config"));
        } else {
            int entityCode = (Integer) dataMap.get("entityCode");
            String shareTo = (String) dataMap.get("shareTo");
            if (StringUtils.isBlank(shareTo) || !"ALL".equals(shareTo) && !"SELF".equals(shareTo)) {
                throw new ServiceException("shareTo参数异常!");
            }

            String filter = "applyType = :applyType AND entityCode = :entityCode AND shareTo = :shareTo";
            if ("SELF".equals(shareTo)) {
                filter = filter + " AND createdBy = :createdBy";
            }

            HashMap<String, Object> paramMap = new HashMap<>();
            paramMap.put("applyType", "NAV");
            paramMap.put("entityCode", entityCode);
            paramMap.put("shareTo", "SELF");
            paramMap.put("createdBy", this.callerContext.getCallerId());
            List<EntityRecord> layoutConfigId = super.queryListRecord("LayoutConfig", filter, paramMap, null, null, "layoutConfigId");
            if (layoutConfigId.size() > 0) {
                throw new ServiceException("列显示 配置已存在!");
            }
        }

        return this.saveOrUpdateLayoutConfig(recordId, dataMap);
    }

    private FormQueryResult saveFilterConfig(String recordId, Map<String, Object> dataMap) {
        if (StringUtils.isBlank(recordId)) {
            String shareTo = (String) dataMap.get("shareTo");
            if (shareTo == null) {
                throw new ServiceException("shareTo参数异常!");
            }

            if (StringUtils.isBlank((String) dataMap.get("configName"))) {
                throw new ServiceException("请填写查询名称!");
            }

            if (!shareTo.equals("SELF") && !this.callerContext.checkSystemRight(SystemRightEnum.ENTITY_LAYOUT_MANAGE)) {
                throw new ServiceException("当前用户没有修改实体布局的权限!");
            }
        } else {
            EntityRecord entityRecord = super.queryRecordById(ID.valueOf(recordId), "shareTo", "createdBy");
            if (!this.callerContext.checkSystemRight(SystemRightEnum.ENTITY_LAYOUT_MANAGE) && ("SELF".equals(entityRecord.getFieldValue("shareTo")) || "SELF".equals(dataMap.get("shareTo")))) {
                throw new ServiceException("当前用户没有修改实体布局的权限!");
            }
        }

        return this.saveOrUpdateLayoutConfig(recordId, dataMap);
    }

    @SystemRight(SystemRightEnum.ENTITY_LAYOUT_MANAGE)
    private FormQueryResult saveEntityLayoutConfig(String recordId, Map<String, Object> dataMap) {
        dataMap.put("shareTo", "ALL");
        int entityCode = (Integer) dataMap.get("entityCode");
        String applyType = (String) dataMap.get("applyType");
        if (StringUtils.isBlank(recordId)) {
            String filter = String.format("entityCode = %s AND applyType = '%s'", entityCode, applyType);
            List<EntityRecord> layoutConfigId = super.queryListRecord("LayoutConfig", filter, null, null, null, "layoutConfigId");
            if (layoutConfigId.size() > 0) {
                throw new ServiceException("当前实体已有配置，新增配置失败!");
            }
        }

        Entity entity;
        if (applyType.equals("SEARCH")) {
            entity = this.pm.getMetadataManager().getEntity(entityCode);
            this.cacheQuickFilterFields(entity.getName(), dataMap);
        } else if (applyType.equals("DEFAULT_FILTER")) {
            entity = this.pm.getMetadataManager().getEntity(entityCode);
            String config = (String) dataMap.get("config");

            Filter filter = JsonHelper.readJsonValue(config, Filter.class);
            this.redisUtil.set(RedisKeyEnum.DEFAULT_FILTER.getKey(entity.getName()), filter);
        }

        return this.saveOrUpdateLayoutConfig(recordId, dataMap);
    }

    public Map<String, Object> getLayoutList(String entityName) {
        Entity entity = this.pm.getMetadataManager().getEntity(entityName);
        String filter = "applyType != :applyType AND entityCode = :entityCode AND (shareTo != :shareTo OR createdBy = :createdBy) ";
        HashMap<String, Object> paramMap = new HashMap();
        paramMap.put("applyType", "NAV");
        paramMap.put("entityCode", entity.getEntityCode());
        paramMap.put("shareTo", "SELF");
        paramMap.put("createdBy", this.callerContext.getCallerId());
        List<EntityRecord> entityRecords = super.queryListRecord("LayoutConfig", filter, paramMap, null, null, "layoutConfigId", "entityCode", "applyType", "configName", "config", "shareTo", "createdBy");
        Map<String, Object> resultMap = new HashMap();
        resultMap.put("FILTER", new ArrayList());
        resultMap.put("LIST", new HashMap());
        entityRecords.forEach((entityRecord) -> {
            String applyType = entityRecord.getFieldValue("applyType");
            ID userId = entityRecord.getFieldValue("createdBy");
            String shareTo = entityRecord.getFieldValue("shareTo");
            if ("LIST".equals(applyType)) {
                Map<String, Object> shareMap = (Map) resultMap.get("LIST");
                shareMap.put(shareTo, entityRecord.getValuesMap());
            } else if ("FILTER".equals(applyType)) {
                boolean roleFlag = this.callerContext.checkSystemRight(SystemRightEnum.ENTITY_LAYOUT_MANAGE);
                List filterList = (List) resultMap.get("FILTER");
                if (userId.getId().equals(this.callerContext.getCallerId())) {
                    filterList.add(entityRecord.getValuesMap());
                } else if (roleFlag && !"SELF".equals(shareTo)) {
                    filterList.add(entityRecord.getValuesMap());
                }
            } else if ("TAB".equals(applyType)) {
                resultMap.put("TAB", entityRecord.getValuesMap());
            } else if ("SEARCH".equals(applyType)) {
                resultMap.put("SEARCH", entityRecord.getValuesMap());
            } else if ("ADD".equals(applyType)) {
                resultMap.put("ADD", entityRecord.getValuesMap());
            } else if ("DEFAULT_FILTER".equals(applyType)) {
                resultMap.put("DEFAULT_FILTER", entityRecord.getValuesMap());
            }

        });
        Map<String, Object> shareMap = (Map) resultMap.get("LIST");
        if (!shareMap.containsKey("ALL")) {
            shareMap.put("ALL", this.newLayoutListRecord(entity.getEntityCode(), "LIST", "ALL"));
        }

        if (!shareMap.containsKey("SELF")) {
            shareMap.put("SELF", this.newLayoutListRecord(entity.getEntityCode(), "LIST", "SELF"));
        }

        return resultMap;
    }

    public EntityRecord getLayoutEntityRecord(String entityName, String applyType, String shareTo) {
        Entity entity = EntityHelper.getEntity(entityName);
        String filter = String.format(" entityCode = '%s' AND applyType = '%s'  AND shareTo ='%s' ", entity.getEntityCode(), applyType, shareTo);
        return this.queryOneRecord("LayoutConfig", filter, null, null);
    }

    private EntityRecord newLayoutListRecord(int entityCode, String applyType, String shareTo) {
        EntityRecord entityRecord = this.pm.newRecord("LayoutConfig");
        Entity entity = this.pm.getMetadataManager().getEntity(entityCode);
        entityRecord.setFieldValue("shareTo", shareTo);
        entityRecord.setFieldValue("config", JsonHelper.writeObjectAsString(this.getEntityDefaultList(entity)));
        entityRecord.setFieldValue("entityCode", entityCode);
        entityRecord.setFieldValue("applyType", applyType);
        return entityRecord;
    }

    public List<EntityRecord> getNavigationList() {
        String filter = String.format("applyType = '%s'", "NAV");
        List<EntityRecord> entityRecords = super.queryListRecord("LayoutConfig", filter, null, "modifiedOn ASC", null, "layoutConfigId", "configName", "config", "shareTo");
        if (this.callerContext.checkSystemRight(SystemRightEnum.NAVIGATION_MANAGE)) {
            return entityRecords;
        } else {
            List<EntityRecord> resultList = entityRecords.stream().filter((entityRecord) -> {
                String shareTo = entityRecord.getFieldValue("shareTo");
                if (StringUtils.isBlank(shareTo)) {
                    return false;
                } else if (shareTo.equals("ALL")) {
                    return true;
                } else {
                    List<ID> userList = this.userService.getUserListByIds(this.shareToIdList(shareTo));
                    return userList.contains(ID.valueOf(this.callerContext.getCallerId()));
                }
            }).collect(Collectors.toList());
            return resultList;
        }
    }

    public EntityRecord getTopNavigation() {
        String filter = String.format("applyType = '%s'", "TOP_NAV");
        List<EntityRecord> entityRecords = super.queryListRecord("LayoutConfig", filter, null, null, null, "layoutConfigId", "configName", "config", "shareTo");
        return entityRecords != null && entityRecords.size() != 0 ? entityRecords.get(0) : null;
    }

    public void deleteConfig(String recordId) {
        ID layoutConfigId = ID.valueOf(recordId);
        EntityRecord entityRecord = super.queryRecordById(layoutConfigId, "applyType", "createdBy");
        String applyType = entityRecord.getFieldValue("applyType");
        if ("NAV".equals(applyType)) {
            if (!this.callerContext.checkSystemRight(SystemRightEnum.NAVIGATION_MANAGE)) {
                throw new ServiceException("当前用户没有配置导航的权限！");
            }

            String filter = String.format("applyType = '%s' AND shareTo = 'ALL'", "NAV");
            List<EntityRecord> entityRecords = super.queryListRecord("LayoutConfig", filter, null, null, null, "layoutConfigId");
            if (entityRecords != null && entityRecords.size() == 1 && layoutConfigId.equals(entityRecords.get(0).getFieldValue("layoutConfigId"))) {
                throw new ServiceException("无法删除该导航！至少需要一个全部用户可用的导航。");
            }
        } else {
            if (!"FILTER".equals(applyType)) {
                if ("LIST".equals(applyType)) {
                    throw new ServiceException("列显示无法删除！");
                }

                if ("TAB".equals(applyType)) {
                    throw new ServiceException("关联页签无法删除！");
                }

                if ("ADD".equals(applyType)) {
                    throw new ServiceException("快捷新建无法删除！");
                }

                if ("SEARCH".equals(applyType)) {
                    throw new ServiceException("快速搜索无法删除！");
                }

                throw new ServiceException("未知的应用类型！");
            }

            ID userId = entityRecord.getFieldValue("createdBy");
            String shareTo = entityRecord.getFieldValue("shareTo");
            if (!this.callerContext.checkSystemRight(SystemRightEnum.ENTITY_LAYOUT_MANAGE) && !shareTo.equals("SELF")) {
                throw new ServiceException("当前用户没有配置实体布局的权限！");
            }

            if (!userId.getId().equals(this.callerContext.getCallerId())) {
                throw new ServiceException("不能删除其他用户的常用查询！");
            }
        }

        super.deleteRecord(layoutConfigId);
    }

    public List<Map<String, Object>> getQuickFilterFields(String entityName) {
        Entity entity = this.pm.getMetadataManager().getEntity(entityName);
        List<Map<String, Object>> fields = this.redisUtil.get(RedisKeyEnum.QUICK_FILTER.getKey(entityName));
        if (fields == null) {
            EntityRecord entityRecord = super.queryOneRecord("LayoutConfig", String.format("applyType = '%s' AND entityCode = %s", "SEARCH", entity.getEntityCode()), null, null, "config");
            return this.cacheQuickFilterFields(entityName, entityRecord == null ? null : entityRecord.getValuesMap());
        } else {
            return fields;
        }
    }

    public Filter getDefaultFilter(String entityName) {
        Entity entity = this.pm.getMetadataManager().getEntity(entityName);
        Filter filter = this.redisUtil.get(RedisKeyEnum.DEFAULT_FILTER.getKey(entityName));
        if (filter == null) {
            EntityRecord entityRecord = super.queryOneRecord("LayoutConfig", String.format("applyType = '%s' AND entityCode = %s", "DEFAULT_FILTER", entity.getEntityCode()), null, null, "config");
            if (entityRecord != null) {
                String config = entityRecord.getFieldValue("config");
                if (StringUtils.isNotBlank(config)) {
                    filter = JsonHelper.readJsonValue(config, Filter.class);
                    this.redisUtil.set(RedisKeyEnum.DEFAULT_FILTER.getKey(entityName), filter);
                }
            }
        }

        return filter;
    }

    public List<Map<String, Object>> cacheQuickFilterFields(String entityName, Map<String, Object> layout) {
        if (layout == null) {
            List<Map<String, Object>> fieldList = new ArrayList<>();
            Entity entity = this.pm.getMetadataManager().getEntity(entityName);
            if (entity.getNameField() != null) {
                String name = entity.getNameField().getName();
                String label = entity.getNameField().getLabel();
                Map<String, Object> fieldMap = new HashMap<>();
                fieldMap.put("name", name);
                fieldMap.put("label", label);
                fieldList.add(fieldMap);
            }

            return fieldList;
        } else {
            String config = (String) layout.get("config");
//            JSONArray fields = JSON.parseArray(config);
            List<Map<String, Object>> fieldList = JsonHelper.readJsonValue(config, new TypeReference<List<Map<String, Object>>>() {
            });
//            List<Map<String, Object>> fieldList = (List)fields.stream().map((obj) -> {
//                Map<String, Object> innerMap = (Map)JSON.parseObject(JSON.toJSONString(obj), HashMap.class);
//                return innerMap;
//            }).collect(Collectors.toList());
            this.redisUtil.set(RedisKeyEnum.QUICK_FILTER.getKey(entityName), fieldList);
            return fieldList;
        }
    }

    private List<Map<String, Object>> getEntityDefaultList(Entity entity) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        entity.getFieldSet().forEach((field) -> {
            if (field.isDefaultMemberOfListFlag()) {
                resultList.add(EntityHelper.field2Map(field));
            }

        });
        return resultList;
    }

    private List<Map<String, Object>> filterNavByRight(List<Map<String, Object>> navArray) {
        List<Map<String, Object>> result = new ArrayList<>();
        navArray.forEach((nva) -> {
            List<Map<String, Object>> children = (List<Map<String, Object>>) nva.get("children");
            if (children != null && children.size() > 0) {
                children = this.filterNavByRight(children);
                if (children.size() > 0) {
                    nva.put("children", children);
                    result.add(nva);
                }
            } else {
                Integer entityCode = (Integer) nva.get("entityCode");
                if (entityCode != null && this.callerContext.checkQueryRight(entityCode)) {
                    result.add(nva);
                }
            }

        });
        return result;
    }

   /* private JSONArray filterNavByRight(JSONArray navArray) {
        JSONArray result = new JSONArray();
        navArray.forEach((object) -> {
            JSONObject nva = (JSONObject)object;
            JSONArray children = nva.getJSONArray("children");
            if (children != null && children.size() > 0) {
                children = this.filterNavByRight(children);
                if (children.size() > 0) {
                    nva.put("children", children);
                    result.add(nva);
                }
            } else {
                Integer entityCode = (Integer)nva.get("entityCode");
                if (entityCode != null && this.callerContext.checkQueryRight(entityCode)) {
                    result.add(nva);
                }
            }

        });
        return result;
    }*/

    private List<String> shareToIdList(String shareTo) {
//        JSONArray array = JSON.parseArray(shareTo);
//        List<String> ids = (List)array.stream().map((object) -> {
//            JSONObject idName = (JSONObject)object;
//            return (String)idName.get("id");
//        }).collect(Collectors.toList());

        List<Map<String, Object>> ids = JsonHelper.readJsonValue(shareTo, new TypeReference<List<Map<String, Object>>>() {
        });
        assert ids != null;
        return ids.stream().map(id -> (String) id.get("id")).collect(Collectors.toList());
    }
}
