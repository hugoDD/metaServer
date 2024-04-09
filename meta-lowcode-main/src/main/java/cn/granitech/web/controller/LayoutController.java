package cn.granitech.web.controller;

import cn.granitech.business.service.LayoutService;
import cn.granitech.interceptor.CallerContext;
import cn.granitech.util.RedisUtil;
import cn.granitech.util.ResponseHelper;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.web.enumration.RedisKeyEnum;
import cn.granitech.web.pojo.FormQueryResult;
import cn.granitech.web.pojo.ResponseBean;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequestMapping({"/layout"})
@RestController
public class LayoutController {
    public static final String LIST = "LIST";
    public static final String ALL = "ALL";
    public static final String SELF = "SELF";
    public static final String FILTER = "FILTER";
    @Resource
    CallerContext callerContext;
    @Resource
    LayoutService layoutService;
    @Resource
    PersistenceManager pm;
    @Resource
    RedisUtil redisUtil;

    @RequestMapping(
            value = {"/getLayoutList"},
            method = {RequestMethod.GET}
    )
    public ResponseBean<Map<String, Object>> getLayoutConfig(String entityName) {
        Entity entity = this.pm.getMetadataManager().getEntity(entityName);
        Map<String, Object> resultMap = this.layoutService.getLayoutList(entityName);

        resultMap.put("chosenListType", this.getUserCache(LIST, entityName));

        resultMap.put("titleWidthForAll", this.getUserCache(LIST, entityName, ALL));

        resultMap.put("titleWidthForSelf", this.getUserCache(LIST, entityName, SELF));

        resultMap.put("advFilter", this.getUserCache(FILTER, entityName));
        resultMap.put("quickFilterLabel", this.layoutService.getQuickFilterFields(entityName).stream()
                .map((field) -> (String) field.get("label")).collect(Collectors.joining("/")));
        resultMap.put("idFieldName", entity.getIdField().getName());
        resultMap.put("nameFieldName", (entity.getNameField() == null ? entity.getIdField() : entity.getNameField()).getName());
        return ResponseHelper.ok(resultMap, "success");
    }


    @RequestMapping(method = {RequestMethod.POST}, value = {"/saveConfig"})
    public ResponseBean<FormQueryResult> saveLayoutConfig(String recordId, String applyType, @RequestBody Map<String, Object> dataMap) {
        return ResponseHelper.ok(this.layoutService.saveLayoutConfig(recordId, applyType, dataMap), "success");
    }

    @RequestMapping(method = {RequestMethod.POST}, value = {"/deleteConfig"})
    public ResponseBean<?> deleteConfig(String recordId) {
        this.layoutService.deleteConfig(recordId);
        return ResponseHelper.ok();
    }

    @RequestMapping(method = {RequestMethod.GET}, value = {"/getNavigationList"})
    public ResponseBean<Map<String, Object>> getLayoutConfig() {
        List<EntityRecord> navigationList = this.layoutService.getNavigationList();
        EntityRecord topNavigation = this.layoutService.getTopNavigation();
        String cacheValue = getUserCache("NAV");
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("chosenNavigationId", cacheValue);
        resultMap.put("navigationList", layoutRecordToMap(navigationList));
        resultMap.put("topNavigation", topNavigation == null ? null : topNavigation.getValuesMap());
        return ResponseHelper.ok(resultMap, "success");
    }

    private List<Map<String, Object>> layoutRecordToMap(List<EntityRecord> navigationList) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (EntityRecord entityRecord : navigationList) {
            result.add(entityRecord.getValuesMap());
        }
        return result;
    }

    @RequestMapping({"/saveUserLayoutCache"})
    public ResponseBean<?> saveUserCache(String cacheKey, String cacheValue) {
        String key = cacheKey + ":" + this.callerContext.getCallerId();
        this.redisUtil.set(RedisKeyEnum.USER_LAYOUT_CACHE.getKey(key), cacheValue);
        return ResponseHelper.ok();
    }

    private String getUserCache(String... cacheKeys) {
        String key = String.join(":", cacheKeys) + ":" + this.callerContext.getCallerId();
        return this.redisUtil.get(RedisKeyEnum.USER_LAYOUT_CACHE.getKey(key));
    }
}
