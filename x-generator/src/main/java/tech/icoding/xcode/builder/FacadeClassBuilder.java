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
import tech.icoding.xcode.model.ClassConvention;
import tech.icoding.xcode.GeneratorUtils;
import tech.icoding.xcode.model.ExEntityClass;
import tech.icoding.xcode.model.ExField;
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


        // 构建其他方法
        final TypeSpec.Builder builder = TypeSpec.classBuilder(simpleClassName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Component.class)
                .addFields(buildFieldSpec(classTree))
                .addMethod(buildConstructor(classTree))
                .addMethod(buildGetMethod(exEntityClass, classTree.getDetailDataClz(), serviceFieldName))
                .addMethod(buildFindMethod(exEntityClass.getEntityClass(),classTree.getBaseDataClz(), serviceFieldName))
                .addMethod(buildCreateMethod(exEntityClass.getEntityClass(),classTree.getFormClz(),classTree.getBaseDataClz(), serviceFieldName))
                .addMethod(buildUpdateMethod(exEntityClass, classTree.getFormClz(),classTree.getBaseDataClz(), serviceFieldName))
                .addMethod(buildDeleteMethod(exEntityClass, serviceFieldName))
                .addMethod(buildConvertToEntityMethod(exEntityClass, classTree.getFormClz()))
                .addMethod(buildConvertToDataMethod(exEntityClass.getEntityClass(), classTree.getBaseDataClz()))
                .addMethod(buildConvertToDetailDataMethod(classTree));
        return builder.build();
    }

    /**
     * 构建类成员变量， Service成员和Facade成员
     * @param classTree
     * @return
     */
    private List<FieldSpec> buildFieldSpec(ClassTree classTree){
        final List<FieldSpec> list = new ArrayList<>();
        final List<FieldSpec> fieldSpecs = new ArrayList<>();
        final Class serviceClass = classTree.getServiceClz();
        final String serviceFieldName = StringUtils.uncapitalize(serviceClass.getSimpleName());
        fieldSpecs.add(FieldSpec.builder(serviceClass, serviceFieldName, Modifier.PRIVATE).build());

        final ExEntityClass exEntityClass = classTree.getExEntityClass();
        Set<Class> classSet = new HashSet<>();
        classSet.addAll(getRelatedFacadeClassSet(exEntityClass));
        classSet.addAll(getRelatedServiceClassSet(exEntityClass));

        classSet.forEach(clz->{
            fieldSpecs.add(FieldSpec.builder(clz, StringUtils.uncapitalize(clz.getSimpleName()), Modifier.PRIVATE).build());
        });

        return list;
    }
    /**
     * 构建构造器方法
     * @param classTree
     * @return
     */
    private MethodSpec buildConstructor(ClassTree classTree){
        //构造器构建
        final ExEntityClass exEntityClass = classTree.getExEntityClass();
        final Class serviceClass = classTree.getServiceClz();
        final String serviceFieldName = StringUtils.uncapitalize(serviceClass.getSimpleName());
        final MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(serviceClass, serviceFieldName)
                .addStatement("this.$N = $N", serviceFieldName, serviceFieldName);
        Set<Class> classSet = new HashSet<>();
        classSet.addAll(getRelatedFacadeClassSet(exEntityClass));
        classSet.addAll(getRelatedServiceClassSet(exEntityClass));

        classSet.forEach(clz->{
            //Service 的构造参数
            constructorBuilder.addParameter(clz, StringUtils.uncapitalize(clz.getSimpleName()))
                    .addStatement("this.$N = $N", StringUtils.uncapitalize(clz.getSimpleName()), StringUtils.uncapitalize(clz.getSimpleName()));
        });
        return constructorBuilder.build();
    }

    /**
     * 获取相关的Service Class。 通过Form字段来获取在创建和修改的时候需要用到的其他模型的Service。
     * @param exEntityClass
     * @return
     */
    private Set<Class> getRelatedServiceClassSet(ExEntityClass exEntityClass){
        final List<ExField> formFields = exEntityClass.getRelatedFormFields();
        final Set<Class> serviceClassSet = new HashSet<>();
        formFields.forEach(exField -> {
            final Class relatedEntityClass = exField.getRelatedEntityClass();
            ClassConvention classConvention = new ClassConvention(relatedEntityClass);
            final Class<?> serviceClass;
            try {
                serviceClass = Class.forName(GeneratorUtils.getFullClassName(classConvention.getServiceClassPackage(), classConvention.getServiceClassSimpleName()));
                serviceClassSet.add(serviceClass);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        return serviceClassSet;
    }

    /**
     * 获取关联模型的FacadeClass， 用于在详情Data组装中重用。
     * @param exEntityClass
     * @return
     */
    private Set<Class> getRelatedFacadeClassSet(ExEntityClass exEntityClass){
        final List<ExField> relatedDataFields = exEntityClass.getRelatedDataFields();
        final Set<Class> facadeClassSet = new HashSet<>();
        relatedDataFields.forEach(exField -> {
            final Class relatedEntityClass = exField.getRelatedEntityClass();
            ClassConvention classConvention = new ClassConvention(relatedEntityClass);
            final Class<?> facadeClass;
            try {
                facadeClass = Class.forName(GeneratorUtils.getFullClassName(classConvention.getFacadeClassPackage(), classConvention.getFacadeClassSimpleName()));
                facadeClassSet.add(facadeClass);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        return facadeClassSet;
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
                .addStatement("final $T data = $N(entity)", dataClass, METHOD_NAME_CONVERT_TO_DETAIL)
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

    private MethodSpec buildConvertToEntityMethod(ExEntityClass exEntityClass, Class formClass){
//        final String formVariable = getVariableName(formClass); //Hard code is more easier
        final MethodSpec.Builder builder = MethodSpec.methodBuilder(METHOD_NAME_CONVERT)
                .addModifiers(Modifier.PRIVATE)
//                .returns(entityClass)
                .addParameter(formClass, "form")
                .addParameter(exEntityClass.getEntityClass(), "entity")
//                .addStatement("final $T $N = new $T()", entityClass, getVariableName(entityClass),entityClass)
                .addStatement("$T.copyProperties(form, entity)", BeanUtils.class);

        exEntityClass.getRelatedFormFields().forEach(exField->{
            final String relatedServiceVariable = StringUtils.uncapitalize(getRelatedServiceClassName(exField));
            if(exField.isToManyRelation()){
                builder.addStatement("form.$N().forEach(id->{ \n" +
                        "final $T ent = $N.find(id);\n" +
                        "entity.$N().add(ent);\n" +
                        "})",  exField.methodNameOfGet(),// 由于Form那边生成字段规则一直，所以这里直接用的entity来取， 如果不一致则会出现问题
                                exField.getRelatedEntityClass(), relatedServiceVariable,
                                exField.methodNameOfGet());
            }else if(exField.isToOneRelation()){
                builder.addStatement("entity.$N($N.find(form.$N()))", exField.methodNameOfSet(), relatedServiceVariable, exField.methodNameOfGet());
            }
        });

        builder.addComment("TODO Override logic. (Copy properties is not the best solution, but an convenient one, for special logic, add below here )")
//                .addStatement("return $N", getVariableName(entityClass))
                .addJavadoc("Convert form to entity object");

        return builder.build();
    }


    /**
     *
     * private UserDetailData convertToDetail(UserEntity entity){
     *     final UserData data = convert(entity);
     *     UserDetailData detailData = new UserDetailData();
     *     BeanUtils.copyProperties(data, detailData);
     *     entity.getRoles().forEach( roleEntity-> {
     *       detailData.getRoles().add(roleFacade.convert(roleEntity));
     *     });
     *     return detailData;
     *   }
     * @param classTree
     * @return
     */
    private MethodSpec buildConvertToDetailDataMethod(ClassTree classTree){
        final Class detailDataClz = classTree.getDetailDataClz();

        final MethodSpec.Builder builder = MethodSpec.methodBuilder(METHOD_NAME_CONVERT_TO_DETAIL)
                .addModifiers(Modifier.PRIVATE)
                .returns(detailDataClz)
                .addParameter(classTree.getEntityClz(), "entity")

                .addStatement("final $T data = $N(entity)", classTree.getBaseDataClz(), METHOD_NAME_CONVERT)
                .addStatement("final $T detailData = new $N(entity)", detailDataClz, detailDataClz)
                .addStatement("$T.copyProperties(data, detailData)", BeanUtils.class);

        final List<ExField> relatedDataFields = classTree.getExEntityClass().getRelatedDataFields();
        for (int i = 0; i < relatedDataFields.size(); i++) {
            ExField exField = relatedDataFields.get(i);
            final Class relatedEntityClass = exField.getRelatedEntityClass();
            ClassConvention classConvention = new ClassConvention(relatedEntityClass);
            if(exField.isToOneRelation()){
                builder.addStatement("data.$N($N.convert(entity.$N)", exField.methodNameOfSet(), StringUtils.uncapitalize(classConvention.getFacadeClassSimpleName()),
                        exField.methodNameOfGet());
            }else if(exField.isToManyRelation()){
                builder.addStatement("entity.$N().forEach( e -> {\n" +
                        "               detailData.$N().add($N.convert(e));\n" +
                        "            })", exField.methodNameOfGet(), exField.methodNameOfGet(),StringUtils.uncapitalize(classConvention.getFacadeClassSimpleName()));
            }
        }

        return builder.addComment("TODO Override logic. (Copy properties is not the best solution, but an convenient one, for special logic, add below here )")
                .addStatement("return detailData")
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
