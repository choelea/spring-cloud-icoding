#! /bin/bash

package=$1
domain=$2

firstLetter=${domain:0:1}
otherLetter=${domain:1}
firstLetter=$(echo $firstLetter | tr '[A-Z]' '[a-z]')
domainLowerCase=$firstLetter$otherLetter

targetFolder=iservice/src/main/java/${package//\./\/}/entity
targetFile=$targetFolder/${domain}Entity.java
mkdir "$targetFolder"
cat > "${targetFile}" <<EOF
package ${package}.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author : Joe
 * @date : 2021/1/11
 */
@Data
@Entity
@DynamicUpdate
@Table(name = "t_${domainLowerCase}s")
public class ${domain}Entity extends BaseEntity<Long>{

}
EOF