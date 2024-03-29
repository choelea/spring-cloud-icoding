package tech.icoding.sci.facade.result;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import tech.icoding.sci.core.common.BusinessException;
import tech.icoding.sci.core.common.CodeAndMessage;
import tech.icoding.sci.core.common.ErrorCodeAndMessage;

import java.io.Serializable;

/**
 * 业务返回值封装 - 用于接口层
 *
 * @author ray
 */
@Data
@ToString
@NoArgsConstructor
public class Result<T> implements Serializable {

    protected final static int SUCCESS = 0;
    protected final static String SUCCESS_MSG = "SUCCESS";
    public final static int ILLEGAL_ARGUMENT  = 1000000001;
    private static final long serialVersionUID = -1529312446614691408L;
    /**
     * 业务返回值编码
     * 统一code长度6位
     */
    private int code;
    /**
     * 提示信息
     */
    private String message;
    /**
     * 当前时间
     */
    private long timestamp;
    /**
     * 用于追踪链路
     */
    private String traceId;
    /**
     * 扩展信息
     */
    private String extInfo;

    /**
     * 业务返回值
     */
    private T result;

    public Result(int code, String message, T result) {
        this.code = code;
        this.timestamp = System.currentTimeMillis();
        this.message = message;
        this.result = result;
    }

    public Result(int code, String message) {
        this.code = code;
        this.timestamp = System.currentTimeMillis();
        this.message = message;
        this.result = null;
    }

    public Result(CodeAndMessage codeAndMessage, T result) {
        this(codeAndMessage.getCode(), codeAndMessage.getMessage(), result);
    }

    public Result(CodeAndMessage codeAndMessage) {
        this(codeAndMessage, null);
    }

    /**
     * 默认返回成功
     *
     * @param result
     * @param <T>
     * @return
     */
    public static <T> Result success(T result) {
        return new Result<>(SUCCESS, SUCCESS_MSG, result);
    }

    /***
     * 默认返回成功
     * @return
     */
    public static Result<?> success() {
        return new Result(SUCCESS, SUCCESS_MSG);
    }

    /**
     * 检查是否发生异常
     */
    public void checkErrorResponse() {
        if (!this.isSuccess()) {
            throw new BusinessException(this.getCode(), this.getMessage(), null);
        }
    }

    /***
     * 解析result
     * @return
     */
    public T parseResult() {
        checkErrorResponse();
        return this.getResult();
    }

    /***
     * 返回失败
     * @param errorCode  错误码
     * @param msg  错误信息
     * @return
     */
    public static Result fail(int errorCode, String msg) {
        return new Result(errorCode, msg);
    }

    /***
     * 返回失败
     * @param error
     * @return
     */
    public static Result fail(ErrorCodeAndMessage error) {
        return fail(error.getCode(), error.getMessage());
    }

    /***
     * 是否返回成功结果
     * @return
     */
    public boolean isSuccess() {
        return SUCCESS == code ? true : false;
    }
}