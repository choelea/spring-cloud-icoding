package tech.icoding.xcode.generator.builder;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeSpec;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.lang.model.element.Modifier;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * 用于生成对应的*DataClass
 * @author : Joe
 * @date : 2022/4/28
 */
@Slf4j
public class FormClassBuilder extends AbstractClassBuilder{

    public TypeSpec buildTypeSpec(Class entityClass, String targetClassName) {
         final TypeSpec.Builder builder = TypeSpec.classBuilder(targetClassName)
                .addModifiers(Modifier.PUBLIC)
                 .addSuperinterface(Serializable.class)
                .addAnnotation(Data.class);

        final Field[] declaredFields = entityClass.getDeclaredFields();

        for (int i = 0; i < declaredFields.length; i++) {
            Field field = declaredFields[i];
            if(!IDENTIFIER_NAME.equals(field.getName())){ // ignore id

                final Annotation[] annotations = field.getAnnotations();
                final Type genericType = getFieldType(field);
                final FieldSpec.Builder fieldBuilder = FieldSpec.builder(genericType, field.getName()).addModifiers(Modifier.PRIVATE);

                for (Annotation annotation : annotations) {
                    if(annotation.annotationType().getName().startsWith("javax.validation.constraints")){ // Keep the same constraints with entity
                        fieldBuilder.addAnnotation(annotation.annotationType());
                    }
                }
                builder.addField(fieldBuilder.build());

            }
        }
        return builder.build();
    }

    /**
     * 针对ManyToOne 的关系字段，获取One的主键字段类型;
     * 其他返回基本类型
     * @param field
     * @return
     */
    private Type getFieldType(Field field){

        final ManyToOne manyToOne = field.getAnnotation(ManyToOne.class);
        if(manyToOne!=null){
            log.info(field.getDeclaringClass().toString());
            log.info(field.getGenericType().getClass().toString());
            final Type firstGenericParameter = GeneratorUtils.getFirstGenericParameter(field.getDeclaringClass());
            return firstGenericParameter;
        }
        return field.getGenericType();

    }
}
