package tech.icoding.xcode.generator.builder;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

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
}
