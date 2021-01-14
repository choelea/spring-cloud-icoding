#! /bin/bash

package=$1
domain=$2

targetFolder=iservice/src/main/java/${package//\./\/}/repository
targetFile=$targetFolder/${domain}Repository.java
mkdir "$targetFolder"
cat > "${targetFile}" <<EOF
package ${package}.repository;

import org.springframework.data.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ${package}.entity.${domain}Entity;

/**
 * @author : Joe
 * @date : 2021/1/11
 */
@Repository
public interface ${domain}Repository extends JpaRepository<${domain}Entity, Long> {
}
EOF