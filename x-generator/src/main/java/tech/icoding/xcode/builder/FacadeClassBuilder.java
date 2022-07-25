package tech.icoding.xcode.builder;

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
import tech.icoding.xcode.ClassConvention;
import tech.icoding.xcode.generator.builder.GeneratorUtils;
import tech.icoding.xcode.generator.field.ExEntityClass;
import tech.icoding.xcode.generator.field.ExField;
import tech.icoding.xcode.model.ClassTree;

import javax.lang.model.element.Modifier;
import java.util.*;
import java.util.stream.Collectors;
/**
 * 用于生成对应的*ConverterClass
 * @author : Joe
 * @date : 2022/4/28
 */
public class FacadeClassBuilder extends AbstractBuilder {
    private static  final String METHOD_NAME_CONVERT = "convert";

    private static  final String METHOD_NAME_CONVERT_TO_DETAIL = "convertToDetail";
//    private static  final String VARIABLE_NAME_PAGE = "page";
//    private static  final String VARIABLE_NAME_size = "size";

    @Override
    protected TypeSpec buildTypeSpec(ClassTree classTree, String simpleClassName)  {
        final ExEntityClass exEntityClass = classTree.getExEntityClass();
        final Class serviceClass = classTree.getServiceClz();
        final String serviceFieldName = StringUtils.uncapitalize(serviceClass.getSimpleName());
        // 获取所有创建此实体需要用到的相关实体，主要用于添加对应的Service类及Facade类
        Map<String, ExField> map = new HashMap<>();
        exEntityClass.getFormFields().forEach(exField -> {
            if(exField.isRelated()){
                map.put(exField.getName(), exField);
            }
        });
//        exEntityClass.getRelatedDataFields().forEach(exField -> {
//            map.put(exField.getName(), exField);
//        });

        //添加本实体相关的Service
        final List<FieldSpec> fieldSpecs = new ArrayList<>();
        fieldSpecs.add(FieldSpec.builder(serviceClass, serviceFieldName, Modifier.PRIVATE).build());

        //构造器构建
        final MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(serviceClass, serviceFieldName)
                .addStatement("this.$N = $N", serviceFieldName, serviceFieldName);

        //类字段和构造器添加其他service

        Set<ExEntityClass> set = new HashSet<>();//过滤掉重复
        map.values().forEach(exField -> {
            set.add(new ExEntityClass(exField.getRelatedEntityClass()));
        });
        set.forEach( exEntityClz->{
            String className = getServiceClassName(exEntityClz.getEntityClass());

            try {
                // service class
                Class clazz = Class.forName(GeneratorUtils.getFullClassName(serviceClass.getPackage().getName(), className));
                String classVariable = StringUtils.uncapitalize(className);
                //Service 的构造参数
                constructorBuilder.addParameter(clazz, classVariable)
                        .addStatement("this.$N = $N", classVariable, classVariable);
                //添加需要用到的其他实体对应的Service
                fieldSpecs.add(FieldSpec.builder(clazz, StringUtils.uncapitalize(className), Modifier.PRIVATE).build());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });


        MethodSpec constructor = constructorBuilder.build();

        // 构建其他方法
        final TypeSpec.Builder builder = TypeSpec.classBuilder(simpleClassName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Component.class)
                .addFields(fieldSpecs)
                .addMethod(constructor)
                .addMethod(buildGetMethod(exEntityClass, classTree.getBaseDataClz(), serviceFieldName))
                .addMethod(buildFindMethod(exEntityClass.getEntityClass(),classTree.getBaseDataClz(), serviceFieldName))
                .addMethod(buildCreateMethod(exEntityClass.getEntityClass(),classTree.getFormClz(),classTree.getBaseDataClz(), serviceFieldName))
                .addMethod(buildUpdateMethod(exEntityClass, classTree.getFormClz(),classTree.getBaseDataClz(), serviceFieldName))
                .addMethod(buildDeleteMethod(exEntityClass, serviceFieldName))
                .addMethod(buildConvertToEntityMethod(exEntityClass, classTree.getFormClz(), map))
                .addMethod(buildConvertToDataMethod(exEntityClass.getEntityClass(), classTree.getBaseDataClz()));
        return builder.build();
    }



    /**
     * Build get method which will get entity by given id and convert it to data.
     * @param exEntityClass
     * @param dataClass
     * @param serviceFieldName
     * @return
     */
    private MethodSpec buildGetMethod(ExEntityClass exEntityClass, Class dataClass, String serviceFieldName){
        return MethodSpec.methodBuilder("get")
                .addModifiers(Modifier.PUBLIC)
                .returns(dataClass)
                .addParameter(exEntityClass.getIdType(), ExField.IDENTIFIER_NAME)
                .addStatement("final $T entity = $N.find($N)", exEntityClass.getEntityClass(), serviceFieldName, ExField.IDENTIFIER_NAME)
                .addStatement("final $T data = $N(entity)", dataClass, METHOD_NAME_CONVERT)
                .addStatement("return data")
                .addJavadoc("Get by ID")
                .build();
    }

//    final PageRequest pageRequest = PageRequest.of(page, size);
//    final Page<CourseEntity> entityPage = courseService.find(pageRequest);
//
//    final List<CourseData> dataList = entityPage.getContent().stream().map(courseEntity -> {
//        return convert(courseEntity);
//    }).collect(Collectors.toList());


    private MethodSpec buildFindMethod(Class entityClass, Class dataClass, String serviceFieldName){

        final ParameterizedTypeName dataPageType = ParameterizedTypeName.get(Page.class, dataClass);
        final ParameterizedTypeName entityPageType = ParameterizedTypeName.get(Page.class, entityClass);
        final ParameterizedTypeName dataListType = ParameterizedTypeName.get(List.class, dataClass);
        final ParameterizedTypeName dataPageImplType = ParameterizedTypeName.get(PageImpl.class, dataClass);
        return MethodSpec.methodBuilder("find")
                .addModifiers(Modifier.PUBLIC)
                .returns(dataPageType)
                .addParameter(int.class, "page").addParameter(int.class, "size")
                .addStatement("final $T pageRequest = $T.of(page, size)", PageRequest.class, PageRequest.class)
                .addStatement("final $T entityPage = $N.find(pageRequest)", entityPageType, serviceFieldName)
                .addStatement("final $T dataList = entityPage.getContent().stream().map(entity -> convert(entity)).collect($T.toList())", dataListType, Collectors.class)
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
     * @param serviceFieldName
     * @return
     */
    private MethodSpec buildCreateMethod(Class entityClass, Class formClass, Class dataClass, String serviceFieldName){
        return MethodSpec.methodBuilder("create")
                .addModifiers(Modifier.PUBLIC)
                .returns(dataClass)
                .addParameter(formClass, "form")
                .addStatement("$T entity = new $T()", entityClass, entityClass)
                .addStatement("$N(form, entity)", METHOD_NAME_CONVERT)
                .addStatement("entity = $N.save(entity)", serviceFieldName)
                .addStatement("return $N(entity)", METHOD_NAME_CONVERT)
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
     * @param exEntityClass
     * @param formClass
     * @param dataClass
     * @param serviceFieldName
     * @return
     */

    private MethodSpec buildUpdateMethod(ExEntityClass exEntityClass, Class formClass,Class dataClass, String serviceFieldName){
        return MethodSpec.methodBuilder("update")
                .addModifiers(Modifier.PUBLIC)
                .returns(dataClass)
                .addParameter(exEntityClass.getIdType(), ExField.IDENTIFIER_NAME)
                .addParameter(formClass, "form")
                .addStatement("$T entity = $N.find($N)", exEntityClass.getEntityClass(),  serviceFieldName, ExField.IDENTIFIER_NAME)
                .addStatement("$N(form, entity)",  METHOD_NAME_CONVERT)
                .addStatement("entity = $N.update(entity)", serviceFieldName)
                .addStatement("return $N(entity)", METHOD_NAME_CONVERT)
                .addJavadoc("Update Entity  to database")
                .build();
    }


    private MethodSpec buildDeleteMethod(ExEntityClass exEntityClass, String serviceFieldName){
        return MethodSpec.methodBuilder("delete")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(exEntityClass.getIdType(), ExField.IDENTIFIER_NAME)
                .addStatement("$N.delete($N)", serviceFieldName, ExField.IDENTIFIER_NAME)
                .addJavadoc("Delete by ID")
                .build();
    }

    private MethodSpec buildConvertToDataMethod(Class entityClass, Class dataClass){
        return MethodSpec.methodBuilder(METHOD_NAME_CONVERT)
                .addModifiers(Modifier.PRIVATE)
                .returns(dataClass)
                .addParameter(entityClass, "entity")
                .addStatement("final $T data = new $T()", dataClass, dataClass)
                .addStatement("$T.copyProperties(entity, data)", BeanUtils.class)
                .addComment("TODO Override logic. (Copy properties is not the best solution, but an convenient one, for special logic, add below here )")
                .addStatement("return data")
                .addJavadoc("Convert entity to data object")
                .build();
    }

    private MethodSpec buildConvertToEntityMethod(ExEntityClass exEntityClass, Class formClass, Map<String, ExField> map){
//        final String formVariable = getVariableName(formClass); //Hard code is more easier
        final MethodSpec.Builder builder = MethodSpec.methodBuilder(METHOD_NAME_CONVERT)
                .addModifiers(Modifier.PRIVATE)
//                .returns(entityClass)
                .addParameter(formClass, "form")
                .addParameter(exEntityClass.getEntityClass(), "entity")
//                .addStatement("final $T $N = new $T()", entityClass, getVariableName(entityClass),entityClass)
                .addStatement("$T.copyProperties(form, entity)", BeanUtils.class);


        map.keySet().forEach(key->{
            final String relatedServiceVariable = StringUtils.uncapitalize(getRelatedServiceClassName(map.get(key)));
            if(map.get(key).isToManyRelation()){
                builder.addStatement("form.$N().forEach(id->{ \n" +
                        "final $T ent = $N.find(id);\n" +
                        "entity.$N().add(ent);\n" +
                        "})",  map.get(key).methodNameOfGet(),// 由于Form那边生成字段规则一直，所以这里直接用的entity来取， 如果不一致则会出现问题
                                map.get(key).getRelatedEntityClass(), relatedServiceVariable,
                                map.get(key).methodNameOfGet());
            }else if(map.get(key).isToOneRelation()){
                builder.addStatement("entity.$N($N.find(form.$N()))", map.get(key).methodNameOfSet(), relatedServiceVariable, map.get(key).methodNameOfGet());
            }
        });

        builder.addComment("TODO Override logic. (Copy properties is not the best solution, but an convenient one, for special logic, add below here )")
//                .addStatement("return $N", getVariableName(entityClass))
                .addJavadoc("Convert form to entity object");

        return builder.build();
    }


    /**
     * private UserDetailData convertToDetail(UserEntity entity) {
     *         final UserDetailData data = new UserDetailData();
     *         BeanUtils.copyProperties(entity, data);
     *
     *         data.setMainRole(roleFacade.convert(entity.getMainRole()));
     *         data.getRoles().addAll(entity.getRoles().stream().map(
     *                 roleEntity -> roleFacade.convert(roleEntity)
     *         ).collect(Collectors.toList()));
     *         return data;
     *     }
     * @param classTree
     * @return
     */
    private MethodSpec buildConvertToDetailDataMethod(ClassTree classTree){
        final Class detailDataClz = classTree.getDetailDataClz();
        final MethodSpec.Builder builder = MethodSpec.methodBuilder(METHOD_NAME_CONVERT_TO_DETAIL)
                .addModifiers(Modifier.PRIVATE)
                .returns(detailDataClz)
                .addParameter(classTree.getEntityClz(), "entity")
                .addStatement("final $T data = new $T()", detailDataClz, detailDataClz)
                .addStatement("$T.copyProperties(entity, data)", BeanUtils.class);



        return builder.addComment("TODO Override logic. (Copy properties is not the best solution, but an convenient one, for special logic, add below here )")
                .addStatement("return data")
                .addJavadoc("Convert entity to data object")
                .build();
    }


    private String getRelatedServiceClassName(ExField exField){
        final Class relatedEntityClass = exField.getRelatedEntityClass();
        return getServiceClassName(relatedEntityClass);
    }

    private String getServiceClassName(Class entityClass){
        ClassConvention classConvention = new ClassConvention(entityClass);
        return classConvention.getServiceClassSimpleName();
    }
}
