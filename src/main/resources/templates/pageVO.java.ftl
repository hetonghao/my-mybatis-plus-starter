package ${cfg.voPackage};

<#if swagger2>
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
</#if>
<#if entityLombokModel>
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
</#if>
<#if cfg.superPageVOClass??>
import ${cfg.superPageVOClass.canonicalName};
</#if>
<#list table.importPackages as pkg>
import ${pkg};
</#list>

/**
 * <p>
 * ${table.comment!} 分页对象
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if entityLombokModel>
@Data
    <#if superEntityClass??>
@EqualsAndHashCode(callSuper = true)
    <#else>
@EqualsAndHashCode(callSuper = false)
    </#if>
@Accessors(chain = true)
</#if>
<#if swagger2>
@ApiModel(value = "${entity}PageVO对象", description = "${table.comment!}")
</#if>
<#if cfg.superPageVOClass??>
public class ${cfg.pageVOName} extends ${cfg.superPageVOClass.simpleName} {
<#else>
public class ${cfg.pageVOName} {
</#if>
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
    <#if field.keyFlag>
        <#assign keyPropertyName="${field.propertyName}"/>
    </#if>

    <#if field.comment!?length gt 0>
        <#if swagger2>
    @ApiModelProperty(value = "${field.comment}")
        <#else>
    /**
    * ${field.comment}
    */
        </#if>
    </#if>
    private ${field.propertyType} ${field.propertyName};
    <#if field.propertyType=='Date' || field.propertyType=='LocalDateTime'>

    @ApiModelProperty(value = "筛选起始:${field.comment!''}")
    private ${field.propertyType} start${field.propertyName?cap_first};

    @ApiModelProperty(value = "筛选结束:${field.comment!''}")
    private ${field.propertyType} end${field.propertyName?cap_first};
    </#if>
</#list>
<#------------  END 字段循环遍历  ---------->
}