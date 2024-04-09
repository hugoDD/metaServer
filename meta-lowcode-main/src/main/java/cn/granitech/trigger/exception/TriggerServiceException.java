package cn.granitech.trigger.exception;


public class TriggerServiceException
        extends RuntimeException {
    public TriggerServiceException() {
    }

    public TriggerServiceException(String message) {
        super(message);
    }

    public TriggerServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public TriggerServiceException(Throwable cause) {
        super(cause);
    }
}



