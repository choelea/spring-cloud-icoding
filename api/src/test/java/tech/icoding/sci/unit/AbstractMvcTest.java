package tech.icoding.sci.unit;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import tech.icoding.sci.sdk.common.PageData;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Collections;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author : Joe
 * @date : 2022/5/9
 */
@Slf4j
public class AbstractMvcTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected <T> T  create(String uri,Class<T> resClass, Serializable form) throws Exception {
        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post(uri)
                .content(JSON.toJSONString(form))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString(Charset.forName("UTF-8"));
        return objectMapper.readValue(contentAsString, resClass);
    }
    protected <T> T update(String uri, Class<T> resClass, String content, Object ...uriVars) throws Exception {
        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .put(uri,uriVars)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString(Charset.forName("UTF-8"));
        return objectMapper.readValue(contentAsString, resClass);
    }

    /**
     *
     * @param uri
     * @param resClass
     * @param uriVars
     * @return
     * @throws Exception
     */
    protected <T> T get(String uri, Class<T> resClass, Object ...uriVars) throws Exception {
        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get(uri,uriVars)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString(Charset.forName("UTF-8"));
        return objectMapper.readValue(contentAsString, resClass);
    }

    protected <T> PageData<T> find(String uri, Class<T> resClass, MultiValueMap<String, String> params) throws Exception {
        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get(uri).params(params)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString(Charset.forName("UTF-8"));
        log.info(contentAsString);
        return objectMapper.readValue(contentAsString, PageData.class);
    }

    protected MultiValueMap<String, String> emptyMap(){
        return CollectionUtils.toMultiValueMap(Collections.EMPTY_MAP);
    }

}
