#! /bin/bash

package=$1
domain=$2

targetFolder=sdk/src/main/java/${package//\./\/}/sdk/data/admin
targetFile=$targetFolder/${domain}Data.java
mkdir "$targetFolder"
cat > "${targetFile}" <<EOF
package ${package}.sdk.data.admin;

import java.io.Serializable;
import lombok.Data;
/**
 * @author : Joe
 * @date : 2021/1/11
 */
@Data
public class ${domain}Data implements Serializable{
    private Long id;
    private String createDate;
    private String lastModifiedDate;
}
EOF