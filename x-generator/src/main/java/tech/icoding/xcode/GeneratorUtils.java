package tech.icoding.xcode;

import com.squareup.javapoet.ParameterizedTypeName;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author : Joe
 * @date : 2022/4/29
 */
public class GeneratorUtils {

    /**
     * 返回泛型的第一个参数类型
     * @param clazz
     * @return
     */
    public static Type getFirstGenericParameter(Class clazz){
        return ((ParameterizedType)clazz.getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    /**
     * 获取Entity 类名的前半部分；比如OrderEntity，将返回 Order
     * @param simpleEntityName
     * @return
     */
    public static String getBizName(String simpleEntityName){
        final String simpleName = simpleEntityName;
        int end = simpleName.indexOf("Entity");
        return simpleName.substring(0, end );
    }



    public static String getFullClassName(String packageName, String name){
        return packageName + "." + name;
    }

    /**
     * 获取项目的根目录
     * @return
     */
    public static String getProjectRoot() {
        final URL location = GeneratorUtils.class.getProtectionDomain().getCodeSource().getLocation();
        File classFolder = new File(location.getFile());
        return classFolder.getParentFile().getParentFile().getParent();
    }

    /**
     * Get the full path of java file
     * @param directory  the source folder
     * @param packageName  package name
     * @param name the simple name of class
     * @return
     */
    public static String getJavaFilePath(File directory, String packageName, String name){
        Path outputDirectory = directory.toPath();
        if (!packageName.isEmpty()) {
            for (String packageComponent : packageName.split("\\.")) {
                outputDirectory = outputDirectory.resolve(packageComponent);
            }
        }
        Path outputPath = outputDirectory.resolve(name + ".java");
        return outputPath.toString();
    }

    /**
     * 判断是否是OneToMany ManyToMany的关系的字段，此类关系的字段是集合类型
     * @param field
     * @return
     */
    public static boolean isToManyRelationField(Field field){
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
    public static ParameterizedTypeName getToManyFieldType(Field field){
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
    public static boolean isToOneRelationField(Field field){
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
    public static List<String> getRelatedEntityName(Class entityClass){
        List<String> list = new ArrayList<>();
        final Field[] fields = entityClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            final Class relatedEntityClass = getRelatedEntityClass(field);
            list.add(getBizName(relatedEntityClass.getSimpleName()));
        }
        return list;
    }

    private static Class getRelatedEntityClass(Field field){
        final Optional<OneToMany> oneToMany = Optional.ofNullable(field.getAnnotation(OneToMany.class));
        final Optional<ManyToOne> manyToOne = Optional.ofNullable(field.getAnnotation(ManyToOne.class));
        final Optional<OneToOne> oneToOne = Optional.ofNullable(field.getAnnotation(OneToOne.class));
        final Optional<ManyToMany> manyToMany = Optional.ofNullable(field.getAnnotation(ManyToMany.class));
        if(oneToMany.isPresent() || manyToMany.isPresent()){
            final Type type = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
            return (Class) type;
        } else if(manyToOne.isPresent() || oneToOne.isPresent()){
            return field.getType();
        }
        return null;
    }


    /**
     * 是否有关系存在
     * @param field
     * @return
     */
    public static boolean hasRelation(Field field){
        final Optional<OneToMany> oneToMany = Optional.ofNullable(field.getAnnotation(OneToMany.class));
        final Optional<ManyToMany> manyToMany = Optional.ofNullable(field.getAnnotation(ManyToMany.class));
        final Optional<ManyToOne> manyToOne = Optional.ofNullable(field.getAnnotation(ManyToOne.class));
        final Optional<OneToOne> oneToOne = Optional.ofNullable(field.getAnnotation(OneToOne.class));
        return oneToMany.isPresent() || manyToMany.isPresent() || manyToOne.isPresent() || oneToOne.isPresent();
    }


    public static String getTop3Package(String packageName){
        final String[] packages = packageName.split("\\.");
        if(packages.length <= 3) return packageName;

        final String[] strings = Arrays.copyOf(packages, 3);

        return StringUtils.join(strings, ".");
    }


}
