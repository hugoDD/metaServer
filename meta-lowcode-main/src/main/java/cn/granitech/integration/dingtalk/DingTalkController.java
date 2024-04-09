package cn.granitech.integration.dingtalk;

import cn.granitech.business.service.CrudService;
import cn.granitech.business.service.UserService;
import cn.granitech.util.ResponseHelper;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.web.constant.SessionNames;
import cn.granitech.web.controller.BaseController;
import cn.granitech.web.pojo.ResponseBean;
import cn.granitech.web.pojo.application.SystemSetting;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequestMapping({"/dingTalk"})
@RestController
public class DingTalkController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(DingTalkController.class);
    private static final String DING_TALK_AUTH_URL = "https://login.dingtalk.com/oauth2/auth?redirect_uri=%s&response_type=code&client_id=%s&scope=openid&state=dddd&prompt=consent";
    @Resource
    CrudService crudService;
    @Resource
    SystemSetting systemSetting;
    @Resource
    UserService userService;

    @RequestMapping(method = {RequestMethod.GET}, value = {"/userLogin"})
    public void userAuth(HttpServletResponse response) throws IOException {
        String loginAuthUrl = this.systemSetting.getHomeURL() + "/dingTalk/userAuth";
        if (StrUtil.isBlank(loginAuthUrl)) {
            log.info("未配置外网项目地址，无法使用钉钉消息通知！");
            return;
        }
        if (!loginAuthUrl.startsWith("http://") && !loginAuthUrl.startsWith("https://")) {
            loginAuthUrl = "http://" + loginAuthUrl;
        }
        String url = String.format(DING_TALK_AUTH_URL, loginAuthUrl, DingTalkSdk.getDingTalkConfig().getDingTalkAppKey());
        log.info("授权地址url={}", url);
        response.sendRedirect(url);
    }

    @RequestMapping(method = {RequestMethod.GET}, value = {"/userAuth"})
    public ResponseBean<String> userAuth(HttpServletRequest request, HttpServletResponse response, @RequestParam("authCode") String authCode) throws IOException {
        log.info("请求授权码={},请求协议={}", authCode, request.getScheme());
        UserLoginInfo userLoginInfo = DingTalkSdk.getUserLoginInfo(DingTalkSdk.getUserAccessToken(authCode));
        log.info("授权到用户信息: {}", userLoginInfo);
        EntityRecord entityRecord = findUserByUserId(DingTalkSdk.getUserIdByUnionId(userLoginInfo.getUnionId()));
        if (entityRecord != null) {
            this.userService.handleUserSession(request, entityRecord.id().getId());
            response.sendRedirect(this.systemSetting.getHomeURL());
            return ResponseHelper.ok();
        }
        request.getSession().setAttribute(SessionNames.DingTalkUserId, userLoginInfo.getUnionId());
        response.sendRedirect(this.systemSetting.getHomeURL() + "/web/login");
        return ResponseHelper.fail("系统内未找到用户信息，登录后自动绑定钉钉！");
    }

    private EntityRecord findUserByUserId(String dingTalkUserId) {
        return this.crudService.queryOneRecord("User", String.format("dingTalkUserId = '%s'", dingTalkUserId), null, null);
    }
}
