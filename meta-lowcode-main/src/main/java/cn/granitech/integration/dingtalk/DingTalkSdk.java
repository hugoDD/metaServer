package cn.granitech.integration.dingtalk;

import cn.granitech.exception.ServiceException;
import cn.granitech.util.JsonHelper;
import cn.granitech.util.CacheUtil;
import cn.granitech.util.SpringHelper;
import cn.granitech.web.enumration.RedisKeyEnum;
import cn.granitech.web.pojo.application.DingTalkSetting;
import cn.granitech.web.pojo.application.SystemSetting;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class DingTalkSdk {
    private static final String ACCESS_TOKEN_URL = "https://api.dingtalk.com/v1.0/oauth2/accessToken";
    private static final String DEPARTMENT_LIST_URL = "https://oapi.dingtalk.com/topapi/v2/department/listsub?access_token=%s";
    private static final String DEPARTMENT_URL = "https://oapi.dingtalk.com/topapi/v2/department/get?access_token=%s";
    private static final String DEPARTMENT_USER_URL = "https://oapi.dingtalk.com/topapi/v2/user/list?access_token=%s";
    private static final String GET_USERID_URL = "https://oapi.dingtalk.com/topapi/user/getbyunionid?access_token=%s";
    private static final String REDIS_TOKEN_KEY = "DingTalkAccessToken";
    private static final String SEND_MESSAGE_URL = "https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2?access_token=%s";
    private static final String USER_ACCESS_TOKEN_URL = "https://api.dingtalk.com/v1.0/oauth2/userAccessToken";
    private static final String USER_INFO_URL = "https://api.dingtalk.com/v1.0/contact/users/me";
    private static final Logger log = LoggerFactory.getLogger(DingTalkSdk.class);

    public static void sendRobotMessage(String robotWebhookUrl, String message, String sign) {
        if (StrUtil.hasBlank(robotWebhookUrl, message, sign)) {
            log.info("钉钉机器人配置参数有误！robotWebhookUrl={},message={},secret={}", robotWebhookUrl, message, sign);
            throw new ServiceException("钉钉消息机器人配置参数有误！");
        }
        try {
            HttpRequest httpRequest = HttpUtil.createPost(String.format("%s&timestamp=%s&sign=%s", robotWebhookUrl, System.currentTimeMillis(), getRobotSign(sign)));

            httpRequest.body(JsonHelper.writeObjectAsString(createTextMessageMap(message)));
            checkApiResult(httpRequest);
        } catch (Exception e) {
            log.info("发送钉钉机器人消息失败！异常信息={}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static String getRobotSign(String secret) throws Exception {
        if (StrUtil.isBlank(secret)) {
            throw new ServiceException("Secret不能为空！");
        }
        String stringToSign = System.currentTimeMillis() + "\n" + secret;
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        return URLEncoder.encode(new String(Base64.encodeBase64(mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8)))), "UTF-8");
    }

    public static Map<String, Object> createTextMessageMap(String message) {
        Map<String, Object> textMap = new HashMap<>();
        textMap.put("content", message);
        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("text", textMap);
        messageMap.put("msgtype", "text");
        return messageMap;
    }

    public static void sendMessage(String message, String dingTalkUsersId) {
        if (StrUtil.isBlank(message) || StrUtil.isBlank(dingTalkUsersId)) {
            throw new ServiceException("消息文案或者钉钉用户不能为空！");
        }
        DingTalkSetting dingTalkConfig = getDingTalkConfig();
        Map<String, Object> dingTalkMap = new HashMap<>();
        dingTalkMap.put("agent_id", dingTalkConfig.getDingTalkAgentId());
        dingTalkMap.put("msg", createTextMessageMap(message));
        dingTalkMap.put("userid_list", dingTalkUsersId);
        HttpRequest httpRequest = HttpUtil.createPost(formatUrlByToken(SEND_MESSAGE_URL));
        httpRequest.body(JsonHelper.writeObjectAsString(dingTalkMap));
        checkApiResult(httpRequest);
    }

    public static String getUserAccessToken(String authCode) {
        DingTalkSetting dingTalkConfig = getDingTalkConfig();
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("clientId", dingTalkConfig.getDingTalkAppKey());
        bodyMap.put("clientSecret", dingTalkConfig.getDingTalkAppSecret());
        bodyMap.put("code", authCode);
        bodyMap.put("grantType", "authorization_code");
        return (String) checkApiResult(HttpUtil.createPost(USER_ACCESS_TOKEN_URL).body(JsonHelper.writeObjectAsString(bodyMap))).get("accessToken");
    }

    public static UserLoginInfo getUserLoginInfo(String userAccessToken) {
        Map<String, String> headMap = new HashMap<>();
        headMap.put("x-acs-dingtalk-access-token", userAccessToken);
        HttpResponse httpResponse = HttpUtil.createGet(USER_INFO_URL).addHeaders(headMap).execute();
        if (httpResponse.getStatus() == 200) {
            return JsonHelper.readJsonValue(httpResponse.body(), UserLoginInfo.class);
//            return (UserLoginInfo) JSON.parseObject(httpResponse.body(), UserLoginInfo.class);
        }
        log.info("钉钉获取用户信息接口异常！message={}", httpResponse.body());
        throw new ServiceException("钉钉获取用户信息接口异常!");
    }

    public static String getUserIdByUnionId(String unionId) {
        Map<String, Object> dingTalkMap = new HashMap<>();
        dingTalkMap.put("unionid", unionId);
        return sendPostHttp(formatUrlByToken(GET_USERID_URL), dingTalkMap, new TypeReference<Map<String, String>>() {
        }).get("userid");
    }

    public static List<User> findAllUser() {
        List<Department> allDepartment = findAllDepartment();
        List<User> userList = new ArrayList<>();
        for (Department department : allDepartment) {
            findDepartmentUser(department.getDept_id(), userList, 0);
        }
        return userList;
    }

    public static void findDepartmentUser(long deptId, List<User> userList, long cursor) {
        Map<String, Object> map = new HashMap<>();
        map.put("dept_id", Long.valueOf(deptId));
        map.put("cursor", Long.valueOf(cursor));
        map.put("size", 100);
        UserPage userPage = sendPostHttp(formatUrlByToken(DEPARTMENT_USER_URL), map, new TypeReference<UserPage>() {
        });
        userList.addAll(userPage.getList());
        if (userPage.isHas_more()) {
            findDepartmentUser(deptId, userList, userPage.getNext_cursor());
        }
    }

    public static void findSubDepartments(long parentId, List<Department> departmentList) {
        for (Department department : sendPostHttp(formatUrlByToken(DEPARTMENT_LIST_URL), Collections.singletonMap("dept_id", parentId), new TypeReference<List<Department>>() {
        })) {
            departmentList.add(department);
            findSubDepartments(department.getDept_id(), departmentList);
        }
    }

    public static List<Department> findAllDepartment() {
        List<Department> departmentList = new ArrayList<>();
        Department rootDepartment = findRootDepartment();
        departmentList.add(rootDepartment);
        findSubDepartments(rootDepartment.getDept_id(), departmentList);
        return departmentList;
    }

    public static Department findRootDepartment() {
        return sendPostHttp(formatUrlByToken(DEPARTMENT_URL), Collections.singletonMap("dept_id", 1), new TypeReference<Department>() {
        });
    }

    public static DingTalkSetting getDingTalkConfig() {
        DingTalkSetting dingTalkSetting = SpringHelper.getBean(SystemSetting.class).getDingTalkSetting();
        if (ObjectUtil.isNull(dingTalkSetting) || !dingTalkSetting.getOpenStatus()) {
            throw new ServiceException("当前未开启钉钉应用功能！");
        } else if (!StrUtil.isBlank(dingTalkSetting.getDingTalkAppKey()) && !StrUtil.isBlank(dingTalkSetting.getDingTalkAppSecret()) && !StrUtil.isBlank(dingTalkSetting.getDingTalkAgentId())) {
            return dingTalkSetting;
        } else {
            throw new ServiceException("钉钉应用配置异常，请检查配置！");
        }
    }

    public static String getAccessToken() {
        DingTalkSetting dingTalkConfig = getDingTalkConfig();
        CacheUtil redisUtil = SpringHelper.getBean(CacheUtil.class);
        if (redisUtil.exists(RedisKeyEnum.TMP_CACHE.getKey(REDIS_TOKEN_KEY))) {
            return redisUtil.get(RedisKeyEnum.TMP_CACHE.getKey(REDIS_TOKEN_KEY));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("appKey", dingTalkConfig.getDingTalkAppKey());
        map.put("appSecret", dingTalkConfig.getDingTalkAppSecret());
        String token = (String) checkApiResult(HttpUtil.createPost(ACCESS_TOKEN_URL)
                .body(JsonHelper.writeObjectAsString(map))).get("accessToken");
        redisUtil.set(RedisKeyEnum.TMP_CACHE.getKey(REDIS_TOKEN_KEY), token, 5400L);
        return token;
    }

    private static Map<String, Object> checkApiResult(HttpRequest httpRequest) {
        HttpResponse httpResponse = httpRequest.execute();
        Map<String, Object> result = JsonHelper.readJsonValue(httpResponse.body(), Map.class);
//        (Map) JSON.parseObject(httpResponse.body(), Map.class);
        if (httpResponse.getStatus() != 200) {
            String message = (String) result.get("message");
            log.info("请求钉钉接口异常！url={}，message={}", httpRequest.getUrl(), message);
            throw new ServiceException(message);
        } else if (!result.containsKey("errcode") || (Integer) result.get("errcode") == 0) {
            return result;
        } else {
            log.info("请求钉钉接口异常！url={}，body={}", httpRequest.getUrl(), httpResponse.body());
            throw new ServiceException((String) result.get("errmsg"));
        }
    }

    private static String formatUrlByToken(String url) {
        return String.format(url, getAccessToken());
    }

    private static <T> T sendPostHttp(String url, Map<String, Object> bodyMap, TypeReference<T> typeReference) {
        HttpRequest httpRequest = HttpUtil.createPost(url);
        if (ObjectUtil.isNotNull(bodyMap)) {
            httpRequest.body(JsonHelper.writeObjectAsString(bodyMap));
        }
        Map<String, Object> apiResult = checkApiResult(httpRequest);
        if ((Integer) apiResult.get("errcode") == 0) {
            return JsonHelper.readJsonValue(apiResult.get("result").toString(), typeReference);
        }
        String errmsg = (String) apiResult.get("errmsg");
        log.error("Failed to send request. URL: {},\nerrmsg:{}", httpRequest.getUrl(), errmsg);
        throw new ServiceException(errmsg);
    }
}
