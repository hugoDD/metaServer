package cn.granitech.business.timer;

import java.util.concurrent.ScheduledFuture;

public final class ScheduledTask {
    public volatile ScheduledFuture<?> future;

    public void cancel() {
        ScheduledFuture<?> future2 = this.future;
        if (future2 != null) {
            future2.cancel(true);
        }
    }
}
