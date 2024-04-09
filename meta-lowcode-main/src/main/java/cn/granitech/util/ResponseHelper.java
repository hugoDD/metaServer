package cn.granitech.util;

import cn.granitech.web.pojo.ResponseBean;

/**
 * @author ly-dourx
 */
public class ResponseHelper {
    private static final String SUCCESS = "success";

    public static <T> ResponseBean<T> ok() {
        return ok(null, SUCCESS);
    }

    public static <T> ResponseBean<T> ok(T object) {
        return ok(object, SUCCESS);
    }

    public static <T> ResponseBean<T> ok(T object, String message) {
        return new ResponseBean<>(200, null, message, object);
    }

    public static <T> ResponseBean<T> fail(String error) {
        return new ResponseBean<>(500, error, null, null);
    }

    public static <T> ResponseBean<T> fail(T object, String error) {
        return new ResponseBean<>(500, error, null, object);
    }

    public static <T> ResponseBean<T> serviceError(T object, String error) {
        return new ResponseBean<>(201, error, null, object);
    }
}
