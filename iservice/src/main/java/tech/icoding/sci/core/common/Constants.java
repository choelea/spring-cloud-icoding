package tech.icoding.sci.core.common;

import java.time.format.DateTimeFormatter;

/**
 * @author : Joe
 * @date : 2021/1/14
 */
public interface Constants {
    String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    String DATE_PATTERN = "yyyy-MM-dd";
    DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATETIME_PATTERN);
    interface Topic {

    }

    interface RedisChannel {
        String CACHE_REFRESH = "CACHE:REFRESH";
    }

    interface LogContext {
        String TRACE_ID = "X-TraceId";
    }
}
