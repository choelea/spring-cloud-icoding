package tech.icoding.xcode.builder;

import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import org.springframework.stereotype.Service;
import tech.icoding.sci.core.service.BaseService;
import tech.icoding.xcode.model.ClassTree;

import javax.lang.model.element.Modifier;

/**
 * 用于生成对应的*RepositoryClass
 * @author : Joe
 * @date : 2022/4/28
 */
public class ServiceClassBuilder extends AbstractBuilder {



    @Override
    protected TypeSpec buildTypeSpec(ClassTree classTree, String simpleClassName) throws Exception {
        final ParameterizedTypeName superClassType = ParameterizedTypeName.get(BaseService.class, classTree.getRepositoryClz(), classTree.getEntityClz(), classTree.getExEntityClass().getIdType());

        final TypeSpec.Builder builder = TypeSpec.classBuilder(simpleClassName)
                .addModifiers(Modifier.PUBLIC)
                .superclass(superClassType).addAnnotation(Service.class);
        return builder.build();
    }
}
