package cn.granitech.trigger.business.trigger.action;

import cn.granitech.business.plugins.trigger.FunctionHelper;
import cn.granitech.business.plugins.trigger.FunctionLambda;
import cn.granitech.business.service.CrudService;
import cn.granitech.exception.ServiceException;
import cn.granitech.trigger.business.service.TriggerServiceImpl;
import cn.granitech.trigger.business.trigger.BaseTrigger;
import cn.granitech.util.JsonHelper;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Component
public class CallBackTrigger
        implements BaseTrigger {
    @Resource
    CrudService crudService;
    @Resource
    TriggerServiceImpl triggerServiceImpl;

    public boolean trigger(ID entityId, String actionContent) {
        callBack(entityId, actionContent);
        return true;
    }


    public String callBack(ID entityId, String actionContent) {
        CallBack callBack = JsonHelper.readJsonValue(actionContent, CallBack.class);
        assert callBack != null;
        if (Objects.equals(callBack.getCallBackType(),"URL")) {
            Map<String, Object> paramMap = getParamMap(entityId, callBack.isPushAllData());
            HttpRequest post = HttpUtil.createPost(callBack.getHookUrl());
            if (StringUtils.isNotBlank(callBack.getHookSecret())) {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("HOOK_SECRET", callBack.getHookSecret());
                post.addHeaders(headers);
            }
            post.form(paramMap);
            String body = post.execute().body();
            if (callBack.isForceSync() && !body.equals("SUCCESS")) {
                throw new ServiceException(String.format("触发器：回调URL执行失败：url:%s,result:%s", callBack.getHookUrl(), body));
            }
            return body;
        }
        if (callBack.getCallBackType().equals("FUNCTION")) {
            Map<String, Object> paramMap = getParamMap(entityId, callBack.isPushAllData());
            FunctionLambda functionLambda = FunctionHelper.getFunctionMap().get(callBack.getFunctionName());
            if (functionLambda != null) {
                functionLambda.execute(paramMap);
                return "调用成功";
            }
            throw new ServiceException(String.format("触发器：函数回调异常：FunctionName:%s", callBack.getFunctionName()));
        }

        throw new ServiceException(String.format("触发器：回调类型异常：CallBackType:%s", callBack.getCallBackType()));
    }


    private Map<String, Object> getParamMap(ID entityId, Boolean isPushAllData) {
        if (entityId == null) {

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("CallBack", "SUCCESS");
            return paramMap;
        }

        Map<String, Object> updateDataCache = this.triggerServiceImpl.getUpdateDataCache();
        if (isPushAllData || updateDataCache == null) {

            EntityRecord entityRecord = this.crudService.queryById(entityId);
            return entityRecord.getValuesMap();
        }

        return updateDataCache;
    }
}



