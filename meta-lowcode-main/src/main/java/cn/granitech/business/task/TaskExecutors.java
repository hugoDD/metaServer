package cn.granitech.business.task;

import cn.granitech.exception.ServiceException;
import cn.granitech.variantorm.metadata.ID;
import cn.hutool.core.util.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.*;

@Component
public class TaskExecutors {
    private static final int MAX_TASKS_NUMBER = Integer.max(Runtime.getRuntime().availableProcessors() / 2, 2);
    private static final ExecutorService EXEC = new ThreadPoolExecutor(MAX_TASKS_NUMBER, MAX_TASKS_NUMBER, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(MAX_TASKS_NUMBER * 6));
    private static final ExecutorService SINGLE_QUEUE = new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
    private static final Map<String, HeavyTask<?>> TASKS = new ConcurrentHashMap<>();
    private static final Logger log = LoggerFactory.getLogger(TaskExecutors.class);

    public static String submit(HeavyTask<?> task, ID execUser) {
        String taskId = task.getClass().getSimpleName() + "-" + RandomUtil.randomString(20);
        if (execUser == null) {
            execUser = ID.valueOf("0000021-00000000000000000000000000000001");
        }
        task.setUser(execUser);
        EXEC.execute(task);
        TASKS.put(taskId, task);
        return taskId;
    }

    public static boolean cancel(String taskid) {
        HeavyTask<?> task = TASKS.get(taskid);
        if (task == null) {
            throw new ServiceException("No Task found : " + taskid);
        }
        task.interrupt();
        for (int i = 1; i <= 3; i++) {
            try {
                Thread.sleep(i * 500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            if (task.isInterrupted()) {
                return true;
            }
        }
        return false;
    }

    public static HeavyTask<?> get(String taskid) {
        return TASKS.get(taskid);
    }

    public static void run(HeavyTask<?> task) {
        task.run();
    }

    public static void queue(Runnable command) {
        SINGLE_QUEUE.execute(command);
    }

    public static void shutdown() {
        List<Runnable> t = EXEC.shutdownNow();
        if (!t.isEmpty()) {
            log.warn("{} task(s) were interrupted", t.size());
        }
        List<Runnable> c = SINGLE_QUEUE.shutdownNow();
        if (!c.isEmpty()) {
            log.warn("{} command(s) were interrupted", c.size());
        }
    }

    @Scheduled(fixedRate = 300000, initialDelay = 300000)
    public void executeJob() {
        if (!TASKS.isEmpty()) {
            int completed = 0;
            for (Map.Entry<String, HeavyTask<?>> e : TASKS.entrySet()) {
                HeavyTask<?> task = e.getValue();
                if (task.getCompletedTime() != null && task.isCompleted()) {
                    if ((System.currentTimeMillis() - task.getCompletedTime().getTime()) / 1000 > 7200) {
                        TASKS.remove(e.getKey());
                        log.info("HeavyTask clean up : " + e.getKey());
                    }
                    completed++;
                }
            }
            log.info("{} task(s) in the queue. {} are completed (will clean up later)", TASKS.size(), completed);
        }
        Queue<Runnable> queue = ((ThreadPoolExecutor) SINGLE_QUEUE).getQueue();
        if (!queue.isEmpty()) {
            log.info("{} command(s) in the single-queue", queue.size());
        }
    }
}
