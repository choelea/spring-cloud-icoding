package tech.icoding.sci.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.annotation.Primary;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import tech.icoding.sci.core.common.BusinessException;
import tech.icoding.sci.facade.result.Errors;
import tech.icoding.sci.facade.result.Result;

/***
 * 统一异常处理
 *
 * 自定义需要继承
 * @author ray
 */
@Primary
@ControllerAdvice
@ConditionalOnMissingBean(name = "exceptionHandler")
@RestController
@Slf4j
public class DefaultGlobalExceptionHandler implements ErrorController {

    @Override
    public String getErrorPath() {
        //todo 如果发生异常，默认跳转的页面
        return "/error";
    }

    @ExceptionHandler(BusinessException.class)
    public Result bizErrorHandler(BusinessException exception) {
        exception.printStackTrace();
        log.info("捕获到全局异常:{}", exception.getMessage());
        return Result.fail(exception.getError());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result methodNotSupportedHandler(HttpRequestMethodNotSupportedException exception) {
        exception.printStackTrace();
        log.info("捕获到全局异常:{}", exception.getMessage());
        return Result.fail(Errors.Sys.METHOD_NOT_ALLOWED_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result notValidParameterHandler(MethodArgumentNotValidException exception) {
        exception.printStackTrace();
        log.info("捕获到全局异常:{}", exception.getMessage());
        return Result.fail(Errors.Biz.ILLEGAL_ARGUMENT_ERROR);
    }

    @ExceptionHandler(Throwable.class)
    public Result throwableHandler(Throwable cause) {
        cause.printStackTrace();
        log.info("捕获到全局异常:{}", cause.getMessage());
        return Result.fail(Errors.Sys.SYS_ERROR.getCode(), Errors.Sys.SYS_ERROR.getMessage());
    }

}