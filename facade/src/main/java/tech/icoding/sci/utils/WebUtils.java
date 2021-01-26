package tech.icoding.sci.utils;

import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * @author : Joe
 * @date : 2021/1/26
 */
public abstract class WebUtils {
    /**
     * 拼接get参数
     *
     * @param baseUrl      baseUrl 例如 https://www.baidu.com/
     * @param parameterMap parameter map
     **/
    public static URI handleUrlParameters(String baseUrl, Map<String, ? extends Object> parameterMap) {
        Assert.hasText(baseUrl, "baseUrl 不能为空");
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        if (parameterMap != null && !parameterMap.isEmpty()) {
            parameterMap.forEach((key, value) -> {
                if(Objects.nonNull(value)){
                    params.put(key, Collections.singletonList(String.valueOf(value)));
                }
            });
        }
        return builder.queryParams(params).build().encode().toUri();
    }
}
