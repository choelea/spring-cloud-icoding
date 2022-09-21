package tech.icoding.xcode;

import tech.icoding.xcode.builder.*;
import tech.icoding.xcode.model.ClassConvention;
import tech.icoding.xcode.model.ClassTree;

/**
 * 四层模型(sdk, iservice, facade, api)代码构建
 * @author : Joe
 * @date : 2022/7/28
 */
public class FourLayerFrameworkBuilder implements FrameworkBuilder{
    private String dataClassSuffix = "Data";
    private String formClassSuffix = "Form";
    private String repositoryClassSuffix = "Repository";
    private String serviceClassSuffix = "Service";
    private String facadeClassSuffix = "Facade";
    private String controllerClassSuffix = "Controller";

    private final String[] MODULES = {"sdk", "iservice", "facade", "api"};
    private final String[] MODULE_FOLDERS = new String[4];

    private final AbstractBuilder baseDataClassBuilder = new BaseDataClassBuilder();
    private final AbstractBuilder formClassBuilder = new FormClassBuilder();
    private final AbstractBuilder detailDataClassBuilder = new DetailDataClassBuilder();
    private final AbstractBuilder repositoryClassBuilder = new RepositoryClassBuilder();
    private final AbstractBuilder serviceClassBuilder = new ServiceClassBuilder();
    private final AbstractBuilder facadeClassBuilder = new FacadeClassBuilder();
    private final AbstractBuilder controllerClassBuilder = new ControllerClassBuilder();
    public FourLayerFrameworkBuilder(){
        final String projectRoot = GeneratorUtils.getProjectRoot(); // 工程的根目录
        for (int i = 0; i < MODULES.length; i++) {
            MODULE_FOLDERS[i] =  projectRoot + "/" + MODULES[i] + "/src/main/java";  // 设置每个模块的java源文件路径
        }
    }
    @Override
    public void build(Class entityClasses, int deep) throws Exception {

        ClassConvention classConvention = new ClassConvention(entityClasses);
        ClassTree classTree = new ClassTree(entityClasses);

        // 构建iService层
        final Class repositoryClz = repositoryClassBuilder.build(classTree, MODULE_FOLDERS[1], classConvention.getRepositoryClassPackage(),classConvention.getRepositoryClassSimpleName());
        classTree.setRepositoryClz(repositoryClz);

        final Class serviceClz = serviceClassBuilder.build(classTree, MODULE_FOLDERS[1], classConvention.getServiceClassPackage(), classConvention.getServiceClassSimpleName());
        classTree.setServiceClz(serviceClz);

        if(deep < 2) return; // 只构建Repo 和 Service

        //构建SDK层的Data和Form
        final Class baseDataClz = baseDataClassBuilder.build(classTree, MODULE_FOLDERS[0], classConvention.getBaseDataClassPackage(), classConvention.getBaseDataClassSimpleName());
        classTree.setBaseDataClz(baseDataClz);

        final Class formClz = formClassBuilder.build(classTree, MODULE_FOLDERS[0], classConvention.getFormClassPackage(), classConvention.getFormClassSimpleName());
        classTree.setFormClz(formClz);

        final Class detailDataClz = detailDataClassBuilder.build(classTree, MODULE_FOLDERS[0], classConvention.getDetailDataClassPackage(), classConvention.getDetailDataClassSimpleName());
        classTree.setDetailDataClz(detailDataClz);

        // 构建facade层
        final Class facadeClz = facadeClassBuilder.build(classTree, MODULE_FOLDERS[2], classConvention.getFacadeClassPackage(), classConvention.getFacadeClassSimpleName());
        classTree.setFacadeClz(facadeClz);


        if(deep < 2) return; // 只构建Repo 和 Service
        // 构建API层

        final Class controllerClz = controllerClassBuilder.build(classTree, MODULE_FOLDERS[3], classConvention.getControllerClassPackage(), classConvention.getControllerClassSimpleName());
        classTree.setControllerClz(controllerClz);

    }

    @Override
    public void clean(Class entityClasses) {

    }
}
