package cn.granitech.variantorm.exception;
public class SqlInjectionException extends RuntimeException {
    public SqlInjectionException(String message) {
        super(message);
    }

    public SqlInjectionException(Throwable cause) {
        super(cause);
    }

    public SqlInjectionException() {}

    public SqlInjectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
