package tech.icoding.xcode;

/**
 * @author : Joe
 * @date : 2022/7/28
 */
public interface FrameworkBuilder {
    void build(Class entityClasses) throws Exception;
    void clean(Class entityClasses);
}
