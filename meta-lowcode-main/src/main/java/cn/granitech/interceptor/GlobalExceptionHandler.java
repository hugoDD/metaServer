package cn.granitech.interceptor;

import cn.granitech.exception.ServiceException;
import cn.granitech.util.ResponseHelper;
import cn.granitech.web.pojo.ResponseBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({Exception.class})
    public ResponseBean exceptionHandler(HttpServletRequest req, Exception e) {
        e.printStackTrace();
        return ResponseHelper.fail(null, StringUtils.isBlank(e.getMessage()) ? "后台错误，请查看日志" : e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({ServiceException.class})
    public ResponseBean ServiceException(HttpServletRequest req, ServiceException e) {
        e.printStackTrace();
        return ResponseHelper.serviceError(null, StringUtils.isBlank(e.getMessage()) ? "业务异常" : e.getMessage());
    }
}
