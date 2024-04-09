package cn.granitech.trigger.business.trigger.action;

import cn.granitech.business.service.CrudService;
import cn.granitech.business.service.NotificationService;
import cn.granitech.business.service.UserService;
import cn.granitech.trigger.business.trigger.BaseTrigger;
import cn.granitech.util.JsonHelper;
import cn.granitech.util.RegexHelper;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.web.enumration.NotificationTypeEnum;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Component
public class SendMsgTrigger
        implements BaseTrigger {
    @Resource
    UserService userService;
    @Resource
    CrudService crudService;
    @Resource
    NotificationService notificationService;

    public boolean trigger(ID entityId, String actionContent) {
        SendMsg sendMsg = JsonHelper.readJsonValue(actionContent, SendMsg.class);


        assert sendMsg != null;
        String content = sendMsg.getContent();
        Set<String> variables = RegexHelper.getVariableSet(content, "\\{([a-zA-Z0-9$\\.]+)}");
        Map<String, Object> variableMap = this.crudService.queryMapById(entityId, variables.toArray(new String[0]));
        for (String variable : variables) {
            content = content.replace(String.format("{%s}", variable), variableMap.get(variable).toString());
        }


        if (sendMsg.getUserType() == SendMsg.INSIDER) {

            List<ID> userListByIds = this.userService.getUserListByIds(sendMsg.getSendTo());

            int type = sendMsg.getType();
            if ((type & SendMsg.TYPE_NOTE) != 0) {
                sendMsgForNote(userListByIds, entityId, content);
            }

            boolean sendSMS = ((type & SendMsg.TYPE_PHONE) != 0);
            boolean sendEmail = ((type & SendMsg.TYPE_EMAIL) != 0);
            boolean sendDingDing = ((type & SendMsg.TYPE_DINGDING) != 0);
            if (sendSMS || sendEmail || sendDingDing) {
                String ids = userListByIds.stream().map(ID::getId).collect(Collectors.joining("','"));
                List<EntityRecord> userList = this.crudService.queryListRecord("User", String.format("userId in ('%s')", ids), null, null, null);
                if (sendSMS) {
                    List<String> mobilePhoneList = userList.stream().map(record -> (String) record.getFieldValue("mobilePhone")).collect(Collectors.toList());
                    this.notificationService.sendSMS(String.join(",", mobilePhoneList), content);
                }
                if (sendEmail) {
                    List<String> emailList = userList.stream().map(record -> (String) record.getFieldValue("email")).collect(Collectors.toList());
                    this.notificationService.sendEmail(String.join(",", emailList), sendMsg.getTitle(), content);
                }
                if (sendDingDing) {

                    List<String> dingTalkList = userList.stream().map(record -> (String) record.getFieldValue("dingTalkUserId")).filter(dingTalkUserId -> (dingTalkUserId != null && !dingTalkUserId.isEmpty())).collect(Collectors.toList());
                    this.notificationService.sendDingTalkNotice(content, dingTalkList);
                }
            }
        } else if (sendMsg.getUserType() == SendMsg.DINGROBOT) {

            this.notificationService.sendDingTalkRobotNotice(sendMsg.getDingdingRobotUrl(), content, sendMsg.getDingdingSign());
        } else {

            int type = sendMsg.getType();

            boolean sendSMS = ((type & SendMsg.TYPE_PHONE) != 0);
            boolean sendEmail = ((type & SendMsg.TYPE_EMAIL) != 0);
            if (sendSMS || sendEmail) {
                Map<String, Object> map = this.crudService.queryMapById(entityId, (String[]) sendMsg.getSendTo().toArray((Object[]) new String[sendMsg.getSendTo().size()]));
                Set<String> toList = map.keySet().stream().map(key -> (map.get(key) == null) ? null : map.get(key).toString()).collect(Collectors.toSet());
                String[] toArray = toList.toArray(new String[0]);
                if (sendSMS) {
                    this.notificationService.sendSMS(String.join(",", toArray), content);
                }
                if (sendEmail) {
                    this.notificationService.sendEmail(String.join(",", toArray), sendMsg.getTitle(), content);
                }
            }
        }

        return true;
    }


    private void sendMsgForNote(List<ID> sendTo, ID entityId, String content) {
        for (ID userId : sendTo)
            this.notificationService.addNotification(userId, entityId, NotificationTypeEnum.SYSTEM_MSG, content);
    }
}



