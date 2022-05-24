package tech.icoding.xcode.generator.builder;

import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import org.springframework.stereotype.Service;
import tech.icoding.sci.service.BaseService;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Type;

/**
 * 用于生成对应的*RepositoryClass
 * @author : Joe
 * @date : 2022/4/28
 */
public class ServiceClassBuilder extends AbstractClassBuilder{


    public TypeSpec buildTypeSpec(Class entityClass, Type repositoryType, String targetClassName) {
        final Type idType = GeneratorUtils.getFirstGenericParameter(entityClass);

        final ParameterizedTypeName superClassType = ParameterizedTypeName.get(BaseService.class, repositoryType, entityClass, idType);

        final TypeSpec.Builder builder = TypeSpec.classBuilder(targetClassName)
                .addModifiers(Modifier.PUBLIC)
                .superclass(superClassType).addAnnotation(Service.class);
        return builder.build();
    }
}
