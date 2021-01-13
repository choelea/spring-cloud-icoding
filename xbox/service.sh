#! /bin/bash


package=$1
domain=$2

targetFolder=iservice/src/main/java/${package//\./\/}/service
targetFile=$targetFolder/${domain}Service.java
mkdir "$targetFolder"
cat > "${targetFile}" <<EOF
package ${package}.service;

import org.springframework.stereotype.Service;
import ${package}.entity.${domain}Entity;
import ${package}.repository.${domain}Repository;

/**
 * @author : Joe
 * @date : 2021/1/11
 */
@Service
public class ${domain}Service extends BaseService<${domain}Repository, ${domain}Entity, Long>{
}
EOF



