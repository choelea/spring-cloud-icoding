## 生产archetype模板
参考：https://www.jianshu.com/p/724a9fa7b37a
```
mvn clean archetype:create-from-project -Dinteractive=true
cd target/generated-sources/archetype/
mvn install
mvn archetype:crawl
```

## 使用archetype模板
mvn archetype:generate -DarchetypeCatalog=local



## 开发手册
开发规范以阿里的开发规范：[Java开发手册v1.5.0 20华山版.pdf](http://tech.jiu-shu.com/Work-Related/Java-v1.5.0.pdf) 为基础来补充和优化。 
如果内容中重叠的，以本README为主; 动手开发前，请先熟读 `Java开发手册》v1.5.0 20华山版.pdf`。

### 模块及主要职责
模块依赖关系: api -> facade -> iservice -> sdk; 
（虽然iservice依赖sdk，但是要尽量避免在iservice里面使用Data和Form类，只有sdk中定义的常量， 才可以iservice使用）

#### 接口DTO - sdk
此模块儿依赖项不多，不继承父类。因此此模块构建出来的jar是可以提供给消费方使用的。 sdk主要有两类Class：
- Data  名为*Data.java的是返回DTO
- Form  名为*Form.java的是请求DTO

#### 基础数据服务 - iservice
主要包含模型定义， ORM， 数据库访问， 不带有业务色彩。
> iservice 不应该依赖*Data 和 *Form的类; DTO转换应该在Facade模块儿完成。 

#### 业务处理逻辑 - facade
主要业务逻辑，包括对第三方服务的调用。 

#### 接口Web层 - api
API接口暴露模块儿， 一些拦截器/过滤器在这里实现；同时整个服务的相关启动配置也在这个模块儿的`*.config`包里实现。
初始化需要加载的逻辑可以放在`tech.icoding.sci.listener.ContextRefreshedListener`中实现。



## 关于Devops
可以通过命令行参数指定版本号构建; `mvn -Drevision=1.1.1 clean package`。 具体详情可以参考文档 [https://maven.apache.org/maven-ci-friendly.html](https://maven.apache.org/maven-ci-friendly.html). 





