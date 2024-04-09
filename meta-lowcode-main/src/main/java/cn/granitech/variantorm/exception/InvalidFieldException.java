package cn.granitech.variantorm.exception;

public class InvalidFieldException extends RuntimeException {
    public InvalidFieldException(String message) {
        super(message);
    }

    public InvalidFieldException() {}

    public InvalidFieldException(Throwable cause) {
        super(cause);
    }

    public InvalidFieldException(String message, Throwable cause) {
        super(message, cause);
    }
}
