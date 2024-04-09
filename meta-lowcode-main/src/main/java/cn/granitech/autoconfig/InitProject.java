package cn.granitech.autoconfig;

import cn.granitech.business.service.SystemService;
import cn.granitech.business.service.TodoTaskService;
import cn.granitech.business.service.UserService;
import cn.granitech.util.RedisUtil;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.persistence.cache.QueryCache;
import cn.granitech.web.enumration.RedisKeyEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
public class InitProject {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Resource
    PersistenceManager pm;
    @Resource
    RedisUtil redisUtil;
    @Resource
    SystemService systemService;
    @Resource
    TodoTaskService todoTaskService;

    @PostConstruct
    public void init() {
        this.systemService.loadSystemSetting();
//        this.systemService.license();
        initQueryCache();
        this.todoTaskService.saveTodoTaskJob();
    }

    private void initQueryCache() {
        QueryCache queryCache = this.pm.getQueryCache();
        if (StringUtils.isEmpty(this.redisUtil.get(RedisKeyEnum.REFERENCE_CACHE.getKey("0000021-00000000000000000000000000000001")))) {
            this.log.info("initQueryCache:初始化IDName");
            queryCache.initIDName();
        }
        if (queryCache.getIDNameList("User", UserService.ROLES, "0000021-00000000000000000000000000000001") == null) {
            this.log.info("initQueryCache:初始化IDNameList");
            queryCache.initIDNameList();
        }
    }
}
