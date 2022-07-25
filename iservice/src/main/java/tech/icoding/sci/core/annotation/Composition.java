package tech.icoding.sci.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识此实体类是组合关系的子类，无法独立存在。用于在生成代码阶段，生成代码阶段可以根据此注解来判断实体类是否需要生成业务侧相关代码。
 * @author : Joe
 * @date : 2022/7/25
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Composition {
}
