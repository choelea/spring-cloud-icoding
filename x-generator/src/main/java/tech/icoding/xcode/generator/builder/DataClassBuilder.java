package tech.icoding.xcode.generator.builder;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import lombok.Data;
import org.apache.commons.lang3.ClassUtils;
import tech.icoding.sci.sdk.common.BaseData;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * 用于生成对应的*DataClass
 * @author : Joe
 * @date : 2022/4/28
 */
public class DataClassBuilder extends AbstractClassBuilder{

    public TypeSpec buildTypeSpec(Class entityClass, String targetClassName) {
        final Type firstGenericParameter = GeneratorUtils.getFirstGenericParameter(entityClass);
        final ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(ClassName.get(BaseData.class),
                ClassName.get(firstGenericParameter));
        Class sourceClass = entityClass;
        final TypeSpec.Builder builder = TypeSpec.classBuilder(targetClassName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Data.class).superclass(parameterizedTypeName);

        builder.addField(generateSerialVersionId());

        final Field[] declaredFields = sourceClass.getDeclaredFields();
        for (int i = 0; i < declaredFields.length; i++) {
            if( (!isFieldExcluded(declaredFields[i].getName())) && ClassUtils.isPrimitiveOrWrapper(declaredFields[i].getType())){
                builder.addField(declaredFields[i].getGenericType(),declaredFields[i].getName(), Modifier.PRIVATE);
            }
        }
        return builder.build();
    }

    protected boolean isFieldExcluded(String fieldName){
        if(SERIAL_VERSION_UID.equals(fieldName)){
            return true;
        }
        return false;
    }
}
