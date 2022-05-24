package tech.icoding.xcode.generator.builder;

import com.squareup.javapoet.*;
import org.atteo.evo.inflector.English;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import tech.icoding.sci.sdk.common.PageData;

import javax.lang.model.element.Modifier;

/**
 * 用于生成对应的*ConverterClass
 * @author : Joe
 * @date : 2022/4/28
 */
public class ControllerClassBuilder extends AbstractClassBuilder {



    /**
     * 构建Converter类
     * @param entityClass
     * @param facadeClass
     * @param dataClass
     * @param targetClassName
     * @return
     */
    public TypeSpec buildTypeSpec(Class entityClass,Class dataClass, Class formClass, Class facadeClass,  String targetClassName, String bizName) {

        final String facadeFieldName = getVariableName(facadeClass);

        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(facadeClass, facadeFieldName)
                .addStatement("this.$N = $N", facadeFieldName, facadeFieldName)
                .build();

        final TypeSpec.Builder builder = TypeSpec.classBuilder(targetClassName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(AnnotationSpec.builder(RestController.class).build())
                .addAnnotation(AnnotationSpec.builder(RequestMapping.class).addMember("value", "$S", "/"+English.plural(bizName).toLowerCase()).build())
                .addField(facadeClass, facadeFieldName, Modifier.PRIVATE)
                .addMethod(constructor)
                .addMethod(buildGetMethod(entityClass, dataClass, facadeClass))
                .addMethod(buildFindMethod(entityClass,dataClass, facadeClass))
                .addMethod(buildCreateMethod(formClass,dataClass, facadeClass))
                .addMethod(buildUpdateMethod(entityClass,formClass,dataClass, facadeClass))
                .addMethod(buildDeleteMethod(entityClass, facadeClass));
        return builder.build();
    }

    /**
     * Build get method which will get entity by given id and convert it to data.
     * @param entityClass
     * @param dataClass
     * @param facadeClass
     * @return
     */
    private MethodSpec buildGetMethod(Class entityClass, Class dataClass, Class facadeClass){
        return MethodSpec.methodBuilder("get")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(AnnotationSpec.builder(GetMapping.class).addMember("value", "$S","/{"+ IDENTIFIER_NAME +"}").build())
                .returns(dataClass)
                .addParameter(ParameterSpec.builder(getIdType(entityClass), IDENTIFIER_NAME,Modifier.FINAL ).addAnnotation(PathVariable.class).build())
                .addStatement("final $T $N = $N.get($N)", dataClass, getVariableName(dataClass),getVariableName(facadeClass), IDENTIFIER_NAME)
                .addStatement("return $N", getVariableName(dataClass))
                .addJavadoc("Get by ID")
                .build();
    }

    /**
     * Build find method like below:
     * <pre>
     *     {@code
         *     @GetMapping
         *     public PageData<CourseData> find(@RequestParam(defaultValue = "1") int pageNumber,
         *                                      @RequestParam(defaultValue = "10") int pageSize) {
         *         final Page<CourseData> courseDataPage = courseFacade.find(pageNumber - 1, pageSize);
         *         return new PageData<>(courseDataPage);
         *     }
     *     }
     * </pre>
     */
    private MethodSpec buildFindMethod(Class entityClass, Class dataClass, Class facadeClass){

        final ParameterizedTypeName dataPageType = ParameterizedTypeName.get(PageData.class, dataClass);
        return MethodSpec.methodBuilder("find")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(GetMapping.class)
                .returns(dataPageType)
//                .addParameter(ParameterSpec.builder(int.class).build())
                .addParameter(ParameterSpec.builder(int.class, "pageNumber")
                            .addAnnotation(AnnotationSpec.builder(RequestParam.class).addMember("defaultValue", "$S",1).build()).build())
                .addParameter(ParameterSpec.builder(int.class, "pageSize")
                        .addAnnotation(AnnotationSpec.builder(RequestParam.class).addMember("defaultValue", "$S",10).build()).build())
                .addStatement("final $T<$T> page = $N.find(pageNumber-1, pageSize)", Page.class, dataClass, getVariableName(facadeClass))
                .addStatement("return new $T<>(page.getTotalElements(), page.getNumber(), page.getSize(), page.getContent())", PageData.class)
                .build();
    }

    private MethodSpec buildCreateMethod(Class formClass, Class dataClass, Class facadeClass){

        return MethodSpec.methodBuilder("create")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(PostMapping.class)
                .returns(dataClass)
                .addParameter(ParameterSpec.builder(formClass, getVariableName(formClass),Modifier.FINAL).addAnnotation(RequestBody.class).build())
                .addStatement("return $N.create($N)", getVariableName(facadeClass), getVariableName(formClass))
                .addJavadoc("Create")
                .build();
    }

    private MethodSpec buildUpdateMethod(Class entityClass, Class formClass, Class dataClass, Class facadeClass){

        return MethodSpec.methodBuilder("update")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(AnnotationSpec.builder(PutMapping.class).addMember("value", "$S","/{"+ IDENTIFIER_NAME +"}").build())
                .returns(dataClass)
                .addParameter(ParameterSpec.builder(getIdType(entityClass), IDENTIFIER_NAME, Modifier.FINAL).addAnnotation(PathVariable.class).build())
                .addParameter(ParameterSpec.builder(formClass, getVariableName(formClass),Modifier.FINAL).addAnnotation(RequestBody.class).build())
                .addStatement("return $N.update($N, $N)", getVariableName(facadeClass), IDENTIFIER_NAME, getVariableName(formClass))
                .addJavadoc("Create")
                .build();
    }


    private MethodSpec buildDeleteMethod(Class entityClass,  Class facadeClass){
        return MethodSpec.methodBuilder("delete")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(AnnotationSpec.builder(DeleteMapping.class).addMember("value", "$S","/{"+ IDENTIFIER_NAME +"}").build())
                .returns(void.class)
                .addParameter(ParameterSpec.builder(getIdType(entityClass), IDENTIFIER_NAME, Modifier.FINAL).addAnnotation(PathVariable.class).build())
                .addStatement("$N.delete($N)",getVariableName(facadeClass), IDENTIFIER_NAME)
//                .addStatement("return $N", getVariableName(dataClass))
                .addJavadoc("Delete by ID")
                .build();
    }

}
