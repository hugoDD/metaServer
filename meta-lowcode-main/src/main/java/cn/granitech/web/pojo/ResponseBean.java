package cn.granitech.web.pojo;

public class ResponseBean<T> {
    private final Integer code;
    private final T data;
    private final String error;
    private final String message;

    public ResponseBean(Integer code2, String error2, String message2, T data2) {
        this.code = code2;
        this.error = error2;
        this.message = message2;
        this.data = data2;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getError() {
        return this.error;
    }

    public String getMessage() {
        return this.message;
    }

    public T getData() {
        return this.data;
    }
}
