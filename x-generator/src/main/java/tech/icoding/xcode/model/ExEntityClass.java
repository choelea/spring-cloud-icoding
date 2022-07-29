package tech.icoding.xcode.model;

import lombok.Data;
import tech.icoding.sci.core.annotation.Composition;
import tech.icoding.xcode.GeneratorUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 * @author : Joe
 * @date : 2022/7/25
 */
@Data
public class ExEntityClass {
    private Class entityClass;

    /**
     * 是否作为主域。 没有标注 Composition 注解的实体类，就作为主域来看待。
     */
    private boolean mainDomain;

    private final List<ExField> exFields = new ArrayList<>();
    public ExEntityClass(Class entityClass) {
        this.entityClass = entityClass;
        this.mainDomain = entityClass.getAnnotation(Composition.class) == null? true:false;
        final Field[] declaredFields = entityClass.getDeclaredFields();
        for (int i = 0; i < declaredFields.length; i++) {
            exFields.add(new ExField(declaredFields[i]));
        }
    }

    /**
     * 获取泛型Entity的ID的类型
     * @return
     */
    public Type getIdType(){
        final Type firstGenericParameter = GeneratorUtils.getFirstGenericParameter(entityClass);
        return firstGenericParameter;
    }
    /**
     * 获取Entity 类名的前半部分；比如OrderEntity，将返回 Order
     * @return
     */
    public String getBizName(){
        final String simpleName = getEntityClass().getSimpleName();
        int end = simpleName.indexOf("Entity");
        return simpleName.substring(0, end );
    }

    /**
     * 返回用于Data的字段
     * @return
     */
    public List<ExField> getBaseDataFields(){
        return getExFields().stream().filter(exField -> !exField.isExcluded() && !exField.isDataIgnore() && !exField.isRelated()).collect(Collectors.toList());
    }

    /**
     * 返回用于Detail Data的字段
     * @return
     */
    public List<ExField> getRelatedDataFields(){
        return getExFields().stream().filter(exField -> !exField.isExcluded() && !exField.isDataIgnore() && exField.isRelated()).collect(Collectors.toList());
    }
    /**
     * 返回用于Form的字段
     * @return
     */
    public List<ExField> getFormFields() {
        return getExFields().stream().filter(exField -> !exField.isId() && !exField.isExcluded() && !exField.isFormIgnore()).collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExEntityClass that = (ExEntityClass) o;
        return Objects.equals(entityClass, that.entityClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entityClass);
    }
}
