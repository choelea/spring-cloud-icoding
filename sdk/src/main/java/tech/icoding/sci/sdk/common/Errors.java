package tech.icoding.sci.sdk.common;

/**
 * 定义异常常量
 *
 * @author ray
 */
public interface Errors {

    int AUTH_ERROR = 999999881;
    int ERROR_OTHER = 900000000; // 偷懒， 其他错误

    /***
     * 业务异常常量
     */
    interface Biz {
        ErrorCodeAndMessage ARGS_NOT_EMPTY_ERROR = ErrorCodeAndMessage.create(Result.ILLEGAL_ARGUMENT, "请求参数不能为空");
        ErrorCodeAndMessage ILLEGAL_ARGUMENT_ERROR = ErrorCodeAndMessage.create(Result.ILLEGAL_ARGUMENT, "请求参数非法");
        ErrorCodeAndMessage ACCESS_DENIED = ErrorCodeAndMessage.create(999000002,"拒绝访问");
        ErrorCodeAndMessage EXCEL_RESOLVE_ERROR = ErrorCodeAndMessage.create(999000003,"excel解析异常");
    }

    /***
     * 系统异常常量
     */
    interface Sys {
        ErrorCodeAndMessage SYS_ERROR = ErrorCodeAndMessage.create(999999999, "系统异常");
        ErrorCodeAndMessage DB_ERROR = ErrorCodeAndMessage.create(999999998,"数据库异常");
        ErrorCodeAndMessage CACHE_ERROR = ErrorCodeAndMessage.create(999999997,"缓存异常");
        ErrorCodeAndMessage NOT_FOUND_ERROR = ErrorCodeAndMessage.create(999999996,"访问资源不存在");
        ErrorCodeAndMessage REPEAT_REQUEST_ERROR = ErrorCodeAndMessage.create(999999995,"重复请求");
        ErrorCodeAndMessage SERVER_NOT_RESPONSE_ERROR = ErrorCodeAndMessage.create(999999994,"服务未响应");
        ErrorCodeAndMessage ES_ERROR = ErrorCodeAndMessage.create(999999993,"ES异常");
        ErrorCodeAndMessage UNSUPPORTED_MEDIA_TYPE_ERROR = ErrorCodeAndMessage.create(999999992,"不支持当前媒体类型");
        ErrorCodeAndMessage METHOD_NOT_ALLOWED_ERROR = ErrorCodeAndMessage.create(999999991,"不支持当前方法");
        ErrorCodeAndMessage ENCRYPT_ERROR = ErrorCodeAndMessage.create(999999990,"加密失败");
        ErrorCodeAndMessage SIGN_FAILED_ERROR = ErrorCodeAndMessage.create(999999989,"签名失败");
        ErrorCodeAndMessage SIGN_VERIFY_FAILED_ERROR = ErrorCodeAndMessage.create(999999988,"签名验证失败");
        ErrorCodeAndMessage CACHE_UNLOCK_SYS_ERROR = ErrorCodeAndMessage.create(999999987,"redis解锁异常");
        ErrorCodeAndMessage REDIS_INCREASING_INVALID = ErrorCodeAndMessage.create(999999986,"self increasing digit invalid.");

        public static int REDIS_ERROR_CODE = 999999985;
    }
}