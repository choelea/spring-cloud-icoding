package tech.icoding.sci.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

/**
 * RestTemplate配置类
 *
 * @author 肖晟鹏
 * @email xiaocpa@digitalchina.com
 * @date 2020-11-04
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({HttpClientProperties.class})
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(@Autowired HttpClientProperties httpClientProperties){

        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager;

        if(httpClientProperties.getKeepAlive().isEnabled()){
             //长连接保持时间
            poolingHttpClientConnectionManager =
                    new PoolingHttpClientConnectionManager(httpClientProperties.getKeepAlive().getTime(), TimeUnit.MILLISECONDS);
        }
        else {
            // 不需要长连接
            poolingHttpClientConnectionManager =
                    new PoolingHttpClientConnectionManager();
        }

        // 设置最大连接数
        poolingHttpClientConnectionManager.setMaxTotal(httpClientProperties.getMaxTotal());
        // 单路由的并发数
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(httpClientProperties.getMaxPerRoute());

        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        httpClientBuilder.setConnectionManager(poolingHttpClientConnectionManager);

        // 开启重试,并设置重试次数
        httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(httpClientProperties.getRetry().getCount(),httpClientProperties.getRetry().isEnabled()));
        HttpClient httpClient = httpClientBuilder.build();

        if(httpClientProperties.getKeepAlive().isEnabled()) {
            //保持长链接配置，keep-alive
            httpClientBuilder.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());
        }


        HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        // 连接超时配置
        httpComponentsClientHttpRequestFactory.setConnectTimeout(httpClientProperties.getConnectionTimeout());
        // 连接读取超时配置
        httpComponentsClientHttpRequestFactory.setReadTimeout(httpClientProperties.getReadTimeout());

        // 连接池不够用时候等待时间
        httpComponentsClientHttpRequestFactory.setConnectionRequestTimeout(httpClientProperties.getConnectionRequestTimeout());

        // 缓冲请求数据，POST大量数据，设定为true 需要占用内存
        httpComponentsClientHttpRequestFactory.setBufferRequestBody(httpClientProperties.isEnabledBufferRequestBody());

        RestTemplate restTemplate = new RestTemplate(httpComponentsClientHttpRequestFactory);
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler());

        return restTemplate;
    }

}