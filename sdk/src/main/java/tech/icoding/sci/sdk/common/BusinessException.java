package tech.icoding.sci.sdk.common;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 业务异常
 *
 * @author ray
 */
@Data
@AllArgsConstructor
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = -8255208523391147163L;
    /**
     * 错误信息
     */
    private ErrorCodeAndMessage error;
    /**
     * 异常对象，用于返回信息
     */
    private Object data;

    public BusinessException(ErrorCodeAndMessage error) {
        this.error = error;
    }

    public BusinessException(int errorCode, String errorMessage, String data) {
        this.error = ErrorCodeAndMessage.create(errorCode, errorMessage);
        this.data = data;
    }

    public BusinessException(int errorCode, String errorMessage) {
        this.error = ErrorCodeAndMessage.create(errorCode, errorMessage);
    }

}