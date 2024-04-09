package cn.granitech.business.task;

public class TaskResultState {
    private int completed;
    private long elapsedTime;
    private String errorMessage;
    private boolean finish;
    private boolean isInterrupted;
    private double progress;
    private int succeeded;
    private int total;

    public TaskResultState(HeavyTask<?> task) {
        this.total = task.getTotal();
        this.progress = task.getCompletedPercent();
        this.completed = task.getCompleted();
        this.succeeded = task.getSucceeded();
        this.finish = task.isCompleted();
        this.isInterrupted = task.isInterrupted();
        this.elapsedTime = task.getElapsedTime();
        this.errorMessage = task.getErrorMessage();
    }

    public int getTotal() {
        return this.total;
    }

    public void setTotal(int total2) {
        this.total = total2;
    }

    public double getProgress() {
        return this.progress;
    }

    public void setProgress(double progress2) {
        this.progress = progress2;
    }

    public int getCompleted() {
        return this.completed;
    }

    public void setCompleted(int completed2) {
        this.completed = completed2;
    }

    public int getSucceeded() {
        return this.succeeded;
    }

    public void setSucceeded(int succeeded2) {
        this.succeeded = succeeded2;
    }

    public boolean isFinish() {
        return this.finish;
    }

    public void setFinish(boolean finish2) {
        this.finish = finish2;
    }

    public boolean isInterrupted() {
        return this.isInterrupted;
    }

    public void setInterrupted(boolean interrupted) {
        this.isInterrupted = interrupted;
    }

    public long getElapsedTime() {
        return this.elapsedTime;
    }

    public void setElapsedTime(long elapsedTime2) {
        this.elapsedTime = elapsedTime2;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage2) {
        this.errorMessage = errorMessage2;
    }
}
