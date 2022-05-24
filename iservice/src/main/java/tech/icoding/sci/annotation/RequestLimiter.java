package tech.icoding.sci.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : Joe
 * @date : 2022/5/26
 */
@Target(ElementType.METHOD) // 作用到方法上
@Retention(RetentionPolicy.RUNTIME) // 运行时有效
public @interface RequestLimiter {

    /**
     * 默认时间5秒
     */
    int time() default 2 * 1000;
}
