package top.charjin.oneapi.openinterface.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.charjin.oneapi.common.model.BaseResponse;
import top.charjin.oneapi.common.model.ErrorCode;
import top.charjin.oneapi.common.util.ResultUtils;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        log.error("BusinessException", e);
        return ResultUtils.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("RuntimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "系统错误");
    }

    @ExceptionHandler(Exception.class)
    public BaseResponse<?> exceptionHandler(Exception e) {
        log.error("Exception", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public BaseResponse<?> missingParameterExceptionHandler(MissingServletRequestParameterException e) {
        log.error("MissingServletRequestParameterException", e);
        return ResultUtils.error(ErrorCode.PARAMS_ERROR, "缺少参数 " + e.getParameterName());
    }


}
