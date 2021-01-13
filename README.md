

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