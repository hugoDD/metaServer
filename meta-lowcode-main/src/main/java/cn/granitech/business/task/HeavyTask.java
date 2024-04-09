package cn.granitech.business.task;

import cn.granitech.interceptor.CallerContext;
import cn.granitech.util.SpringHelper;
import cn.granitech.variantorm.metadata.ID;
import cn.hutool.core.date.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public abstract class HeavyTask<T> implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(HeavyTask.class);
    private final Date beginTime = DateUtil.date();
    protected String errorMessage;
    private int completed = 0;
    private Date completedTime;
    private volatile boolean interrupt = false;
    private volatile boolean interruptState = false;
    private int succeeded = 0;
    private ID threadUser;
    private int total = -1;

    protected HeavyTask() {
    }

    /* access modifiers changed from: protected */
    public abstract T execute() throws Exception;

    public void setUser(ID user) {
        this.threadUser = user;
    }

    /* access modifiers changed from: protected */
    public void addCompleted() {
        this.completed++;
    }

    /* access modifiers changed from: protected */
    public Date getCompletedTime() {
        return this.completedTime;
    }

    public void setCompletedTime(Date completedTime2) {
        this.completedTime = completedTime2;
    }

    /* access modifiers changed from: protected */
    public void addSucceeded() {
        this.succeeded++;
    }

    public long getElapsedTime() {
        if (getCompletedTime() != null) {
            return getCompletedTime().getTime() - this.beginTime.getTime();
        }
        return DateUtil.date().getTime() - this.beginTime.getTime();
    }

    public int getTotal() {
        return this.total;
    }

    /* access modifiers changed from: protected */
    public void setTotal(int total2) {
        this.total = total2;
    }

    public int getCompleted() {
        return this.completed;
    }

    public double getCompletedPercent() {
        if (this.total == -1 || this.completed == 0) {
            return 0.0d;
        }
        if (this.completed < this.total) {
            return (1.0d * ((double) this.completed)) / ((double) this.total);
        }
        return 1.0d;
    }

    public boolean isCompleted() {
        return getCompletedTime() != null || (this.total != -1 && getCompleted() >= getTotal());
    }

    /* access modifiers changed from: protected */
    public void setCompleted(int completed2) {
        this.completed = completed2;
    }

    public int getSucceeded() {
        return this.succeeded;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void interrupt() {
        this.interrupt = true;
    }

    /* access modifiers changed from: protected */
    public boolean isInterrupt() {
        return this.interrupt;
    }

    /* access modifiers changed from: protected */
    public void setInterrupted() {
        this.interruptState = true;
    }

    public boolean isInterrupted() {
        return this.interruptState;
    }

    public final void run() {
        CallerContext callerContext = SpringHelper.getBean(CallerContext.class);
        if (this.threadUser != null) {
            callerContext.setCallerId(this.threadUser.getId());
        } else {
            callerContext.setCallerId("0000021-00000000000000000000000000000001");
        }
        try {
            execute();
        } catch (Exception ex) {
            log.error("Exception during task execute", ex);
            this.errorMessage = ex.getLocalizedMessage();
        } finally {
            completedAfter();
        }
    }

    /* access modifiers changed from: protected */
    public void completedAfter() {
        this.completedTime = DateUtil.date();
    }

    public String toString() {
        return "HeavyTask#" + getClass().getSimpleName();
    }
}
