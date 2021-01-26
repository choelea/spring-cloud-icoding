package tech.icoding.sci.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * RestTemplate配置属性
 *
 * @author 肖晟鹏
 * @email xiaocpa@digitalchina.com
 * @date 2020-11-04
 */
@Data
@ToString
@ConfigurationProperties(prefix = HttpClientProperties.PREFIX)
public class HttpClientProperties {

    public static final String PREFIX = "httpclient";

    /**
     * 最大连接数
     */
    private int maxTotal = 20;

    /**
     * 单路由并发数
     */
    private int maxPerRoute = 16;

    /**
     * 连接超时时间
     */
    private int connectionTimeout = 3000;

    /**
     * 连接读取超时时间
     */
    private int readTimeout = 3000;

    /**
     * 连接池不够用时候等待时间
     */
    private int connectionRequestTimeout = 3000;

    /**
     * 长连接相关配置
     */
    private KeepAlive keepAlive;

    /**
     * 重试相关配置
     */
    private Retry retry;


    /**
     * 缓冲请求数据，POST大量数据，设定为true 需要占用内存
     */
    private boolean enabledBufferRequestBody = false;

    @Data
    public static class KeepAlive{

        /**
         * 是否保持长连接
         */
        private boolean enabled = false;

        /**
         * 长连接保持时间，单位 ms
         */
        private int time = 20000;

    }

    @Data
    public static class Retry{

        /**
         * 是否开启重试
         */
        private boolean enabled = true;

        /**
         * 重试次数
         */
        private int count = 3 ;

    }

}
