package tech.icoding.xcode.model;

import tech.icoding.xcode.GeneratorUtils;

/**
 * 各个Class名称约定
 * @author : Joe
 * @date : 2022/7/29
 */
public class ClassConvention {
    private final Class entityClass;
    private String bizName;
    private String basePackage;

    public ClassConvention(Class entityClass) {
        this.entityClass = entityClass;
        this.basePackage = GeneratorUtils.getTop3Package(entityClass.getPackage().getName());
        this.bizName = GeneratorUtils.getBizName(entityClass.getSimpleName());
    }


    /**
     * 获取基础Data类名(包括Package)
     * @return
     */
    public String getBaseDataClassSimpleName(){
        return bizName + "Data";
    }

    public String getBaseDataClassPackage(){
        return basePackage + ".sdk.data";
    }

    public String getFormClassPackage() {
        return basePackage + ".sdk.form.admin";
    }

    public String getFormClassSimpleName() {
        return bizName + "Form";
    }

    public String getDetailDataClassPackage() {
        return basePackage + ".sdk.data.detail";
    }

    public String getDetailDataClassSimpleName() {
        return bizName + "DetailData";
    }

    public String getRepositoryClassSimpleName() {
        return bizName + "Repository";
    }

    public String getRepositoryClassPackage() {
        return basePackage + ".core.repository";
    }

    public String getServiceClassSimpleName() {
        return bizName + "Service";
    }

    public String getServiceClassPackage() {
        return basePackage + ".core.service";
    }

    public String getFacadeClassSimpleName() {
        return bizName + "Facade";
    }

    public String getFacadeClassPackage() {
        return basePackage + ".facade.admin";
    }

    public String getControllerClassSimpleName() {
        return bizName + "Controller";
    }

    public String getControllerClassPackage() {
        return basePackage + ".controller.admin";
    }
}
