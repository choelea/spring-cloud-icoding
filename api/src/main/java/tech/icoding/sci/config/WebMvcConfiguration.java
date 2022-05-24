package tech.icoding.sci.config;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.SentinelWebInterceptor;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.DefaultBlockExceptionHandler;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.config.SentinelWebMvcConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.handler.MappedInterceptor;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import tech.icoding.sci.interceptor.LogInterceptor;

/***
 * Web MVC 配置
 * @author Joe
 */
@Configuration
@EnableSwagger2
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${spring.application.version}")
    private String applicationVersion;

    @Autowired
    private LogInterceptor logInterceptor;

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }


    @Bean
    public MappedInterceptor loggingInterceptor() {
        return new MappedInterceptor(new String[] { "/**" }, logInterceptor);
    }
    @Bean
    public MappedInterceptor sentinelInterceptor(){
        SentinelWebMvcConfig config = new SentinelWebMvcConfig();

        // Depending on your situation, you can choose to process the BlockException via
        // the BlockExceptionHandler or throw it directly, then handle it
        // in Spring web global exception handler.

        // config.setBlockExceptionHandler((request, response, e) -> { throw e; });

        // Use the default handler.
        config.setBlockExceptionHandler(new DefaultBlockExceptionHandler());

        // Custom configuration if necessary
        config.setHttpMethodSpecify(true);
        // By default web context is true, means that unify web context(i.e. use the default context name),
        // in most scenarios that's enough, and it could reduce the memory footprint.
        // If set it to false, entrance contexts will be separated by different URLs,
        // which is useful to support "chain" relation flow strategy.
        // We can change it and view different result in `Resource Chain` menu of dashboard.
        config.setWebContextUnify(true);
        config.setOriginParser(request -> request.getHeader("S-user"));

        return new MappedInterceptor(new String[] { "/**" }, new SentinelWebInterceptor(config));
        // Add sentinel interceptor
    }

    /**
     * For Swagger
     * @return
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * For Swagger
     * @return
     */
    protected ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(applicationName)
                .description(applicationName + "系统Swagger")
                .version(applicationVersion)
                .contact(new Contact("Joe", "http://tech.jiu-shu.com", "joe.lea@foxmail.com"))
                .build();
    }

}