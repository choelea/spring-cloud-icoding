package tech.icoding.xcode.generator;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import net.openhft.compiler.CompilerUtils;
import tech.icoding.xcode.generator.builder.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;

/**
 * Generate Data Service Repository Facade Controller 类
 * @author : Joe
 * @date : 2022/4/28
 */
public class SimpleGenerator {
    private String dataClassSuffix = "Data";
    private String formClassSuffix = "Form";
    private String repositoryClassSuffix = "Repository";
    private String serviceClassSuffix = "Service";
    private String facadeClassSuffix = "Facade";
    private String controllerClassSuffix = "Controller";

    private static final String SDK_MODULE = "sdk";
    private static final String SERVICE_MODULE = "iservice";
    private static final String FACADE_MODULE = "facade";
    private static final String CONTROLLER_MODULE = "api";

    private final DataClassBuilder dataClassBuilder = new DataClassBuilder();
    private final FormClassBuilder formClassBuilder = new FormClassBuilder();
    private final DataClassBuilder repositoryClassBuilder = new RepositoryClassBuilder();
    private final BaseDataClassBuilder baseDataClassBuilder = new BaseDataClassBuilder();
    private final ServiceClassBuilder serviceClassBuilder = new ServiceClassBuilder();
    private final FacadeClassBuilder facadeClassBuilder = new FacadeClassBuilder();
    private final ControllerClassBuilder controllerClassBuilder = new ControllerClassBuilder();

    public SimpleGenerator(){
    }
    public SimpleGenerator(String dataClassSuffix, String formClassSuffix, String repositoryClassSuffix, String serviceClassSuffix,
                           String facadeClassSuffix, String controllerClassSuffix) {
        this.dataClassSuffix = dataClassSuffix;
        this.formClassSuffix = formClassSuffix;
        this.repositoryClassSuffix = repositoryClassSuffix;
        this.serviceClassSuffix = serviceClassSuffix;
        this.facadeClassSuffix = facadeClassSuffix;
        this.controllerClassSuffix = controllerClassSuffix;
    }


