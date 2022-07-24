package tech.icoding.sci;

import tech.icoding.sci.core.entity.OrderEntity;
import tech.icoding.sci.core.entity.OrderItemEntity;
import tech.icoding.sci.core.entity.RoleEntity;
import tech.icoding.sci.core.entity.UserEntity;
import tech.icoding.xcode.generator.SimpleGenerator;

/**
 * @author : Joe
 * @date : 2022/5/23
 */
public class CodesGenerationApp {
    public static void main(String[] args) throws Exception {
        SimpleGenerator simpleGenerator = new SimpleGenerator("Data", "Form", "Repository",
                "Service", "Facade","Controller");
//        simpleGenerator.setOverwrite(true);
//        simpleGenerator.generateALl(Company.class);
//        simpleGenerator.generateALl(User.class);

//        simpleGenerator.generateALl(Course.class);
        simpleGenerator.generateALl(UserEntity.class,true,true);
        simpleGenerator.generateALl(RoleEntity.class,true,true);
//          simpleGenerator.clean(OrderItemEntity.class);
    }
}
