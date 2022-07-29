package tech.icoding.xcode.model;

import lombok.Data;

/**
 * 记录下来
 * @author : Joe
 * @date : 2022/7/29
 */
@Data
public class ClassTree {

    private ExEntityClass exEntityClass;

    private Class entityClz;
    private Class baseDataClz;
    private Class detailDataClz;
    private Class formClz;
    private Class repositoryClz;
    private Class serviceClz;
    private Class facadeClz;
    private Class controllerClz;

    public ClassTree(Class entityClz){
        this.entityClz = entityClz;
        exEntityClass = new ExEntityClass(this.entityClz);
    }
}