    public void clean (Class entityClass) throws Exception {
        final String projectRoot = getProjectRoot(entityClass);
        final String bizName = GeneratorUtils.getBizName(entityClass.getSimpleName());
        final String dataPackageName = getBaseDataClassPackage(entityClass);
        final String formPackageName = getFormClassPackage(entityClass);
        final String repositoryPackageName = getRepositoryClassPackage(entityClass);
        final String servicePackageName = getServiceClassPackage(entityClass);
        final String facadePackageName = getFacadeClassPackage(entityClass);
        final String controllerPackageName = getControllerClassPackage(entityClass);

        String srcFolder = getSrcFolder(projectRoot, SDK_MODULE);
        String file = getJavaFilePath(new File(srcFolder), dataPackageName, bizName + dataClassSuffix);
        new File(file).deleteOnExit();

//        srcFolder = getSrcFolder(projectRoot, SDK_MODULE);
        file = getJavaFilePath(new File(srcFolder), formPackageName, bizName + formClassSuffix);
        new File(file).deleteOnExit();

        srcFolder = getSrcFolder(projectRoot, SERVICE_MODULE);
        file = getJavaFilePath(new File(srcFolder), repositoryPackageName, bizName + repositoryClassSuffix);
        new File(file).deleteOnExit();

//        srcFolder = getSrcFolder(projectRoot, SERVICE_MODULE);
        file = getJavaFilePath(new File(srcFolder), servicePackageName, bizName + serviceClassSuffix);
        new File(file).deleteOnExit();

        srcFolder = getSrcFolder(projectRoot, FACADE_MODULE);
        file = getJavaFilePath(new File(srcFolder), facadePackageName, bizName + facadeClassSuffix);
        new File(file).deleteOnExit();

        srcFolder = getSrcFolder(projectRoot, CONTROLLER_MODULE);
        file = getJavaFilePath(new File(srcFolder), controllerPackageName, bizName + controllerClassSuffix);
        new File(file).deleteOnExit();
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
        String bizName = GeneratorUtils.getBizName(entityClass.getSimpleName());
        final String dataPackageName = getBaseDataClassPackage(entityClass);
        final String formPackageName = getFormClassPackage(entityClass);
        final String repositoryPackageName = getRepositoryClassPackage(entityClass);
        final String servicePackageName = getServiceClassPackage(entityClass);
        final String facadePackageName = getFacadeClassPackage(entityClass);
        final String controllerPackageName = getControllerClassPackage(entityClass);

//        final Type[] genericInterfaces = entityClass.getGenericInterfaces();
//        for (int i = 0; i < genericInterfaces.length; i++) {
//            System.out.println(genericInterfaces[i]);
//        }
        // Generate Data
        final TypeSpec dateTypeSpec = baseDataClassBuilder.buildTypeSpec(entityClass, bizName + dataClassSuffix);
        generate(overWrite,getSrcFolder(projectRoot, SDK_MODULE), dataPackageName, dateTypeSpec);

        // Generate form Class
        final TypeSpec formTypeSpec = formClassBuilder.buildTypeSpec(entityClass, bizName + formClassSuffix);
        generate(overWrite,getSrcFolder(projectRoot, SDK_MODULE), formPackageName, formTypeSpec);

        // Generate Repository
        final TypeSpec  repositoryTypeSpec = repositoryClassBuilder.buildTypeSpec(entityClass, bizName + repositoryClassSuffix);
        generate(overWrite,getSrcFolder(projectRoot, SERVICE_MODULE), repositoryPackageName, repositoryTypeSpec);

        // Generator Service
        final Class<?> repositoryClass = Class.forName(GeneratorUtils.getFullClassName(repositoryPackageName, repositoryTypeSpec.name));
        final TypeSpec serviceTypeSpec = serviceClassBuilder.buildTypeSpec(entityClass,repositoryClass, bizName + serviceClassSuffix);
        generate(overWrite,getSrcFolder(projectRoot, SERVICE_MODULE), servicePackageName, serviceTypeSpec);

        if( !mainDomain ){ // 针对子域，只生成到Repository这个层面
            return;
        }

        // Generator Facade
        final Class<?> serviceClass = Class.forName(GeneratorUtils.getFullClassName(servicePackageName, serviceTypeSpec.name));
        final Class<?> dataClass = Class.forName(GeneratorUtils.getFullClassName(dataPackageName, dateTypeSpec.name));
        final Class<?> formClass = Class.forName(GeneratorUtils.getFullClassName(formPackageName, formTypeSpec.name));
        final TypeSpec facadeTypeSpec = facadeClassBuilder.buildTypeSpec(entityClass, dataClass, formClass,serviceClass, bizName + facadeClassSuffix);
        generate(overWrite,getSrcFolder(projectRoot, FACADE_MODULE), facadePackageName, facadeTypeSpec);

        // Generator Controller
        final Class<?> facadeClass = Class.forName(GeneratorUtils.getFullClassName(facadePackageName, facadeTypeSpec.name));
        final TypeSpec controllerTypeSpec = controllerClassBuilder.buildTypeSpec(entityClass, dataClass, formClass, facadeClass, bizName + controllerClassSuffix, bizName);
        generate(overWrite, getSrcFolder( projectRoot, CONTROLLER_MODULE),controllerPackageName,controllerTypeSpec);
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
        final String fullClassName = GeneratorUtils.getFullClassName(packageName, simpleName);
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
        CompilerUtils.loadFromResource(GeneratorUtils.getFullClassName(packageName, name), getJavaFilePath(file,packageName, name));
    }

    private String getBaseDataClassPackage(Class entityClass){
        String parentPackageName = getParentPackage(entityClass);
        return parentPackageName + ".sdk." + dataClassSuffix.toLowerCase();
    }

    private String getFormClassPackage(Class entityClass){
        String parentPackageName = getParentPackage(entityClass);
        return parentPackageName + ".sdk." + formClassSuffix.toLowerCase() + ".admin";
    }

    private String getRepositoryClassPackage(Class entityClass){
        String parentPackageName = getParentPackage(entityClass);
        return  parentPackageName + ".core." + repositoryClassSuffix.toLowerCase();
    }

    private String getServiceClassPackage(Class entityClass){
        String parentPackageName = getParentPackage(entityClass);
        return  parentPackageName + ".core." + serviceClassSuffix.toLowerCase();
    }

    private String getFacadeClassPackage(Class entityClass){
        String parentPackageName = getParentPackage(entityClass);
        return parentPackageName + ".facade." + facadeClassSuffix.toLowerCase() + ".admin";
    }

    private String getControllerClassPackage(Class entityClass){
        String parentPackageName = getParentPackage(entityClass);
        return parentPackageName + ".api." + controllerClassSuffix.toLowerCase() + ".admin";
    }

    public static String getParentPackage(Class entityClazz){
        String name = entityClazz.getPackage().getName();
        int end = name.lastIndexOf(".");
        name =  name.substring(0, end);
        end = name.lastIndexOf(".");
        return name.substring(0, end);
    }
}
