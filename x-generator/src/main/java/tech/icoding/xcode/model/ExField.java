package tech.icoding.xcode.model;


import com.squareup.javapoet.FieldSpec;
import lombok.Data;
import org.springframework.util.StringUtils;
import tech.icoding.sci.core.annotation.DataIgnore;
import tech.icoding.sci.core.annotation.FormIgnore;

import javax.lang.model.element.Modifier;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

/**
 * 扩展字段
 * @author : Joe
 * @date : 2022/7/25
 */
@Data
public class ExField{
    public static final String IDENTIFIER_NAME ="id";
    public static final String SERIAL_VERSION_UID = "serialVersionUID";
    public static final String DEL_FLAG = "delFlag";

    private Field field;

    /**
     * 字段名称
     */
    private String name;
    public ExField(Field field) {
        this.field = field;
        this.name = field.getName();
    }

    /**
     * 返回field对应的getXXMethod名称
     * @return
     */
    public String methodNameOfGet(){
        return "get" + StringUtils.capitalize(name);
    }

    public String methodNameOfSet(){
        return "set" + StringUtils.capitalize(name);
    }
    /**
     * 是否被Data DTO忽略。在生成Data类的时候将其忽略掉。
     * @return
     */
    public boolean isDataIgnore(){
        return field.getAnnotation(DataIgnore.class)!=null;
    }

    /**
     * 是否被Form DTO忽略。在生成Data类的时候将其忽略掉。
     * @return
     */
    public boolean isFormIgnore(){
        return field.getAnnotation(FormIgnore.class)!=null;
    }

    /**
     * 是否需要在生成的时候忽略掉的字段
     * @return
     */
    public boolean isExcluded(){
        return DEL_FLAG.equals(getName()) || SERIAL_VERSION_UID.equals(getName());
    }

    /**
     * 是否是ID字段
     * @return
     */
    public boolean isId(){
        return IDENTIFIER_NAME.equals(getName());
    }
    /**
     * 是否是关系字段
     * @return
     */
    public boolean isRelated(){
        final Optional<OneToMany> oneToMany = Optional.ofNullable(this.field.getAnnotation(OneToMany.class));
        final Optional<ManyToMany> manyToMany = Optional.ofNullable(this.field.getAnnotation(ManyToMany.class));
        final Optional<ManyToOne> manyToOne = Optional.ofNullable(this.field.getAnnotation(ManyToOne.class));
        final Optional<OneToOne> oneToOne = Optional.ofNullable(this.field.getAnnotation(OneToOne.class));
        return oneToMany.isPresent() || manyToMany.isPresent() || manyToOne.isPresent() || oneToOne.isPresent();
    }

    /**
     * 获取关系实体的Class
     * @return
     */
    public Class getRelatedEntityClass(){
        if(!isRelated()){
            throw new UnsupportedOperationException("只支持 关系型字段");
        }
        if(isToManyRelation()){
            return (Class)((ParameterizedType) getField().getGenericType()).getActualTypeArguments()[0];
        }else if (isToOneRelation()){
            return getField().getType();
        }else{
            throw new RuntimeException("未处理逻辑");
        }
    }


    /**
     * 获取关系实体类的主键类型
     * @return
     */
    public Type getRelatedEntityIdType(){
        if(!isRelated()){
            throw new UnsupportedOperationException("只支持 关系型字段");
        }
        if(isToManyRelation()){
            return ((ParameterizedType)getField().getDeclaringClass().getGenericSuperclass())
                    .getActualTypeArguments()[0];
        }else if (isToOneRelation()){
            return ((ParameterizedType)getField().getDeclaringClass().getGenericSuperclass())
                    .getActualTypeArguments()[0];
        } else{
            throw new RuntimeException("This is unexpected");
        }

    }

    /**
     * 判断是否是OneToMany ManyToMany的关系的字段，此类关系的字段是集合类型
     * @return
     */
    public boolean isToManyRelation(){
        final OneToMany oneToMany = getField().getAnnotation(OneToMany.class);
        final ManyToMany manyToMany = getField().getAnnotation(ManyToMany.class);
        if(oneToMany!= null || manyToMany != null){
            return true;
        }
        return false;
    }

    /**
     * 判断是否是ManyToOne 或者OneToOne关系的字段
     * @return
     */
    public  boolean isToOneRelation(){
        final ManyToOne manyToOne = getField().getAnnotation(ManyToOne.class);
        final OneToOne oneToOne = getField().getAnnotation(OneToOne.class);
        if( manyToOne != null || oneToOne != null ){
            return true;
        }
        return false;
    }

    /**
     * 构建FieldSpec
     * @return
     */
    public FieldSpec build(){
        if(!isRelated()){
            final FieldSpec.Builder fieldBuilder = FieldSpec.builder(field.getGenericType(), field.getName()).addModifiers(Modifier.PRIVATE);
            return fieldBuilder.build();
        }else{
            return null; //TODO
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExField exField = (ExField) o;
        return this.getField().equals(exField.field);
    }

    @Override
    public int hashCode() {
        return field.hashCode();
    }
}
