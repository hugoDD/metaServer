package cn.granitech.business.service;

import cn.granitech.integration.dingtalk.DingTalkSdk;
import cn.granitech.interceptor.CallerContext;
import cn.granitech.util.EntityHelper;
import cn.granitech.util.JsonHelper;
import cn.granitech.util.CacheUtil;
import cn.granitech.util.RegexHelper;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.persistence.cache.QueryCache;
import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.variantorm.pojo.Pagination;
import cn.granitech.variantorm.pojo.QuerySchema;
import cn.granitech.web.enumration.MessageEnum;
import cn.granitech.web.enumration.NotificationTypeEnum;
import cn.granitech.web.enumration.RedisKeyEnum;
import cn.granitech.web.pojo.application.DingTalkSetting;
import cn.granitech.web.pojo.application.EmailSetting;
import cn.granitech.web.pojo.application.SMSSetting;
import cn.granitech.web.pojo.application.SystemSetting;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NotificationService extends BaseService {
    private static final String SEND_SMS_URL = "https://api-v4.mysubmail.com/sms/multisend";
    private static final String SEND_EMAIL_URL = "https://api-v4.mysubmail.com/mail/send";
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Resource
    CallerContext callerContext;
    @Resource
    CacheUtil redisUtil;
    @Resource
    SystemSetting systemSetting;

    public NotificationService() {
    }

    public void addNotification(ID userId, ID recordId, NotificationTypeEnum type, String message) {
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("toUser", userId);
        dataMap.put("relatedRecord", recordId);
        dataMap.put("type", type.getType());
        dataMap.put("message", message);
        dataMap.put("unread", true);
        dataMap.put("fromUser", this.callerContext.getCallerId());
        super.saveOrUpdateRecord("Notification", null, dataMap);
        this.updateNoteCount();
    }

    public void addApprovalNotification(ID recordId, MessageEnum messageEnum, ID taskCreateUser) {
        Entity entity = EntityHelper.getEntity(recordId.getEntityCode());
        QueryCache queryCache = this.pm.getQueryCache();
        String label = entity.getLabel();
        String recordName = queryCache.getIDName(recordId.getId()).getName();
        EntityRecord entityRecord = this.queryRecordById(recordId);
        ID ownerUser = entityRecord.getFieldValue("ownerUser");
        if (ownerUser != null) {
            String message;
            if (taskCreateUser != null && ownerUser != taskCreateUser) {
                message = MessageEnum.APPROVAL_MSG_SUBMIT.getMessage(label, recordName);
                this.addNotification(taskCreateUser, recordId, NotificationTypeEnum.APPROVAL_NOTE, message);
            }

            message = messageEnum.getMessage(label, recordName);
            this.addNotification(ownerUser, recordId, NotificationTypeEnum.APPROVAL_NOTE, message);
        }

    }

    public void addApprovalNotification(ID recordId, MessageEnum messageEnum) {
        this.addApprovalNotification(recordId, messageEnum, null);
    }

    public void addApprovalNotification(ID[] userIds, ID recordId, MessageEnum messageEnum) {
        if (!ArrayUtil.isEmpty(userIds)) {
            Entity entity = EntityHelper.getEntity(recordId.getEntityCode());
            QueryCache queryCache = this.pm.getQueryCache();
            String label = entity.getLabel();
            String recordName = queryCache.getIDName(recordId.getId()).getName();
            NotificationTypeEnum notificationTypeEnum;
            if (messageEnum == MessageEnum.APPROVAL_MSG_SEND) {
                notificationTypeEnum = NotificationTypeEnum.APPROVAL_SEND_COPY;
            } else {
                notificationTypeEnum = NotificationTypeEnum.APPROVAL_NOTE;
            }

            String message = messageEnum.getMessage(label, recordName);
            this.addNotification(userIds, recordId, notificationTypeEnum, message);
        }
    }

    public void addAssignNotification(ID recordId, ID operator, ID ownerUser, ID toUser) {
        if (!ownerUser.equals(toUser)) {
            Entity entity = this.pm.getMetadataManager().getEntity(recordId.getEntityCode());
            QueryCache queryCache = this.pm.getQueryCache();
            String label = entity.getLabel();
            String recordName = queryCache.getIDName(recordId.getId()).getName();
            String operatorName = queryCache.getIDName(operator.getId()).getName();
            String toUserName = queryCache.getIDName(toUser.getId()).getName();
            this.addNotification(ownerUser, recordId, NotificationTypeEnum.ASSIGN_MSG, MessageEnum.ASSIGN_MSG_TO_OWNER.getMessage(label, recordName, operatorName, toUserName));
            this.addNotification(toUser, recordId, NotificationTypeEnum.ASSIGN_MSG, MessageEnum.ASSIGN_MSG_TO_USER.getMessage(operatorName, label, recordName));
        }
    }

    public void addNotification(ID[] userIds, ID recordId, NotificationTypeEnum type, String message) {

        for (ID userId : userIds) {
            this.addNotification(userId, recordId, type, message);
        }

    }

    private void updateNoteCount() {
        this.redisUtil.remove(RedisKeyEnum.NOTIFICATION_COUNT.getKey(this.callerContext.getCallerId()));

        try {
            this.queryNoteCount();
        } catch (Exception var2) {
            System.err.println("更新未读消息数量失败！");
            var2.printStackTrace();
        }

    }

    public List<Map<String, Object>> queryNotification(Boolean unread) {
        String filter = "toUser = '%s' " + (unread == null ? "" : "AND unread = %s");
        filter = String.format(filter, this.callerContext.getCallerId(), unread);
        QuerySchema querySchema = new QuerySchema();
        querySchema.setMainEntity("Notification");
        querySchema.setFilter(filter);
        querySchema.setSort("createdOn DESC");
        querySchema.setSelectFields("fromUser,toUser,relatedRecord,message,type,unread,createdOn");
        return super.queryListMap(querySchema, new Pagination(1, 100));
    }

    public int queryNoteCount() {
        String key = RedisKeyEnum.NOTIFICATION_COUNT.getKey(this.callerContext.getCallerId());
        Integer count = this.redisUtil.get(key);
        if (count == null) {
            String filter = String.format("unread = 1 AND toUser = '%s'", this.callerContext.getCallerId());
            List<EntityRecord> entityRecords = super.queryListRecord("Notification", filter, null, null, null, "notificationId");
            count = entityRecords.size();
            this.redisUtil.set(key, count);
        }

        return count;
    }

    public void read(String notificationId) {
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("notificationId", notificationId);
        dataMap.put("unread", false);
        super.saveOrUpdateRecord("Notification", ID.valueOf(notificationId), dataMap);
        this.updateNoteCount();
    }

    public void readAll() {
        String filter = String.format("unread = 1 AND toUser = '%s'", this.callerContext.getCallerId());
        List<EntityRecord> entityRecords = super.queryListRecord("Notification", filter, null, null, null, "notificationId");

        for (EntityRecord entityRecord : entityRecords) {
            ID notificationId = entityRecord.getFieldValue("notificationId");
            HashMap<String, Object> dataMap = new HashMap<>();
            dataMap.put("unread", false);
            super.saveOrUpdateRecord("Notification", notificationId, dataMap);
        }

        this.updateNoteCount();
    }

    public void sendSMS(String mobilePhone, String content) {
        if (this.checkSMSState() && !StringUtils.isBlank(mobilePhone) && !StringUtils.isBlank(content)) {
            List<Map<String, String>> toList = Arrays.stream(mobilePhone.split(","))
                    .filter((str) -> StringUtils.isNotBlank(str) && str.matches("^1\\d{10}$")).map((str) -> {
                        Map<String, String> map = new HashMap<>();
                        map.put("to", str);
                        return map;
                    }).collect(Collectors.toList());
            SMSSetting smsSetting = this.systemSetting.getSmsSetting();
            HashMap<String, Object> formMap = new HashMap<>();
            formMap.put("appid", smsSetting.getAppId());
            formMap.put("multi", JsonHelper.writeObjectAsString(toList));
            formMap.put("content", smsSetting.getSignature() + content);
            formMap.put("signature", smsSetting.getAppKey());
            String body = HttpUtil.createPost(SEND_SMS_URL).form(formMap).execute().body();
            this.log.info("短信发送完成。短信内容：{}，发送结果：{}", content, body);
        }
    }

    public boolean checkSMSState() {
        SMSSetting smsSetting = this.systemSetting.getSmsSetting();
        return smsSetting.getOpenStatus() && StringUtils.isNotBlank(smsSetting.getAppId()) && StringUtils.isNotBlank(smsSetting.getAppKey()) && StringUtils.isNotBlank(smsSetting.getSignature());
    }

    public void sendEmail(String to, String subject, String text) {
        if (this.checkEmailState() && !StringUtils.isBlank(to) && !StringUtils.isBlank(subject)) {
            List<String> toList = Arrays.stream(to.split(","))
                    .filter((str) -> StringUtils.isNotBlank(str) && RegexHelper.isEmail(str))
                    .collect(Collectors.toList());
            EmailSetting emailSetting = this.systemSetting.getEmailSetting();
            HashMap<String, Object> formMap = new HashMap<>();
            formMap.put("appid", emailSetting.getAppId());
            formMap.put("to", String.join(",", toList));
            formMap.put("from", emailSetting.getFrom());
            formMap.put("from_name", emailSetting.getFromName());
            formMap.put("subject", subject);
            formMap.put("text", text);
            formMap.put("signature", emailSetting.getAppKey());
            if (StringUtils.isNotBlank(emailSetting.getCc())) {
                formMap.put("cc", emailSetting.getCc());
            }

            String body = HttpUtil.createPost(SEND_EMAIL_URL).form(formMap).execute().body();
            Map<String, Object> result = JsonHelper.writeObjectAsMap(body);
//            JSONObject result = JSON.parseObject(body);
            this.log.info("发送邮件结果:{}", result);
        }
    }

    public boolean checkEmailState() {
        EmailSetting emailSetting = this.systemSetting.getEmailSetting();
        return emailSetting.getOpenStatus() && StringUtils.isNotBlank(emailSetting.getAppId()) && StringUtils.isNotBlank(emailSetting.getAppKey()) && StringUtils.isNotBlank(emailSetting.getFrom());
    }

    public void sendDingTalkNotice(String message, List<String> dingTalkUsers) {
        if (!StrUtil.isBlank(message) && !ArrayUtil.isEmpty(dingTalkUsers)) {
            String users = String.join(",", dingTalkUsers);
            DingTalkSdk.sendMessage(message, users);
        }
    }

    public void sendDingTalkRobotNotice(String robotWebhookUrl, String message, String sign) {
        if (!StrUtil.hasBlank(robotWebhookUrl, message, sign)) {
            DingTalkSdk.sendRobotMessage(robotWebhookUrl, message, sign);
        }
    }

    public boolean checkDingState() {
        DingTalkSetting dingTalkSetting = this.systemSetting.getDingTalkSetting();
        return dingTalkSetting.getOpenStatus() && StringUtils.isNotBlank(dingTalkSetting.getDingTalkAgentId()) && StringUtils.isNotBlank(dingTalkSetting.getDingTalkAppKey()) && StringUtils.isNotBlank(dingTalkSetting.getDingTalkAppSecret());
    }
}
