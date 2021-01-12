#! /bin/bash

package=$1
domain=$2

targetFile=iservice/src/main/java/${package//\./\/}/repository/${domain}Repository.java
cat > "${targetFile}" <<EOF
package ${package}.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ${package}.entity.${domain}Entity;

/**
 * @author : Joe
 * @date : 2021/1/11
 */
@Repository
public interface ${domain}Repository extends PagingAndSortingRepository<${domain}Entity, Long> {
}
EOF