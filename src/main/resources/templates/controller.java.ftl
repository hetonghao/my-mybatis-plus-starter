package ${package.Controller};

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ${package.Service}.${table.serviceName};
import ${package.Entity}.${entity};
import ${cfg.voPackage}.${cfg.pageVOName};
import ${cfg.boPackage}.${cfg.BOName};
import com.baomidou.mybatisplus.core.metadata.IPage;

<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

import javax.validation.Valid;

/**
 * <p>
 * ${table.comment!} 前端控制器
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Api(tags = {"${table.comment!}"})
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>
    @Autowired
    private ${table.serviceName} ${table.serviceName?substring(1)?uncap_first};

    @PostMapping
    @ApiOperation(value = "列表", notes = "所有数据列表查询,传入pageNumber与pageSize可分页")
    public IPage<${cfg.BOName}> page(@RequestBody ${cfg.pageVOName} vo) {
        IPage page = vo.generatePage();
        ${table.serviceName?substring(1)?uncap_first}.page(page, vo);
        return page;
    }

    @GetMapping("{id}")
    @ApiOperation(value = "根据id查询")
    public ${cfg.BOName} detail(@PathVariable("id") Long id) {
        return ${table.serviceName?substring(1)?uncap_first}.findDetail(id);
    }

    @PatchMapping
    @ApiOperation(value = "新增或更新", notes = "新增或更新，id is null新增数据，id not null更新数据")
    public ${cfg.BOName} saveOrUpdate(@RequestBody @Valid ${entity} ${entity?uncap_first}) {
        ${table.serviceName?substring(1)?uncap_first}.saveData(${entity?uncap_first});
        return detail(${entity?uncap_first}.getId());
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "删除")
    public void delete(@PathVariable("id") Long id) {
        ${table.serviceName?substring(1)?uncap_first}.delete(id);
    }
}
</#if>
