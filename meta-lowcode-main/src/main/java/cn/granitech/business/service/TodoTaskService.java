package cn.granitech.business.service;

import cn.granitech.business.timer.CronTaskRegistrar;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.web.enumration.NotificationTypeEnum;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TodoTaskService extends BaseService {
    private static final String REMIND_TYPE_KEY = "remindType";
    private static final String TODO_ITEM_KEY = "todoItem";
    private static final String RECORD_ID_KEY = "recordId";
    private static final String SUBJECT = "%s-我的待办任务";
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Resource
    NotificationService notificationService;
    @Resource
    UserService userService;
    @Resource
    CronTaskRegistrar cronTaskRegistrar;

    public TodoTaskService() {
    }

    public void sendTodoTaskReminders() {
        String today = DateUtil.today();
        List<EntityRecord> todoRecords = this.getTodoRecordsForToday(today);
        this.log.info("开始执行当天的待办消息推送={}", today);

        for (EntityRecord todoRecord : todoRecords) {
            this.sendRemindersForTodoRecord(todoRecord);
        }

    }

    private List<EntityRecord> getTodoRecordsForToday(String today) {
        String filter = String.format(" todoDate = '%s' ", today);
        return this.queryListRecord("TodoTask", filter, null, null, null);
    }

    private void sendRemindersForTodoRecord(EntityRecord todoRecord) {
        String message = todoRecord.getFieldValue(TODO_ITEM_KEY);
        String remindType = todoRecord.getFieldValue(REMIND_TYPE_KEY);
        ID recordId = todoRecord.getFieldValue(RECORD_ID_KEY);
        this.log.info("任务待办提醒记录ID={}", recordId);
        EntityRecord entityRecord = this.queryRecordById(recordId);
        if (entityRecord == null) {
            this.log.info("提醒记录不存在ID={}", recordId);
        } else {
            ID userId = entityRecord.getFieldValue("ownerUser");
            EntityRecord userRecord = this.userService.getUserById(userId);
            if (StrUtil.isNotBlank(remindType)) {
                List<String> notificationTypes = StrUtil.split(remindType, ",");

                for (String type : notificationTypes) {
                    this.sendReminderBasedOnType(type, userRecord, message, recordId);
                }
            }

        }
    }

    private void sendReminderBasedOnType(String type, EntityRecord userRecord, String message, ID recordId) {
        byte var6 = -1;
        switch (type.hashCode()) {
            case 972180:
                if (type.equals("短信")) {
                    var6 = 1;
                }
                break;
            case 1168392:
                if (type.equals("邮件")) {
                    var6 = 0;
                }
                break;
            case 1174283:
                if (type.equals("通知")) {
                    var6 = 2;
                }
        }

        switch (var6) {
            case 0:
                String email = userRecord.getFieldValue("email");
                if (StrUtil.isNotBlank(email)) {
                    this.notificationService.sendEmail(email, String.format(SUBJECT, DateUtil.today()), message);
                } else {
                    this.log.info("用户={}无邮件地址，发送提醒失败", userRecord.id());
                }
                break;
            case 1:
                String mobilePhone = userRecord.getFieldValue("mobilePhone");
                if (StrUtil.isNotBlank(mobilePhone)) {
                    this.notificationService.sendSMS(mobilePhone, message);
                } else {
                    this.log.info("用户={}无手机号，发送提醒失败", userRecord.id());
                }
                break;
            case 2:
                this.callerContext.setCallerId("0000021-00000000000000000000000000000001");
                this.notificationService.addNotification(userRecord.id(), recordId, NotificationTypeEnum.TODO_MSG, message);
        }

    }

    public void saveTodoTaskJob() {
        String corn = "0 0 10 * * ?";
        EntityRecord entityRecord = this.queryOneRecord("SystemSetting", " settingName = 'todoTaskCorn' ", null, null);
        if (entityRecord != null) {
            corn = StrUtil.isNotBlank(entityRecord.getFieldValue("settingValue")) ? (String) entityRecord.getFieldValue("settingValue") : (String) entityRecord.getFieldValue("defaultValue");
        }

        this.cronTaskRegistrar.addCronTask(this::sendTodoTaskReminders, corn);
    }
}
