package tech.icoding.xcode.generator.builder;


import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeSpec;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import tech.icoding.sci.core.annotation.FormIgnore;
import javax.lang.model.element.Modifier;
import java.io.Serializable;
import java.lang.reflect.Field;

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

            if(!isFieldExcluded(field.getName()) && !isFormIgnored(field)){ // ignore id

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


    /**
     * 检查是否包含FormIgnore的注解
     * @param field
     * @return
     */
    protected boolean isFormIgnored(Field field){
        return field.getAnnotation(FormIgnore.class)==null?false:true;
    }

    /**
     * 判断属性是否在排除的之外
     * @param fieldName
     * @return
     */
    protected boolean isFieldExcluded(String fieldName){
        if(IDENTIFIER_NAME.equals(fieldName) || SERIAL_VERSION_UID.equals(fieldName)
            || DEL_FLAG.equals(fieldName)){
            return true;
        }
        return false;
    }
}
