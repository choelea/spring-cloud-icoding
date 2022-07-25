package tech.icoding.xcode.builder;

import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import tech.icoding.xcode.model.ClassTree;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Type;

/**
 * 用于生成对应的*RepositoryClass
 * @author : Joe
 * @date : 2022/4/28
 */
public class RepositoryClassBuilder extends AbstractBuilder {

    @Override
    protected TypeSpec buildTypeSpec(ClassTree classTree, String simpleClassName){
        final Type idType = classTree.getExEntityClass().getIdType();
        final ParameterizedTypeName jpaSpecificationExecutor = ParameterizedTypeName.get(JpaSpecificationExecutor.class, idType);
        final ParameterizedTypeName jpaRepository = ParameterizedTypeName.get(JpaRepository.class, classTree.getEntityClz(),idType);

        final TypeSpec.Builder builder = TypeSpec.interfaceBuilder(simpleClassName)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(jpaSpecificationExecutor).addSuperinterface(jpaRepository)
                .addAnnotation(Repository.class);
        return builder.build();
    }
}
