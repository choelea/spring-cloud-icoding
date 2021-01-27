#! /bin/bash

package=$1
domain=$2
targetFolder=facade/src/main/java/${package//\./\/}/converter/todata
targetFile=$targetFolder/${domain}DataConverter.java
mkdir "$targetFolder"
cat > "${targetFile}" <<EOF
package ${package}.converter.todata;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ${package}.sdk.common.Constants;
import ${package}.entity.${domain}Entity;
import ${package}.sdk.data.admin.${domain}Data;

/**
 * @author : Joe
 * @date : 2021/1/6
 */
@Component
public class ${domain}DataConverter implements Converter<${domain}Entity, ${domain}Data> {
    @Override
    public ${domain}Data convert(${domain}Entity source) {
        ${domain}Data target = new ${domain}Data();
        target.setId(source.getId());
        target.setCreateDate(source.getCreatedDate().format(Constants.DATE_TIME_FORMATTER));
        target.setLastModifiedDate(source.getLastModifiedDate().format(Constants.DATE_TIME_FORMATTER));
        // TODO 设置 data 内容
        return target;
    }
}

EOF