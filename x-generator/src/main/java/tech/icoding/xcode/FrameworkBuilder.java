package tech.icoding.xcode;

/**
 * @author : Joe
 * @date : 2022/7/28
 */
public interface FrameworkBuilder {

    /**
     *
     * @param entityClasses
     * @param deep  构建到几层， 1 - 底层 （Repo + Service） 2 - Facade (SDK + facade) 3 - API 接口 Controller
     * @throws Exception
     */
    void build(Class entityClasses, int deep) throws Exception;
    void clean(Class entityClasses);
}
