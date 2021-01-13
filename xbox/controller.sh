#! /bin/bash

package=$1
domain=$2
firstLetter=${domain:0:1}
otherLetter=${domain:1}
firstLetter=$(echo $firstLetter | tr '[A-Z]' '[a-z]')
domainLowerCase=$firstLetter$otherLetter

targetFolder=api/src/main/java/${package//\./\/}/controller/admin/${domain}Controller.java
cat > "${targetFolder}" <<EOF
package ${package}.controller.admin;

import io.swagger.annotations.Api;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ${package}.facade.admin.${domain}Facade;
import ${package}.sdk.data.${domain}Data;
import ${package}.sdk.form.${domain}Form;

import javax.annotation.Resource;

/**
 * @author : Joe
 * @date : 2021/1/11
 */
@RestController
@RequestMapping("/admin/${domainLowerCase}s")
@Api(tags = "${domain}管理API")
public class ${domain}Controller {
    @Resource
    private ${domain}Facade ${domainLowerCase}Facade;

    @GetMapping
    public Page<${domain}Data> get(@RequestParam int page, @RequestParam int size) {
        return ${domainLowerCase}Facade.findAll(PageRequest.of(page-1, size));
    }

    @GetMapping("/{id}")
    public ${domain}Data get(@PathVariable("id") Long id) {
        return ${domainLowerCase}Facade.get(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        ${domainLowerCase}Facade.deleteById(id);
    }
    @PostMapping
    public ${domain}Data create(@RequestBody ${domain}Form ${domainLowerCase}Form) {
        return ${domainLowerCase}Facade.save(${domainLowerCase}Form);
    }

    @PutMapping("/{id}")
    public ${domain}Data update(@PathVariable("id") Long id, @RequestBody ${domain}Form ${domainLowerCase}Form) {
        return ${domainLowerCase}Facade.update(id, ${domainLowerCase}Form);
    }
}

EOF