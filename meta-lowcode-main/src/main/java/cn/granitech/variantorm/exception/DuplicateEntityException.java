package cn.granitech.variantorm.exception;


public class DuplicateEntityException extends RuntimeException {
    public DuplicateEntityException(Throwable cause) {
        super(cause);
    }

    public DuplicateEntityException() {
    }

    public DuplicateEntityException(String message) {
        super(message);
    }

    public DuplicateEntityException(String message, Throwable cause) {
        super(message, cause);
    }


}
