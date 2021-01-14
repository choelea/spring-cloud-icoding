#! /bin/bash

package=$1
domain=$2

firstLetter=${domain:0:1}
otherLetter=${domain:1}
firstLetter=$(echo $firstLetter | tr '[A-Z]' '[a-z]')
domainLowerCase=$firstLetter$otherLetter

targetFolder=facade/src/main/java/${package//\./\/}/facade/admin
targetFile=$targetFolder/${domain}Facade.java
mkdir "$targetFolder"
cat > "${targetFile}" <<EOF
package ${package}.facade.admin;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ${package}.entity.${domain}Entity;
import ${package}.sdk.data.${domain}Data;
import ${package}.sdk.form.${domain}Form;
import ${package}.service.${domain}Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : Joe
 * @date : 2021/1/11
 */
@Component
public class ${domain}Facade {
    @Resource
    private ${domain}Service ${domainLowerCase}Service;

    @Resource
    private Converter<${domain}Entity, ${domain}Data> converter;

    public ${domain}Data get(Long id){
        final ${domain}Entity ${domainLowerCase}Entity = ${domainLowerCase}Service.findById(id);
        return converter.convert(${domainLowerCase}Entity);
    }

    public Page<${domain}Data> findAll(Pageable pageable) {
        final Page<${domain}Entity> page = ${domainLowerCase}Service.findAll(pageable);
        List<${domain}Data> list = new ArrayList<>(page.getContent().size());
        for (${domain}Entity ${domainLowerCase}Entity :page.getContent()) {
            list.add(converter.convert(${domainLowerCase}Entity));
        }
        return new PageImpl<>(list, page.getPageable(), page.getTotalElements() );
    }

    public void deleteById(Long id) {
        ${domainLowerCase}Service.deleteById(id);
    }

    public ${domain}Data save(${domain}Form ${domainLowerCase}Form) {
        ${domain}Entity ${domainLowerCase}Entity = new ${domain}Entity();
        //TODO 将Form的值设置到Entity
        final ${domain}Entity u = ${domainLowerCase}Service.save(${domainLowerCase}Entity);
        return converter.convert(u);
    }

    public ${domain}Data update(Long id, ${domain}Form ${domainLowerCase}Form) {
        final ${domain}Entity ${domainLowerCase}Entity = ${domainLowerCase}Service.findById(id);
        ${domainLowerCase}Entity.setId(id);
        //TODO 将 form的值设置到entity
        ${domainLowerCase}Service.update(${domainLowerCase}Entity);
        return converter.convert(${domainLowerCase}Entity);
    }
}

EOF