#! /bin/bash

package=$1
domain=$2

targetFolder=sdk/src/main/java/${package//\./\/}/sdk/form
mkdir "$targetFolder"
targetFile=$targetFolder/${domain}Form.java

cat > "${targetFile}" <<EOF
package ${package}.sdk.form;

import lombok.Data;
/**
 * @author : Joe
 * @date : 2021/1/11
 */
@Data
public class ${domain}Form {

}
EOF