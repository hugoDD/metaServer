package cn.granitech.business.plugins.trigger;

import cn.granitech.variantorm.metadata.ID;
import cn.granitech.web.enumration.TriggerWhenEnum;

import java.util.List;

public interface TriggerService {
    void executeTrigger(TriggerWhenEnum triggerWhenEnum, ID id, TriggerLock triggerLock);

    void executeTrigger(String str, int i, String str2, String str3, int i2, TriggerWhenEnum triggerWhenEnum);

    List<String> getDeleteRecordIdList();

    TriggerLock getTriggerLock();
}
