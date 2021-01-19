package tech.icoding.sci.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.icoding.sci.filter.TraceLoggerContextFilter;

/**
 * 系统配置类
 * @author : Joe
 * @date : 2021/1/19
 */
@Configuration
public class AppConfig {

    @Bean
    public FilterRegistrationBean<TraceLoggerContextFilter> loggingFilter(TraceLoggerContextFilter traceLoggerContextFilter){
        FilterRegistrationBean<TraceLoggerContextFilter> registrationBean
                = new FilterRegistrationBean<>();

        registrationBean.setFilter(traceLoggerContextFilter);
        registrationBean.addUrlPatterns("/*");

        return registrationBean;
    }
}
