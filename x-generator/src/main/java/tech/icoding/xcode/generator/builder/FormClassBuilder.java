package tech.icoding.xcode.generator.builder;


import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.lang.model.element.Modifier;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.io.Serializable;
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

        builder.addField(generateSerialVersionId());

        final Field[] declaredFields = entityClass.getDeclaredFields();

        for (int i = 0; i < declaredFields.length; i++) {
            Field field = declaredFields[i];

            if(!isFieldExcluded(field.getName())){ // ignore id

                final ManyToOne manyToOne = field.getAnnotation(ManyToOne.class);
                final OneToMany oneToMany = field.getAnnotation(OneToMany.class);
                final OneToOne oneToOne = field.getAnnotation(OneToOne.class);
                if(manyToOne != null || oneToMany!= null || oneToOne != null){
                    final Type firstGenericParameter = GeneratorUtils.getFirstGenericParameter(field.getDeclaringClass());
                    if(oneToMany !=null ){
                        final ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(field.getType(),firstGenericParameter);
                        final FieldSpec.Builder fieldBuilder = FieldSpec.builder(parameterizedTypeName, field.getName()).addModifiers(Modifier.PRIVATE);
                        builder.addField(fieldBuilder.build());
                    } else {
                        final FieldSpec.Builder fieldBuilder = FieldSpec.builder(firstGenericParameter, field.getName()).addModifiers(Modifier.PRIVATE);
                        builder.addField(fieldBuilder.build());
                    }

                }else{
                    final FieldSpec.Builder fieldBuilder = FieldSpec.builder(field.getGenericType(), field.getName()).addModifiers(Modifier.PRIVATE);
                    builder.addField(fieldBuilder.build());
                }
            }
        }
        return builder.build();
    }


    /**
     * 判断属性是否在排除的之外
     * @param fieldName
     * @return
     */
    protected boolean isFieldExcluded(String fieldName){
        if(IDENTIFIER_NAME.equals(fieldName) || SERIAL_VERSION_UID.equals(fieldName)){
            return true;
        }
        return false;
    }
}
