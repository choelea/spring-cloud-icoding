package tech.icoding.xcode.generator;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import net.openhft.compiler.CompilerUtils;
import tech.icoding.xcode.generator.builder.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;

/**
 * Generate Data Service Repository Facade Controller 类
 * @author : Joe
 * @date : 2022/4/28
 */
public class SimpleGenerator {
    private String dataClassSuffix;
    private String formClassSuffix;
    private String repositoryClassSuffix;
    private String serviceClassSuffix;
    private String facadeClassSuffix;
    private String controllerClassSuffix;

    private final DataClassBuilder dataClassBuilder;
    private final FormClassBuilder formClassBuilder;
    private final DataClassBuilder repositoryClassBuilder;
    private final ServiceClassBuilder serviceClassBuilder;
    private final FacadeClassBuilder facadeClassBuilder;
    private final ControllerClassBuilder controllerClassBuilder;

    public SimpleGenerator(String dataClassSuffix, String formClassSuffix, String repositoryClassSuffix, String serviceClassSuffix,
                           String facadeClassSuffix, String controllerClassSuffix) {
        this.dataClassSuffix = dataClassSuffix;
        this.formClassSuffix = formClassSuffix;
        this.repositoryClassSuffix = repositoryClassSuffix;
        this.serviceClassSuffix = serviceClassSuffix;
        this.facadeClassSuffix = facadeClassSuffix;
        this.controllerClassSuffix = controllerClassSuffix;
        this.facadeClassBuilder = new FacadeClassBuilder();
        formClassBuilder = new FormClassBuilder();
        dataClassBuilder = new DataClassBuilder();
        repositoryClassBuilder = new RepositoryClassBuilder();
        serviceClassBuilder = new ServiceClassBuilder();
        controllerClassBuilder = new ControllerClassBuilder();
    }


    /**
     * 生成相关Java文件，包括Data Form Repository Service Facade Controller
     * @param entityClass   Domain Class
     * @param overWrite    一般设置为false， 当监测到有对应业务的Class存在的时候直接返回不做任何代码生成， 如果设置为true，则会重新生成并覆盖
     * @param mainDomain   是否是主领域，如果是false， 则只生成Data Form Repository Service 代码
     * @throws Exception
     */
    public void generateALl(Class entityClass,boolean overWrite, boolean mainDomain) throws Exception {
        String projectRoot = getProjectRoot(entityClass);
        String bizName = getBizName(entityClass);
        String parentPackageName = getParentPackage(entityClass);
        final String dataPackageName = parentPackageName + ".admin." + dataClassSuffix.toLowerCase();
        final String formPackageName = parentPackageName + ".admin." + formClassSuffix.toLowerCase();
        final String repositoryPackageName = parentPackageName + "." + repositoryClassSuffix.toLowerCase();
        final String servicePackageName = parentPackageName + "." + serviceClassSuffix.toLowerCase();
        final String facadePackageName = parentPackageName + ".admin." + facadeClassSuffix.toLowerCase();
        final String controllerPackageName = parentPackageName + ".admin." + controllerClassSuffix.toLowerCase();

        final Type[] genericInterfaces = entityClass.getGenericInterfaces();
        for (int i = 0; i < genericInterfaces.length; i++) {
            System.out.println(genericInterfaces[i]);
        }
        // Generate Data
        final TypeSpec dateTypeSpec = dataClassBuilder.buildTypeSpec(entityClass, bizName + dataClassSuffix);
        generate(overWrite,getSrcFolder(projectRoot, "sdk"), dataPackageName, dateTypeSpec);

        // Generate form Class
        final TypeSpec formTypeSpec = formClassBuilder.buildTypeSpec(entityClass, bizName + formClassSuffix);
        generate(overWrite,getSrcFolder(projectRoot, "sdk"), formPackageName, formTypeSpec);


        // Generate Repository
        final TypeSpec  repositoryTypeSpec = repositoryClassBuilder.buildTypeSpec(entityClass, bizName + repositoryClassSuffix);
        generate(overWrite,getSrcFolder(projectRoot, "iservice"), repositoryPackageName, repositoryTypeSpec);


        // Generator Service
        final Class<?> repositoryClass = Class.forName(getFullClassName(repositoryPackageName, repositoryTypeSpec.name));
        final TypeSpec serviceTypeSpec = serviceClassBuilder.buildTypeSpec(entityClass,repositoryClass, bizName + serviceClassSuffix);
        generate(overWrite,getSrcFolder(projectRoot, "iservice"), servicePackageName, serviceTypeSpec);

        if(!mainDomain){ // 针对子域，只生成到Repository这个层面
            return;
        }

        // Generator Facade
        final Class<?> serviceClass = Class.forName(getFullClassName(servicePackageName, serviceTypeSpec.name));
        final Class<?> dataClass = Class.forName(getFullClassName(dataPackageName, dateTypeSpec.name));
        final Class<?> formClass = Class.forName(getFullClassName(formPackageName, formTypeSpec.name));
        final TypeSpec facadeTypeSpec = facadeClassBuilder.buildTypeSpec(entityClass, dataClass, formClass,serviceClass, bizName + facadeClassSuffix);
        generate(overWrite,getSrcFolder(projectRoot, "facade"), facadePackageName, facadeTypeSpec);

        // Generator Controller
        final Class<?> facadeClass = Class.forName(getFullClassName(facadePackageName, facadeTypeSpec.name));
        final TypeSpec controllerTypeSpec = controllerClassBuilder.buildTypeSpec(entityClass, dataClass, formClass, facadeClass, bizName + controllerClassSuffix, bizName);
        generate(overWrite, getSrcFolder( projectRoot, "api"),controllerPackageName,controllerTypeSpec);
    }

