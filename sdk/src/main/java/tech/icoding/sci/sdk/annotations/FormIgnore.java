package tech.icoding.sci.sdk.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 仅做为标识，用来标识在生成具体Form表单类的时候忽略掉此属性
 * @author : Joe
 * @date : 2022/7/20
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FormIgnore {
}
