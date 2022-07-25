package tech.icoding.xcode.builder;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import net.openhft.compiler.CompilerUtils;
import org.apache.commons.lang3.RandomUtils;
import tech.icoding.xcode.generator.builder.GeneratorUtils;
import tech.icoding.xcode.generator.field.ExField;
import tech.icoding.xcode.model.ClassTree;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;

/**
 * @author : Joe
 * @date : 2022/7/28
 */
public abstract  class AbstractBuilder {
    /**
     * 生成 serialVersionUID 字段
     * @return
     */
    protected FieldSpec generateSerialVersionId(){
        return FieldSpec.builder(long.class, ExField.SERIAL_VERSION_UID, Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer("$N", RandomUtils.nextLong(10000000000000000L, 999999999999999999L)+"l").build();
    }

    protected abstract TypeSpec buildTypeSpec(ClassTree classTree, String simpleClassName) throws Exception;

    public Class build(ClassTree classTree, String srcFolder, String packageName, String simpleClassName) throws Exception {
        final TypeSpec typeSpec = buildTypeSpec(classTree, simpleClassName);
        File file = new File(srcFolder);
        JavaFile javaFile = JavaFile.builder(packageName,typeSpec ).build();
        javaFile.writeTo(file);
        return loadClassFromFile(file, packageName, typeSpec.name);
    }

    /**
     * Load Class From File
     * @param srcFolder
     * @param packageName
     * @param name
     * @throws ClassNotFoundException
     * @return
     */
    protected Class loadClassFromFile(File srcFolder, String packageName, String name) throws IOException, ClassNotFoundException {
        try {
            return Class.forName(GeneratorUtils.getFullClassName(packageName, name));
        } catch (ClassNotFoundException e) {
            return CompilerUtils.loadFromResource(GeneratorUtils.getFullClassName(packageName, name), GeneratorUtils.getJavaFilePath(srcFolder,packageName, name));
        }
    }


}
