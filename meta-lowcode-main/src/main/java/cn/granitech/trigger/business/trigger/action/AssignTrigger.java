package cn.granitech.trigger.business.trigger.action;

import cn.granitech.business.service.CrudService;
import cn.granitech.business.service.UserService;
import cn.granitech.trigger.business.trigger.BaseTrigger;
import cn.granitech.util.JsonHelper;
import cn.granitech.util.CacheUtil;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.pojo.IDName;
import cn.granitech.web.enumration.RedisKeyEnum;
import cn.granitech.web.pojo.Cascade;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class AssignTrigger
        implements BaseTrigger {
    @Resource
    UserService userService;
    @Resource
    CacheUtil redisUtil;
    @Resource
    CrudService crudService;

    public boolean trigger(ID entityId, String actionContent) {
        Assign assign = JsonHelper.readJsonValue(actionContent, Assign.class);

        assert assign != null;
        int assignRule = assign.getAssignRule();


        List<String> queryFieldList = new ArrayList<>();
        if (assign.getAssignType() == 1) {
            queryFieldList.add("ownerUser");
        }
        assign.getCascades().forEach(cascade -> {
            if (cascade.isReferenced() || StringUtils.isBlank(cascade.getFieldName())) {
                return;
            }
            queryFieldList.add(cascade.getFieldName());
        });
        Map<String, Object> entityMap = new HashMap<>();
        if (queryFieldList.size() > 0) {
            entityMap = this.crudService.queryMapById(entityId, queryFieldList.toArray(new String[0]));
        }


        ID ownerUserId;

        if (assign.getAssignType() == 1) {
            ownerUserId = ((IDName) entityMap.get("ownerUser")).getId();
        } else {
            List<ID> userIdList = this.userService.getUserListByIds(assign.getAssignTo());
            if (CollectionUtils.isEmpty(userIdList)) {
                return false;
            }
            if (assignRule == 1) {
                String cacheKey = RedisKeyEnum.TRIGGER_ASSIGN.getKey(String.valueOf(entityId.getEntityCode()));

                ID last = this.redisUtil.get(cacheKey);
                int index = (last == null) ? 0 : ((userIdList.indexOf(last) + 1) % userIdList.size());
                ownerUserId = userIdList.get(index);


                this.redisUtil.addSet(cacheKey, ownerUserId);
            } else {

                int random = RandomUtils.nextInt(0, userIdList.size());
                ownerUserId = userIdList.get(random);
            }
        }
        List<Cascade> cascades = assign.getCascades();
        List<String> recordIdList = new ArrayList<>();
        recordIdList.add(entityId.getId());


        this.crudService.assignRecord(ownerUserId, recordIdList, cascades);

        return true;
    }
}



