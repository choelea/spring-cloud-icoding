package tech.icoding.xcode.generator.builder;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用于生成对应的*ConverterClass
 * @author : Joe
 * @date : 2022/4/28
 */
public class FacadeClassBuilder extends  AbstractClassBuilder{
    private static  final String METHOD_NAME_CONVERT = "convert";
//    private static  final String VARIABLE_NAME_PAGE = "page";
//    private static  final String VARIABLE_NAME_size = "size";
    /**
     * 构建Converter类
     * @param entityClass
     * @param serviceClass
     * @param dataClass
     * @param targetClassName
     * @return
     */
    public TypeSpec buildTypeSpec(Class entityClass,Class dataClass, Class formClass, Class serviceClass,  String targetClassName) {

        final String serviceFieldName = getVariableName(serviceClass);

        final List<String> relatedEntityNames = getRelatedEntityName(entityClass);
        final List<FieldSpec> fieldSpecs = new ArrayList<>();
        fieldSpecs.add(FieldSpec.builder(serviceClass, serviceFieldName, Modifier.PRIVATE).build());

        final MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(serviceClass, serviceFieldName)
                .addStatement("this.$N = $N", serviceFieldName, serviceFieldName);

        relatedEntityNames.forEach( biz->{

            String className = biz+"Service";
            Class clazz = null;
            try {
                clazz = Class.forName(GeneratorUtils.getFullClassName(serviceClass.getPackage().getName(), className));
                constructorBuilder.addParameter(clazz, StringUtils.uncapitalize(className));
                fieldSpecs.add(FieldSpec.builder(clazz, StringUtils.uncapitalize(className), Modifier.PRIVATE).build());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });


        MethodSpec constructor = constructorBuilder.build();

        final TypeSpec.Builder builder = TypeSpec.classBuilder(targetClassName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Component.class)
//                .addField(serviceClass, serviceFieldName, Modifier.PRIVATE)
                .addFields(fieldSpecs)
                .addMethod(constructor)
                .addMethod(buildGetMethod(entityClass, dataClass, serviceClass))
                .addMethod(buildFindMethod(entityClass,dataClass, serviceClass))
                .addMethod(buildCreateMethod(entityClass,formClass,dataClass, serviceClass))
                .addMethod(buildUpdateMethod(entityClass, formClass,dataClass, serviceClass))
                .addMethod(buildDeleteMethod(entityClass, serviceClass))
                .addMethod(buildConvertToEntityMethod(entityClass, formClass))
                .addMethod(buildConvertToDataMethod(entityClass, dataClass));
        return builder.build();
    }

    /**
     * Build get method which will get entity by given id and convert it to data.
     * @param entityClass
     * @param dataClass
     * @param serviceClass
     * @return
     */
    private MethodSpec buildGetMethod(Class entityClass, Class dataClass, Class serviceClass){
        return MethodSpec.methodBuilder("get")
                .addModifiers(Modifier.PUBLIC)
                .returns(dataClass)
                .addParameter(getIdType(entityClass), IDENTIFIER_NAME)
                .addStatement("final $T $N = $N.find($N)", entityClass, getVariableName(entityClass),getVariableName(serviceClass), IDENTIFIER_NAME)
                .addStatement("final $T $N = $N($N)", dataClass,getVariableName(dataClass), METHOD_NAME_CONVERT, getVariableName(entityClass))
                .addStatement("return $N", getVariableName(dataClass))
                .addJavadoc("Get by ID")
                .build();
    }

//    final PageRequest pageRequest = PageRequest.of(page, size);
//    final Page<CourseEntity> entityPage = courseService.find(pageRequest);
//
//    final List<CourseData> dataList = entityPage.getContent().stream().map(courseEntity -> {
//        return convert(courseEntity);
//    }).collect(Collectors.toList());


    private MethodSpec buildFindMethod(Class entityClass, Class dataClass, Class serviceClass){

        final ParameterizedTypeName dataPageType = ParameterizedTypeName.get(Page.class, dataClass);
        final ParameterizedTypeName entityPageType = ParameterizedTypeName.get(Page.class, entityClass);
        final ParameterizedTypeName dataListType = ParameterizedTypeName.get(List.class, dataClass);
        final ParameterizedTypeName dataPageImplType = ParameterizedTypeName.get(PageImpl.class, dataClass);
        return MethodSpec.methodBuilder("find")
                .addModifiers(Modifier.PUBLIC)
                .returns(dataPageType)
                .addParameter(int.class, "page").addParameter(int.class, "size")
                .addStatement("final $T pageRequest = $T.of(page, size)", PageRequest.class, PageRequest.class)
                .addStatement("final $T entityPage = $N.find(pageRequest)", entityPageType, getVariableName(serviceClass))
                .addStatement("final $T dataList = entityPage.getContent().stream().map(entity -> {\n" +
                        "        return convert(entity);\n" +
                        "    }).collect($T.toList())", dataListType, Collectors.class)
                .addStatement("final $T dataPage = new $T(dataList, entityPage.getPageable(), entityPage.getTotalElements())", dataPageImplType, dataPageImplType)
                .addStatement("return dataPage")
                .addJavadoc("Find pageable data")
                .build();
    }

    /**
     *  构建create方法，会生成类似如下内容:
     *  <pre>{@code
     *   public CourseData create(CourseForm courseForm) {
     *     Course course = new Course();
     *     convert(courseForm, course);
     *     course = courseService.save(course);
     *     return convert(course);
     *   }
     *   }
     *   </pre>
     * @param entityClass
     * @param formClass
     * @param dataClass
     * @param serviceClass
     * @return
     */
    private MethodSpec buildCreateMethod(Class entityClass, Class formClass, Class dataClass, Class serviceClass){
        return MethodSpec.methodBuilder("create")
                .addModifiers(Modifier.PUBLIC)
                .returns(dataClass)
                .addParameter(formClass, getVariableName(formClass))
                .addStatement("$T $N = new $T()", entityClass, getVariableName(entityClass), entityClass)
                .addStatement("$N($N,$N)", METHOD_NAME_CONVERT, getVariableName(formClass), getVariableName(entityClass))
                .addStatement("$N = $N.save($N)", getVariableName(entityClass), getVariableName(serviceClass), getVariableName(entityClass))
                .addStatement("return $N($N)", METHOD_NAME_CONVERT, getVariableName(entityClass))
                .addJavadoc("Create Entity and save to database")
                .build();
    }

    /**
     * 构建更新方法
     *   public CourseData update(Long id, CourseForm courseForm) {
     *     Course course = courseService.find(id);
     *     convert(courseForm, course);
     *     course = courseService.update(course);
     *     return convert(course);
     *   }
     * @param entityClass
     * @param formClass
     * @param dataClass
     * @param serviceClass
     * @return
     */

    private MethodSpec buildUpdateMethod(Class entityClass, Class formClass,Class dataClass, Class serviceClass){
        return MethodSpec.methodBuilder("update")
                .addModifiers(Modifier.PUBLIC)
                .returns(dataClass)
                .addParameter(getIdType(entityClass), IDENTIFIER_NAME)
                .addParameter(formClass, getVariableName(formClass))
                .addStatement("$T $N = $N.find($N)", entityClass, getVariableName(entityClass), getVariableName(serviceClass), IDENTIFIER_NAME)
                .addStatement("$N($N, $N)",  METHOD_NAME_CONVERT, getVariableName(formClass),getVariableName(entityClass))
                .addStatement("$N = $N.update($N)", getVariableName(entityClass), getVariableName(serviceClass), getVariableName(entityClass))
                .addStatement("return $N($N)", METHOD_NAME_CONVERT, getVariableName(entityClass))
                .addJavadoc("Update Entity  to database")
                .build();
    }


    private MethodSpec buildDeleteMethod(Class entityClass, Class serviceClass){
        return MethodSpec.methodBuilder("delete")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(getIdType(entityClass), IDENTIFIER_NAME)
                .addStatement("$N.delete($N)", getVariableName(serviceClass), IDENTIFIER_NAME)
                .addJavadoc("Delete by ID")
                .build();
    }

    private MethodSpec buildConvertToDataMethod(Class entityClass, Class dataClass){
        return MethodSpec.methodBuilder(METHOD_NAME_CONVERT)
                .addModifiers(Modifier.PRIVATE)
                .returns(dataClass)
                .addParameter(entityClass, getVariableName(entityClass))
                .addStatement("final $T $N = new $T()", dataClass, getVariableName(dataClass),dataClass)
                .addStatement("$T.copyProperties($N, $N)", BeanUtils.class, getVariableName(entityClass), getVariableName(dataClass))
                .addComment("TODO Override logic. (Copy properties is not the best solution, but an convenient one, for special logic, add below here )")
                .addStatement("return $N", getVariableName(dataClass))
                .addJavadoc("Convert entity to data object")
                .build();
    }

    private MethodSpec buildConvertToEntityMethod(Class entityClass, Class formClass){
        return MethodSpec.methodBuilder(METHOD_NAME_CONVERT)
                .addModifiers(Modifier.PRIVATE)
//                .returns(entityClass)
                .addParameter(formClass, getVariableName(formClass))
                .addParameter(entityClass, getVariableName(entityClass))
//                .addStatement("final $T $N = new $T()", entityClass, getVariableName(entityClass),entityClass)
                .addStatement("$T.copyProperties($N, $N)", BeanUtils.class, getVariableName(formClass), getVariableName(entityClass))
                .addComment("TODO Override logic. (Copy properties is not the best solution, but an convenient one, for special logic, add below here )")
//                .addStatement("return $N", getVariableName(entityClass))
                .addJavadoc("Convert form to entity object")
                .build();
    }

}
