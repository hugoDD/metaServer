package cn.granitech.web.controller;

import cn.granitech.aop.annotation.SystemRight;
import cn.granitech.business.task.HeavyTask;
import cn.granitech.business.task.TaskExecutors;
import cn.granitech.business.task.TaskResultState;
import cn.granitech.util.ResponseHelper;
import cn.granitech.web.enumration.SystemRightEnum;
import cn.granitech.web.pojo.ResponseBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequestMapping({"/heavyTask"})
@RestController
public class HeavyTaskController {
    @GetMapping({"/taskState"})
    @SystemRight(SystemRightEnum.RECYCLE_IMPORT_MANAGE)
    public ResponseBean<TaskResultState> taskState(@RequestParam String taskId) {
        HeavyTask<?> task = TaskExecutors.get(taskId);
        if (task == null) {
            return ResponseHelper.fail("任务Id未找到！taskId=" + taskId);
        }
        return ResponseHelper.ok(formatTaskState(task));
    }

    @GetMapping({"cancel"})
    public ResponseBean<TaskResultState> taskCancel(HttpServletRequest request, @RequestParam String taskId) {
        HeavyTask<?> task = TaskExecutors.get(taskId);
        if (task == null) {
            return ResponseHelper.fail("任务Id未找到！taskId=" + taskId);
        }
        if (task.isCompleted()) {
            return ResponseHelper.fail("无法终止，因为任务已经完成");
        }
        task.interrupt();
        for (int i = 0; i < 10; i++) {
            if (task.isInterrupted()) {
                return ResponseHelper.ok(formatTaskState(task));
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return ResponseHelper.fail("无法终止任务");
    }

    private TaskResultState formatTaskState(HeavyTask<?> task) {
        return new TaskResultState(task);
    }
}
