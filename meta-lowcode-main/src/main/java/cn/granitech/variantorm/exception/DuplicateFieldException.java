package cn.granitech.variantorm.exception;

public class DuplicateFieldException extends RuntimeException {
    public DuplicateFieldException(String message) {
        super(message);
    }

    public DuplicateFieldException(Throwable cause) {
        super(cause);
    }

    public DuplicateFieldException() {}

    public DuplicateFieldException(String message, Throwable cause) {
        super(message, cause);
    }
}
