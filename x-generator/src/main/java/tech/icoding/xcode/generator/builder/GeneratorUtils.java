package tech.icoding.xcode.generator.builder;

import javax.persistence.ManyToOne;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : Joe
 * @date : 2022/4/29
 */
public class GeneratorUtils {
    public static Type getFirstGenericParameter(Class clazz){
        return ((ParameterizedType)clazz.getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    /**
     * Try to find related Entity Class. (Though OneToOne OneToMany ManyToOne annotation)
     * @param entityClazz
     * @return
     */
    public static List<Class> findRelatedEntityClass(Class entityClazz){
        List<Class> list = new ArrayList<>();
        final Field[] declaredFields = entityClazz.getDeclaredFields();

        for (int i = 0; i < declaredFields.length; i++) {
            Field field = declaredFields[i];
            final ManyToOne manyToOne = field.getAnnotation(ManyToOne.class);

        }
        return list;
    }

}
