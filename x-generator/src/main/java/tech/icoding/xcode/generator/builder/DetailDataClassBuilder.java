package tech.icoding.xcode.generator.builder;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import lombok.Data;
import tech.icoding.sci.core.annotation.DataIgnore;
import tech.icoding.sci.sdk.common.BaseData;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

import static tech.icoding.xcode.generator.builder.GeneratorUtils.*;

/**
 * 用于生成对应的*DataClass
 * @author : Joe
 * @date : 2022/4/28
 */
public class DetailDataClassBuilder extends AbstractClassBuilder{

    public TypeSpec buildTypeSpec(Class entityClass, String targetClassName) {
        final Type firstGenericParameter = GeneratorUtils.getFirstGenericParameter(entityClass);
        final ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(ClassName.get(BaseData.class),
                ClassName.get(firstGenericParameter));
        Class sourceClass = entityClass;
        final TypeSpec.Builder builder = TypeSpec.classBuilder(targetClassName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Data.class).superclass(parameterizedTypeName);

        //
        builder.addField(generateSerialVersionId());

        final Field[] declaredFields = sourceClass.getDeclaredFields();
        for (int i = 0; i < declaredFields.length; i++) {
            Field field = declaredFields[i];
            if( !isFieldExcluded(field.getName()) && !isDataIgnored(field)){
                if (isToOneRelationField(field)){
                    final FieldSpec.Builder fieldBuilder = FieldSpec.builder(getToManyFieldType(field), field.getName()).addModifiers(Modifier.PRIVATE);
                    builder.addField(fieldBuilder.build());
                } else if(isToManyRelationField(field)) {
                    final FieldSpec.Builder fieldBuilder = FieldSpec.builder(getToManyFieldType(field), field.getName()).addModifiers(Modifier.PRIVATE);
                    builder.addField(fieldBuilder.build());
                } else {
                    final FieldSpec.Builder fieldBuilder = FieldSpec.builder(field.getGenericType(), field.getName()).addModifiers(Modifier.PRIVATE);
                    builder.addField(fieldBuilder.build());
                }
            }
        }
        return builder.build();
    }

    protected boolean isFieldExcluded(String fieldName){
        if(SERIAL_VERSION_UID.equals(fieldName) || DEL_FLAG.equals(fieldName)){
            return true;
        }
        return false;
    }

    protected boolean isDataIgnored(Field field){
        return field.getAnnotation(DataIgnore.class)==null?false:true;
    }

}
