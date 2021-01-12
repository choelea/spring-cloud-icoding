#! /bin/bash

package=$1
domain=$2

targetFile=sdk/src/main/java/${package//\./\/}/sdk/data/${domain}Data.java
cat > "${targetFile}" <<EOF
package ${package}.sdk.data;

import lombok.Data;
/**
 * @author : Joe
 * @date : 2021/1/11
 */
@Data
public class ${domain}Data {
    String createDate;
    String lastModifiedDate;
}
EOF