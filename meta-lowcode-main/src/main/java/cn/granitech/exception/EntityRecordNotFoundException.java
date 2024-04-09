package cn.granitech.exception;


public class EntityRecordNotFoundException extends RuntimeException {
    public EntityRecordNotFoundException() {
    }

    public EntityRecordNotFoundException(String message) {
        super(message);
    }

    public EntityRecordNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityRecordNotFoundException(Throwable cause) {
        super(cause);
    }
}
