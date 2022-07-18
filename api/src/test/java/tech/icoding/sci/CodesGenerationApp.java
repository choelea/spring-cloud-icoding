package tech.icoding.sci;

import tech.icoding.sci.entity.OrderEntity;
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
//        simpleGenerator.generateALl(OrderItemEntity.class,true,true);
        simpleGenerator.generateALl(OrderEntity.class,true,true);
    }
}
