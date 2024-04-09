package cn.granitech.variantorm.exception;

public class InvalidEntityException extends RuntimeException {
    public InvalidEntityException(String message) {
        super(message);
    }

    public InvalidEntityException() {}

    public InvalidEntityException(Throwable cause) {
        super(cause);
    }

    public InvalidEntityException(String message, Throwable cause) {
        super(message, cause);
    }
}
