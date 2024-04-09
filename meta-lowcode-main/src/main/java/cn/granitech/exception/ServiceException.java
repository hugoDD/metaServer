package cn.granitech.exception;

public class ServiceException extends RuntimeException {
    public ServiceException() {
    }


    public ServiceException(String message, String... params) {
        super(String.format(message, params));
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }
}
