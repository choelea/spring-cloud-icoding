#! /bin/bash

package=$1
domain=$2
firstLetter=${domain:0:1}
otherLetter=${domain:1}
firstLetter=$(echo $firstLetter | tr '[A-Z]' '[a-z]')
domainLowerCase=$firstLetter$otherLetter

targetFolder=api/src/main/java/${package//\./\/}/controller/admin
targetFile=$targetFolder/${domain}Controller.java
mkdir "$targetFolder"
cat > "${targetFile}" <<EOF
package ${package}.controller.admin;

import io.swagger.annotations.Api;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ${package}.core.Result;
import ${package}.facade.admin.${domain}Facade;
import ${package}.sdk.data.admin.${domain}Data;
import ${package}.sdk.form.admin.${domain}Form;

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
    public Result get(@PathVariable("id") Long id) {
        ${domain}Data ${domainLowerCase}Data = ${domainLowerCase}Facade.get(id);
        return Result.success(${domainLowerCase}Data);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id") Long id) {
        ${domainLowerCase}Facade.deleteById(id);
        return Result.success();
    }
    @PostMapping
    public Result create(@RequestBody ${domain}Form ${domainLowerCase}Form) {
        ${domain}Data ${domainLowerCase}Data = ${domainLowerCase}Facade.save(${domainLowerCase}Form);
        return Result.success(${domainLowerCase}Data);
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable("id") Long id, @RequestBody ${domain}Form ${domainLowerCase}Form) {
        ${domain}Data ${domainLowerCase}Data = ${domainLowerCase}Facade.update(id, ${domainLowerCase}Form);
        return Result.success(${domainLowerCase}Data);
    }
}

EOF