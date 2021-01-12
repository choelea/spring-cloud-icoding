#! /bin/bash

package=$1
domain=$2

firstLetter=${domain:0:1}
otherLetter=${domain:1}
firstLetter=$(echo $firstLetter | tr '[A-Z]' '[a-z]')
domainLowerCase=$firstLetter$otherLetter

targetFile=iservice/src/main/java/${package//\./\/}/entity/${domain}Entity.java
cat > "${targetFile}" <<EOF
package ${package}.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author : Joe
 * @date : 2021/1/11
 */
@Data
@Entity
@Table(name = "t_${domainLowerCase}s")
public class ${domain}Entity extends BaseEntity<Long>{

    //private static final long serialVersionUID = -3785545392726922140L;
}
EOF