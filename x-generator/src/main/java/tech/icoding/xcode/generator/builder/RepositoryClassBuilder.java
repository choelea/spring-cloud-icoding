package tech.icoding.xcode.generator.builder;

import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Type;

/**
 * 用于生成对应的*RepositoryClass
 * @author : Joe
 * @date : 2022/4/28
 */
public class RepositoryClassBuilder extends DataClassBuilder {

    public TypeSpec buildTypeSpec(Class entityClass, String targetClassName) {
        final Type firstGenericParameter = GeneratorUtils.getFirstGenericParameter(entityClass);
        final ParameterizedTypeName jpaSpecificationExecutor = ParameterizedTypeName.get(JpaSpecificationExecutor.class, firstGenericParameter);
        final ParameterizedTypeName jpaRepository = ParameterizedTypeName.get(JpaRepository.class, entityClass,firstGenericParameter);

        final TypeSpec.Builder builder = TypeSpec.interfaceBuilder(targetClassName)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(jpaSpecificationExecutor).addSuperinterface(jpaRepository)
                .addAnnotation(Repository.class);
        return builder.build();
    }
}
