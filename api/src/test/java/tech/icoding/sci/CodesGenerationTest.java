package tech.icoding.sci;

import tech.icoding.sci.core.entity.PermissionEntity;
import tech.icoding.sci.core.entity.RoleEntity;
import tech.icoding.xcode.FourLayerFrameworkBuilder;

/**
 * @author : Joe
 * @date : 2022/5/23
 */
public class CodesGenerationTest {
    public static void main(String[] args) throws Exception {
        FourLayerFrameworkBuilder fourLayerFrameworkBuilder = new FourLayerFrameworkBuilder();

        fourLayerFrameworkBuilder.build(PermissionEntity.class);
        fourLayerFrameworkBuilder.build(RoleEntity.class);
//        fourLayerFrameworkBuilder.build(UserEntity.class);
    }
}
