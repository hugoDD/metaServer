package cn.granitech.variantorm.exception;

public class IllegalFieldValueException extends Exception {
    public IllegalFieldValueException(String message) {
        super(message);
    }

    public IllegalFieldValueException() {}

    public IllegalFieldValueException(Throwable cause) {
        super(cause);
    }

    public IllegalFieldValueException(String message, Throwable cause) {
        super(message, cause);
    }
}
