package tech.icoding.xcode.builder;

import com.squareup.javapoet.*;
import org.atteo.evo.inflector.English;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import tech.icoding.sci.sdk.common.PageData;
import tech.icoding.xcode.model.ExEntityClass;
import tech.icoding.xcode.model.ExField;
import tech.icoding.xcode.model.ClassTree;

import javax.lang.model.element.Modifier;

/**
 * 用于生成对应的*ConverterClass
 * @author : Joe
 * @date : 2022/4/28
 */
public class ControllerClassBuilder extends AbstractBuilder {


    @Override
    protected TypeSpec buildTypeSpec(ClassTree classTree, String simpleClassName)  {
        final Class facadeClass = classTree.getFacadeClz();
        final String facadeFieldName = StringUtils.uncapitalize(facadeClass.getSimpleName());
        final Class entityClass = classTree.getEntityClz();
        final Class dataClass = classTree.getBaseDataClz();
        final Class formClass = classTree.getFormClz();
        final String bizName = classTree.getExEntityClass().getBizName();

        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(classTree.getFacadeClz(), facadeFieldName)
                .addStatement("this.$N = $N", facadeFieldName, facadeFieldName)
                .build();

        final TypeSpec.Builder builder = TypeSpec.classBuilder(simpleClassName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(AnnotationSpec.builder(RestController.class).build())
                .addAnnotation(AnnotationSpec.builder(RequestMapping.class).addMember("value", "$S", "/"+English.plural(bizName).toLowerCase()).build())
                .addField(facadeClass, facadeFieldName, Modifier.PRIVATE)
                .addMethod(constructor)
                .addMethod(buildGetMethod(classTree.getExEntityClass(), classTree.getBaseDataClz(), facadeFieldName))
                .addMethod(buildFindMethod(classTree.getExEntityClass(),dataClass, facadeFieldName))
                .addMethod(buildCreateMethod(classTree.getFormClz(),dataClass, facadeFieldName))
                .addMethod(buildUpdateMethod(classTree.getExEntityClass(),formClass,dataClass, facadeFieldName))
                .addMethod(buildDeleteMethod(classTree.getExEntityClass(), facadeFieldName));
        return builder.build();
    }



    /**
     * Build get method which will get entity by given id and convert it to data.
     * @param entityClass
     * @param dataClass
     * @param facadeFieldName
     * @return
     */
    private MethodSpec buildGetMethod(ExEntityClass entityClass, Class dataClass, String facadeFieldName){
        return MethodSpec.methodBuilder("get")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(AnnotationSpec.builder(GetMapping.class).addMember("value", "$S","/{"+ ExField.IDENTIFIER_NAME +"}").build())
                .returns(dataClass)
                .addParameter(ParameterSpec.builder(entityClass.getIdType(), ExField.IDENTIFIER_NAME,Modifier.FINAL ).addAnnotation(PathVariable.class).build())
                .addStatement("final $T data = $N.get($N)", dataClass, facadeFieldName, ExField.IDENTIFIER_NAME)
                .addStatement("return data")
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
     *
     * @param entityClass
     * @param dataClass
     * @param facadeFieldName
     * @return
     */
    private MethodSpec buildFindMethod(ExEntityClass entityClass, Class dataClass, String facadeFieldName){

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
                .addStatement("final $T<$T> page = $N.find(pageNumber-1, pageSize)", Page.class, dataClass, facadeFieldName)
                .addStatement("return new $T<>(page.getTotalElements(), page.getNumber(), page.getSize(), page.getContent())", PageData.class)
                .build();
    }

    /**
     * 
     * @param formClass
     * @param dataClass
     * @param facadeFieldName
     * @return
     */
    private MethodSpec buildCreateMethod(Class formClass, Class dataClass, String facadeFieldName){

        return MethodSpec.methodBuilder("create")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(PostMapping.class)
                .returns(dataClass)
                .addParameter(ParameterSpec.builder(formClass, "form", Modifier.FINAL).addAnnotation(RequestBody.class).build())
                .addStatement("return $N.create(form)", facadeFieldName)
                .addJavadoc("Create")
                .build();
    }

    /**
     * 
     * @param exEntityClass
     * @param formClass
     * @param dataClass
     * @param facadeFieldName
     * @return
     */
    private MethodSpec buildUpdateMethod(ExEntityClass exEntityClass, Class formClass, Class dataClass, String facadeFieldName){

        return MethodSpec.methodBuilder("update")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(AnnotationSpec.builder(PutMapping.class).addMember("value", "$S","/{"+ ExField.IDENTIFIER_NAME +"}").build())
                .returns(dataClass)
                .addParameter(ParameterSpec.builder(exEntityClass.getIdType(), ExField.IDENTIFIER_NAME, Modifier.FINAL).addAnnotation(PathVariable.class).build())
                .addParameter(ParameterSpec.builder(formClass, "form",Modifier.FINAL).addAnnotation(RequestBody.class).build())
                .addStatement("return $N.update($N, form)", facadeFieldName, ExField.IDENTIFIER_NAME)
                .addJavadoc("Create")
                .build();
    }


    /**
     * 
     * @param exEntityClass
     * @param facadeFieldName
     * @return
     */
    private MethodSpec buildDeleteMethod(ExEntityClass exEntityClass, String facadeFieldName){
        return MethodSpec.methodBuilder("delete")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(AnnotationSpec.builder(DeleteMapping.class).addMember("value", "$S","/{"+ ExField.IDENTIFIER_NAME +"}").build())
                .returns(void.class)
                .addParameter(ParameterSpec.builder(exEntityClass.getIdType(), ExField.IDENTIFIER_NAME, Modifier.FINAL).addAnnotation(PathVariable.class).build())
                .addStatement("$N.delete($N)",facadeFieldName, ExField.IDENTIFIER_NAME)
//                .addStatement("return $N", getVariableName(dataClass))
                .addJavadoc("Delete by ID")
                .build();
    }


}
