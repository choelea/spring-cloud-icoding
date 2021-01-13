#! /bin/bash

package=$1
domain=$2

targetFolder=facade/src/main/java/${package//\./\/}/converter/todto/${domain}DataConverter.java
cat > "${targetFolder}" <<EOF
package ${package}.facade.converter.todto;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ${package}.entity.${domain}Entity;
import ${package}.sdk.data.${domain}Data;

/**
 * @author : Joe
 * @date : 2021/1/6
 */
@Component
public class ${domain}DataConverter implements Converter<${domain}Entity, ${domain}Data> {
    @Override
    public ${domain}Data convert(${domain}Entity source) {
        ${domain}Data target = new ${domain}Data();

        // TODO 设置 data 内容
        return target;
    }
}

EOF