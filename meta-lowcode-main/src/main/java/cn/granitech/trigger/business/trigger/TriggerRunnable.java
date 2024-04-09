package cn.granitech.trigger.business.trigger;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;


public class TriggerRunnable
        implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(TriggerRunnable.class);

    private final int entityCode;

    private final String triggerConfigId;

    private final String actionFilter;

    private final String actionContent;

    private final int actionType;


    public TriggerRunnable(int entityCode, String triggerConfigId, String actionFilter, String actionContent, int actionType) {
        this.entityCode = entityCode;
        this.triggerConfigId = triggerConfigId;
        this.actionFilter = actionFilter;
        this.actionContent = actionContent;
        this.actionType = actionType;
    }


    public void run() {
        logger.info("定时任务开始执行 - entityCode：{}，triggerConfigId：{}，actionFilter：{}，actionContent：{}", Integer.valueOf(this.entityCode), this.triggerConfigId, this.actionFilter, this.actionContent);
//        PluginService pluginService = SpringHelper.getBean(PluginService.class);
//        TriggerService triggerService = pluginService.getTriggerService();
//        triggerService.executeTrigger(this.triggerConfigId, this.entityCode, this.actionFilter, this.actionContent, this.actionType, TriggerWhenEnum.TIMER);
    }


    public boolean equals(Object triggerRunnable) {
        return this.triggerConfigId.equals(((TriggerRunnable) triggerRunnable).triggerConfigId);
    }


    public int hashCode() {
        return Objects.hash(this.triggerConfigId);
    }
}