    protected void generate(boolean overWrite, String srcFolder,String packageName, TypeSpec typeSpec) throws Exception {
        preGenerateCheck(overWrite,packageName, typeSpec.name);
        File file = new File(srcFolder);
        JavaFile javaFile = JavaFile.builder(packageName,typeSpec ).build();
        javaFile.writeTo(file);
        loadClassFromFile(file, packageName, typeSpec.name);
    }

    /**
     * 生成前校验逻辑， 如果overwrite为false，则需要做如下校验：
     * 判断判断Class是否存在，存在则抛出异常。
     * @param packageName
     * @param simpleName
     * @return
     */
    private void preGenerateCheck(boolean overwrite, String packageName, String simpleName) throws Exception {
        if(overwrite) return;
        final String fullClassName = getFullClassName(packageName, simpleName);
        try {
            Class.forName(fullClassName);
        } catch (ClassNotFoundException e) {
            return;
        }
        throw new Exception("There is Class exist with the same name:"+fullClassName+ ". If you want to overwrite it please set overwrite to true");
    }

    protected String getSrcFolder(Class entityClazz) {
        final URL location = entityClazz.getProtectionDomain().getCodeSource().getLocation();
        File classFolder = new File(location.getFile());
        return classFolder.getParentFile().getParent() + "/src/main/java";
    }

    protected String getSrcFolder(String projectRoot,  String module) {
        return projectRoot + "/" + module+"/src/main/java";
    }

    /**
     * 获取项目的根目录
     * @param entityClazz
     * @return
     */
    protected String getProjectRoot(Class entityClazz) {
        final URL location = entityClazz.getProtectionDomain().getCodeSource().getLocation();
        File classFolder = new File(location.getFile());
        return classFolder.getParentFile().getParentFile().getParent();
    }

    protected String getBizName(Class entityClazz){
        final String simpleName = entityClazz.getSimpleName();
        int end = simpleName.indexOf("Entity");
        return entityClazz.getSimpleName().substring(0, end );
    }

    protected String getParentPackage(Class entityClazz){
        final String name = entityClazz.getPackage().getName();
        final int end = name.lastIndexOf(".");
        return name.substring(0, end);
    }

    protected String getFullClassName(String packageName, String name){
        return packageName + "." + name;
    }

    /**
     * Get the full path of java file
     * @param directory  the source folder
     * @param packageName  package name
     * @param name the simple name of class
     * @return
     */
    protected String getJavaFilePath(File directory, String packageName, String name) throws IOException {
        Path outputDirectory = directory.toPath();
        if (!packageName.isEmpty()) {
            for (String packageComponent : packageName.split("\\.")) {
                outputDirectory = outputDirectory.resolve(packageComponent);
            }
        }

        Path outputPath = outputDirectory.resolve(name + ".java");
        return outputPath.toString();
    }
    /**
     * Load Class From File
     * @param file
     * @param packageName
     * @param name
     * @throws ClassNotFoundException
     * @throws MalformedURLException
     */
    protected void loadClassFromFile(File file, String packageName, String name) throws ClassNotFoundException, IOException {
        CompilerUtils.loadFromResource(getFullClassName(packageName, name), getJavaFilePath(file,packageName, name));
    }
}
