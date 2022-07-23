package tech.icoding.sci.api.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tech.icoding.sci.sdk.common.Constants;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public final class TraceLoggerContextFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        MDC.putCloseable(Constants.LogContext.TRACE_ID, RandomStringUtils.randomAlphanumeric(9));
        log.info("记录请求{}-{}开始，并添加Trace Id", httpServletRequest.getMethod(), httpServletRequest.getRequestURL());
        long start = System.currentTimeMillis();
        filterChain.doFilter(httpServletRequest, httpServletResponse);
        log.info("记录请求{}-{}处理结束,返回码:{}, 耗时:{}毫秒", httpServletRequest.getMethod(), httpServletRequest.getRequestURL(),
                httpServletResponse.getStatus(),System.currentTimeMillis()-start);
    }
}