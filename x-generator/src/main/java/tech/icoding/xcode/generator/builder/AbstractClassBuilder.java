package tech.icoding.xcode.generator.builder;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.util.StringUtils;

import javax.lang.model.element.Modifier;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 公共抽象父类
 * @author : Joe
 * @date : 2022/4/28
 */
public abstract class AbstractClassBuilder{
    protected static final String IDENTIFIER_NAME ="id";
    protected static final String SERIAL_VERSION_UID = "serialVersionUID";
    protected static final String DEL_FLAG = "delFlag";
    public Type getIdType(Class entityClass) {
        return GeneratorUtils.getFirstGenericParameter(entityClass);
    }

    /**
     * 获取类的变量名字。 （类的简单名称的首字母小写）
     * @param clazz
     * @return
     */
    protected String getVariableName(Class clazz){
        return StringUtils.uncapitalize(clazz.getSimpleName());
    }

    /**
     * 生成 serialVersionUID 字段
     * @return
     */
    protected FieldSpec generateSerialVersionId(){
        return FieldSpec.builder(long.class, SERIAL_VERSION_UID, Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer("$N", RandomUtils.nextLong(10000000000000000L, 999999999999999999L)+"l").build();
    }


    /**
     * 判断是否是OneToMany ManyToMany的关系的字段，此类关系的字段是集合类型
     * @param field
     * @return
     */
    protected boolean isToManyRelationField(Field field){
        final OneToMany oneToMany = field.getAnnotation(OneToMany.class);
        final ManyToMany manyToMany = field.getAnnotation(ManyToMany.class);
        if(oneToMany!= null || manyToMany != null){
            return true;
        }
        return false;
    }

    /**
     * 获取ToMany关系字段的类型；去集合类型，泛型参数采用关系中的类的泛型的第一个参数。 比如：
     * <pre>
     *     {@code
     *     public class RoleEntity extends BaseEntity<Long>{
     *
     *
     *     ...... UserEntity 类中
     *
     *         @ManyToMany(mappedBy = "users")
     *         private Set<RoleEntity> roles;
     *     }
     *
     *     返回 Set<Long> 类型
     * </pre>
     *
     *
     *
     * @param field
     * @return
     */
    protected ParameterizedTypeName getToManyFieldType(Field field){
        final boolean toManyRelationField = isToManyRelationField(field);
        if(!toManyRelationField){
            throw new UnsupportedOperationException("只支持 toMany关系的字段");
        }
        final Type firstGenericParameter = GeneratorUtils.getFirstGenericParameter(field.getDeclaringClass());
        return  ParameterizedTypeName.get(field.getType(),firstGenericParameter);
    }
    /**
     * 判断是否是ManyToOne 或者OneToOne关系的字段
     * @param field
     * @return
     */
    protected boolean isToOneRelationField(Field field){
        final ManyToOne manyToOne = field.getAnnotation(ManyToOne.class);
        final OneToOne oneToOne = field.getAnnotation(OneToOne.class);
        if( manyToOne != null || oneToOne != null ){
            return true;
        }
        return false;
    }

    /**
     *
     * @param entityClass
     * @return
     */
    protected List<String> getRelatedEntityName(Class entityClass){
        List<String> list = new ArrayList<>();
        final Field[] fields = entityClass.getFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            final Optional<OneToMany> oneToMany = Optional.ofNullable(field.getAnnotation(OneToMany.class));
            final Optional<ManyToOne> manyToOne = Optional.ofNullable(field.getAnnotation(ManyToOne.class));
            final Optional<OneToOne> oneToOne = Optional.ofNullable(field.getAnnotation(OneToOne.class));
            final Optional<ManyToMany> manyToMany = Optional.ofNullable(field.getAnnotation(ManyToMany.class));
            if(oneToMany.isPresent() || manyToMany.isPresent()){
                final Type type = GeneratorUtils.getFirstGenericParameter(field.getType());
                list.add(GeneratorUtils.getBizName(type.getTypeName()));
            } else if(manyToOne.isPresent() || oneToOne.isPresent()){
                list.add(GeneratorUtils.getBizName(field.getType().getSimpleName()));
            }
        }
        return list;
    }

    protected boolean hasRelation(Field field){
        final Optional<OneToMany> oneToMany = Optional.ofNullable(field.getAnnotation(OneToMany.class));
        final Optional<ManyToMany> manyToMany = Optional.ofNullable(field.getAnnotation(ManyToMany.class));
        final Optional<ManyToOne> manyToOne = Optional.ofNullable(field.getAnnotation(ManyToOne.class));
        final Optional<OneToOne> oneToOne = Optional.ofNullable(field.getAnnotation(OneToOne.class));
        return oneToMany.isPresent() || manyToMany.isPresent() || manyToOne.isPresent() || oneToOne.isPresent();
    }
}
