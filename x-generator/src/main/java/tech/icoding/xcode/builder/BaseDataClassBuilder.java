package tech.icoding.xcode.builder;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import lombok.Data;
import tech.icoding.sci.sdk.common.BaseData;
import tech.icoding.xcode.model.ExEntityClass;
import tech.icoding.xcode.model.ExField;
import tech.icoding.xcode.model.ClassTree;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Field;
import java.util.List;
/**
 * 用于生成对应的基础的Data Class。 基础的Data Class 不包括关系(OneToMany  OneToOne ManyToOne ManyToMany)的字段
 * @author : Joe
 * @date : 2022/4/28
 */
public class BaseDataClassBuilder extends AbstractBuilder {

    public TypeSpec buildTypeSpec(ClassTree classTree, String simpleClassName) {

        ExEntityClass exEntityClass = classTree.getExEntityClass();
        final ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(ClassName.get(BaseData.class),
                ClassName.get(exEntityClass.getIdType()));
        final TypeSpec.Builder builder = TypeSpec.classBuilder(simpleClassName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Data.class).superclass(parameterizedTypeName);

        builder.addField(generateSerialVersionId());

        final List<ExField> exFields = exEntityClass.getBaseDataFields();

        for (int i = 0; i < exFields.size(); i++) {
            Field field = exFields.get(i).getField();
            final FieldSpec.Builder fieldBuilder = FieldSpec.builder(field.getGenericType(), field.getName()).addModifiers(Modifier.PRIVATE);
            builder.addField(fieldBuilder.build());
        }
        return builder.build();
    }
}
