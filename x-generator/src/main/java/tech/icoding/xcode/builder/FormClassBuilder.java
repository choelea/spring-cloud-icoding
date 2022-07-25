package tech.icoding.xcode.builder;


import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import tech.icoding.xcode.generator.field.ExEntityClass;
import tech.icoding.xcode.generator.field.ExField;
import tech.icoding.xcode.model.ClassTree;

import javax.lang.model.element.Modifier;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;
/**
 * 用于生成对应的*DataClass
 * @author : Joe
 * @date : 2022/4/28
 */
@Slf4j
public class FormClassBuilder extends AbstractBuilder {

    public TypeSpec buildTypeSpec(ClassTree classTree, String targetClassName) {

        ExEntityClass exEntityClass = classTree.getExEntityClass();
        final TypeSpec.Builder builder = TypeSpec.classBuilder(targetClassName)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(Serializable.class)
                .addAnnotation(Data.class);

        builder.addField(generateSerialVersionId());

        final List<ExField> exFields = exEntityClass.getFormFields();

        for (int i = 0; i < exFields.size(); i++) {
            ExField exField = exFields.get(i);
            if(exField.isToManyRelation()){
                final FieldSpec.Builder fieldBuilder = FieldSpec.builder(getToManyFieldType(exField), exField.getName()).addModifiers(Modifier.PRIVATE);
                builder.addField(fieldBuilder.build());
            } else if (exField.isToOneRelation()) {
                final FieldSpec.Builder fieldBuilder = FieldSpec.builder(exField.getRelatedEntityIdType(), exField.getName()).addModifiers(Modifier.PRIVATE);
                builder.addField(fieldBuilder.build());
            } else{
                final FieldSpec.Builder fieldBuilder = FieldSpec.builder(exField.getField().getGenericType(), exField.getName()).addModifiers(Modifier.PRIVATE);
                builder.addField(fieldBuilder.build());
            }

        }
        return builder.build();
    }


    /**
     * 获取ToMany关系字段的类型；去集合类型，泛型参数采用关系中的类的泛型的第一个参数。 比如：
     * <pre>
     *     {@code
     *     public class RoleEntity extends BaseEntity<Long>{
     *
     *
     *     ...... UserEntity 类中
     *
     *         @ManyToMany(mappedBy = "users")
     *         private Set<RoleEntity> roles;
     *     }
     *
     *     返回 Set<Long> 类型
     * </pre>
     *
     *
     *
     * @param field
     * @return
     */
    public ParameterizedTypeName getToManyFieldType(ExField exField){
        if(!exField.isToManyRelation()){
            throw new UnsupportedOperationException("只支持 toMany关系的字段");
        }
        final Type idType = exField.getRelatedEntityIdType();
        return  ParameterizedTypeName.get(exField.getField().getType(), idType);
    }
}
