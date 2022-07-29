package tech.icoding.xcode.builder;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import lombok.Data;
import org.springframework.util.Assert;
import tech.icoding.xcode.model.ClassConvention;
import tech.icoding.xcode.GeneratorUtils;
import tech.icoding.xcode.model.ExField;
import tech.icoding.xcode.model.ClassTree;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 用于生成对应的*DataClass
 * @author : Joe
 * @date : 2022/4/28
 */
public class DetailDataClassBuilder extends AbstractBuilder {

    public TypeSpec buildTypeSpec(ClassTree classTree, String targetClassName) throws ClassNotFoundException{

        Assert.notNull(classTree.getBaseDataClz(), "cannot be null");
        final TypeSpec.Builder builder = TypeSpec.classBuilder(targetClassName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Data.class).superclass(classTree.getBaseDataClz());

        builder.addField(generateSerialVersionId());

        final List<ExField> relatedDataFields = classTree.getExEntityClass().getRelatedDataFields();
        for (int i = 0; i < relatedDataFields.size(); i++) {
            ExField exField = relatedDataFields.get(i);
            ClassConvention classConvention = new ClassConvention(exField.getRelatedEntityClass());
            String fullClassName = GeneratorUtils.getFullClassName(classConvention.getBaseDataClassPackage(),classConvention.getBaseDataClassSimpleName());
            final Class<?> dataClz = Class.forName(fullClassName);
            if(exField.isToOneRelation()){
                final FieldSpec.Builder fieldBuilder = FieldSpec.builder(dataClz, exField.getName()).addModifiers(Modifier.PRIVATE);
                builder.addField(fieldBuilder.build());
            } else if(exField.isToManyRelation()){
                final Class<?> type = exField.getField().getType();
                Class collectionClass = ArrayList.class;
                if (type.isAssignableFrom(Set.class)){
                    collectionClass = HashSet.class;
                }
                final ParameterizedTypeName fieldType = ParameterizedTypeName.get(exField.getField().getType(), dataClz);
                final FieldSpec.Builder fieldBuilder = FieldSpec.builder(fieldType, exField.getName()).addModifiers(Modifier.PRIVATE).initializer("new $T<>()", collectionClass);
                builder.addField(fieldBuilder.build());
            }
        }

        return builder.build();
    }


}
