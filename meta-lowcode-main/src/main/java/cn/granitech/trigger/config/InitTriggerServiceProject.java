package cn.granitech.trigger.config;

import cn.granitech.trigger.business.service.TriggerServiceImpl;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
public class InitTriggerServiceProject implements ApplicationRunner {
    @Resource
    TriggerServiceImpl triggerServiceImpl;

    public void run(ApplicationArguments args) {
        this.triggerServiceImpl.initScheduledTasks();
    }
}



