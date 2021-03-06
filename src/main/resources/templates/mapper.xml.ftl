<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${package.Mapper}.${table.mapperName}">

<#if enableCache>
    <!-- 开启二级缓存 -->
    <cache type="org.mybatis.caches.ehcache.LoggingEhcache"/>

</#if>
<#if baseResultMap>
    <!-- 通用查询映射结果 -->
    <resultMap id="BaseResultMap" type="${package.Entity}.${entity}">
<#list table.fields as field>
<#if field.keyFlag><#--生成主键排在第一位-->
        <id column="${field.name}" property="${field.propertyName}" />
</#if>
</#list>
<#list table.commonFields as field><#--生成公共字段 -->
    <result column="${field.name}" property="${field.propertyName}" />
</#list>
<#list table.fields as field>
<#if !field.keyFlag><#--生成普通字段 -->
        <result column="${field.name}" property="${field.propertyName}" />
</#if>
</#list>
    </resultMap>

</#if>
<#if baseColumnList>
    <!-- 通用查询结果列 -->
    <sql id="Base_Column_List">
<#list table.commonFields as field>
        ${field.name},
</#list>
        ${table.fieldNames}
    </sql>

</#if>
    <resultMap id="entity" type="${package.Entity}.${entity}"/>
    <resultMap id="bo" extends="entity" type="${cfg.boPackage}.${cfg.BOName}">
    </resultMap>
    <sql id="condition">
        <if test="vo != null">
        <#if cfg.isGenerateAllDefaultCondition>
        <#list table.fields as field>
            <#if field.propertyType=='String'>
            <if test="vo.${field.propertyName} !=null and vo.${field.propertyName} !=''">
                and base.${field.name} like concat('%',<#noparse>#</#noparse>{vo.${field.propertyName}},'%')
            <#else>
            <if test="vo.${field.propertyName} !=null">
                and base.${field.name} = <#noparse>#</#noparse>{vo.${field.propertyName}}
            </#if>
            </if>
            <#if field.propertyType=='Date' || field.propertyType=='LocalDateTime'>
            <if test="vo.start${field.propertyName?cap_first} !=null">
                and base.${field.name} >= <#noparse>#</#noparse>{vo.start${field.propertyName?cap_first}}
            </if>
            <if test="vo.end${field.propertyName?cap_first} !=null">
                and base.${field.name} &lt;= <#noparse>#</#noparse>{vo.end${field.propertyName?cap_first}}
            </if>
            </#if>
        </#list>
        </#if>
        </if>
    </sql>
    <select id="page" resultMap="bo">
        select base.*
        from ${table.name} as base
        <where>
            <include refid="condition"/>
        </where>
    </select>
    <select id="countByVO" resultType="integer">
        select count(1)
        from ${table.name} as base
        <where>
            <include refid="condition"/>
        </where>
    </select>
    <select id="findDetail" resultMap="bo">
        select base.*
        from ${table.name} as base
        where base.id = ${r"#"}{id}
    </select>
</mapper>
