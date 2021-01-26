package tech.icoding.sci.facade;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import tech.icoding.sci.utils.WebUtils;

import javax.annotation.Resource;
import java.net.URI;
import java.util.Map;

/**
 * 请求第三方门面
 * @author Joe Li
 * @create 2020-11-06
 */
@Component
@Slf4j
public class RestFacade {
    @Resource
    private RestTemplate restTemplate;

    /**
     * post请求
     *
     * @param url         请求地址
     * @param headers     请求头
     * @param requestBody 请求体
     * @param clazz       返回体class对象
     * @return 返回体body
     */
    private <T> T post(String url, HttpHeaders headers, JSONObject requestBody, Class<T> clazz) {
        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);
        log.info("请求第三方URL:{}, 请求体:{}", url, entity);
        ResponseEntity<JSONObject> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                JSONObject.class);
        T t = responseEntity.getBody().toJavaObject(clazz);
        log.info("请求第三方URL:{}, 返回:{}", url, t);
        return t;
    }

    public <T> T post(String url, JSONObject requestBody, Class<T> clazz) {
        //设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return post(url, headers, requestBody, clazz);
    }

    /**
     * get请求
     *
     * @param url          请求地址
     * @param headers      请求头
     * @param requestParam 请求参数
     * @param clazz        返回体class对象
     * @return 返回体body
     */
    private <T> T get(String url, HttpHeaders headers, Map<String, Object> requestParam, Class<T> clazz) {
        //设置URI
        URI uri = WebUtils.handleUrlParameters(url, requestParam);
        log.info("请求第三方URL:{}, 请参数:{}", url, requestParam);
        HttpEntity<JSONObject> entity = new HttpEntity<>(headers);
        ResponseEntity<JSONObject> responseEntity = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                entity,
                JSONObject.class);
        T t = responseEntity.getBody().toJavaObject(clazz);
        log.info("请求第三方URL:{}, 返回:{}", url, t);
        return t;
    }

    public <T> T get(String url, Map<String, Object> requestParam, Class<T> clazz) {
        HttpHeaders headers = new HttpHeaders();
        return get(url, headers, requestParam, clazz);
    }
}