

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
#### 对外 - sdk
#### 基础数据服务 - iservice
#### 业务处理逻辑 - facade
#### 接口Web层 - api

