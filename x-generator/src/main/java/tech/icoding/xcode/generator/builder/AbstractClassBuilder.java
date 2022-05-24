package tech.icoding.xcode.generator.builder;

import org.springframework.util.StringUtils;

import java.lang.reflect.Type;

/**
 * 公共抽象父类
 * @author : Joe
 * @date : 2022/4/28
 */
public abstract class AbstractClassBuilder{
    protected static final String IDENTIFIER_NAME ="id";
    public Type getIdType(Class entityClass) {
        return GeneratorUtils.getFirstGenericParameter(entityClass);
    }

    /**
     * 获取类的变量名字。 （类的简单名称的首字母小写）
     * @param clazz
     * @return
     */
    protected String getVariableName(Class clazz){
        return StringUtils.uncapitalize(clazz.getSimpleName());
    }


}
