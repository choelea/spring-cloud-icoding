package tech.icoding.xcode.generator.builder;

import com.squareup.javapoet.FieldSpec;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.util.StringUtils;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Type;

/**
 * 公共抽象父类
 * @author : Joe
 * @date : 2022/4/28
 */
public abstract class AbstractClassBuilder{
    protected static final String IDENTIFIER_NAME ="id";
    protected static final String SERIAL_VERSION_UID = "serialVersionUID";
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

    /**
     * 生成 serialVersionUID 字段
     * @return
     */
    protected FieldSpec generateSerialVersionId(){
        return FieldSpec.builder(long.class, SERIAL_VERSION_UID, Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer("$N", RandomUtils.nextLong(10000000000000000L, 999999999999999999L)+"l").build();
    }


}
