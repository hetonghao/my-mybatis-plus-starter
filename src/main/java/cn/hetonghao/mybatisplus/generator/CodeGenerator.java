package cn.hetonghao.mybatisplus.generator;

import cn.hetonghao.mybatisplus.vo.PageVO;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * 业务代码生成器
 *
 * @author HeTongHao
 * @since 2020/4/6 12:00
 */
public class CodeGenerator {
    /**
     * JDBC相关配置
     */
    private static final String DRIVER = "org.postgresql.Driver";
    private static final String URL = "jdbc:postgresql://localhost:5432/device?useUnicode=true&characterEncoding=utf8";
    private static final String USER_NAME = "postgres";
    private static final String PASSWORD = "Thinker@2019";

    /**
     * 生成在哪个包下
     */
    private static final String PARENT_PACKAGE_NAME = "cn.hetonghao";
    /**
     * 代码生成者
     */
    private static final String AUTHOR = "HeTongHao";

    /**
     * 是否初始化生成,设为false则不会覆盖除entity以外的代码
     */
    private static boolean isInitCodeGenerator = true;

    /**
     * 是否生成所有默认查询条件
     */
    private static boolean isGenerateAllDefaultCondition = true;

    /**
     * 启动入口
     *
     * @param args
     */
    public static void main(String[] args) {
        GlobalConfig gc = new GlobalConfig()
                //id类型，数据库自增
                .setIdType(IdType.AUTO)
                //文件覆盖
                .setFileOverride(true)
                .setOutputDir(getCurrentProjectPath() + "/src/main/java")
                .setAuthor(AUTHOR)
                //生成完是否打开文件夹
                .setOpen(false)
                //实体属性 Swagger2 注解
                .setSwagger2(true);
        System.out.println("路径\t\t" + gc.getOutputDir());
        System.out.println("包\t\t" + PARENT_PACKAGE_NAME);
        PackageConfig pc = new PackageConfig()
                .setModuleName(scanner("模块名"))
                .setParent(PARENT_PACKAGE_NAME)
                .setXml("mapper");
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator()
                // 全局配置
                .setGlobalConfig(gc)
                // 包配置
                .setPackageInfo(pc)
                // 数据源配置
                .setDataSource(dataSourceConfig())
                // 策略配置
                .setStrategy(getStrategyConfig(pc))
                // 自定义配置
                .setCfg(getInjectionConfig(gc, pc))
                // 配置模板
                .setTemplate(initTemplateConfig())
                // 使用Freemarker模板引擎生成
                .setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }

    /**
     * 获取当前项目路径
     *
     * @return
     */
    private static String getCurrentProjectPath() {
        String userDir = System.getProperty("user.dir");
        String modelPath = System.getProperty("java.class.path").replaceFirst("/target/.*$", "");
        return userDir.equals(modelPath) ? userDir : modelPath;
    }

    /**
     * 策略配置
     *
     * @param pc
     * @return
     */
    private static StrategyConfig getStrategyConfig(PackageConfig pc) {
        return new StrategyConfig()
                .setNaming(NamingStrategy.underline_to_camel)
                .setColumnNaming(NamingStrategy.underline_to_camel)
                .setEntityLombokModel(true)
                .setRestControllerStyle(true)
                .setInclude(scanner("表名，多个英文逗号分割").split(","))
                .setControllerMappingHyphenStyle(true)
                .setTablePrefix(pc.getModuleName() + "_")
                //逻辑删除字段
                .setLogicDeleteFieldName("deleted")
                //乐观锁字段
                .setVersionFieldName("version")
                //自动填充字段
                .setTableFillList(Lists.newArrayList(
                        new TableFill("create_time", FieldFill.INSERT)
                        , new TableFill("update_time", FieldFill.INSERT_UPDATE)));
    }

    /**
     * 读取控制台内容
     *
     * @param tip
     * @return
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入" + tip + "：");
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }


    /**
     * 自定义生成配置
     *
     * @param gc
     * @param pc
     * @return
     */
    private static InjectionConfig getInjectionConfig(GlobalConfig gc, PackageConfig pc) {
        // 如果模板引擎是 freemarker String templatePath = "/templates/pageVO.java.ftl";
        // 如果模板引擎是 velocity String templatePath = "/templates/mapper.xml.vm";
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        Map<String, Object> map = Maps.newHashMap();
        if (isInitCodeGenerator) {
            //PageVO
            focList.add(new FileOutConfig("/templates/pageVO.java.ftl") {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    for (TableField tableField : tableInfo.getFields()) {
                        if (tableField.isKeyFlag()) {
                            map.put("pkKeyType", tableField.getPropertyType());
                        }
                    }
                    // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                    String voPackage = pc.getParent() + ".vo";
                    String voPath = gc.getOutputDir() + "/" + voPackage.replaceAll("\\.", "/") + "/";
                    map.put("voPackage", voPackage);
                    String className = tableInfo.getEntityName() + "PageVO";
                    map.put("pageVOName", className);
                    map.put("superPageVOClass", PageVO.class);
                    return voPath + className + StringPool.DOT_JAVA;
                }
            });
            //PageVO
            focList.add(new FileOutConfig("/templates/BO.java.ftl") {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    String boPackage = pc.getParent() + ".bo";
                    String boPath = gc.getOutputDir() + "/" + boPackage.replaceAll("\\.", "/") + "/";
                    String className = tableInfo.getEntityName() + "BO";
                    map.put("BOName", className);
                    map.put("boPackage", boPackage);
                    return boPath + className + StringPool.DOT_JAVA;
                }
            });
        }
        // 自定义属性注入
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                map.put("isGenerateAllDefaultCondition", isGenerateAllDefaultCondition);
                this.setMap(map);
            }
        };
        return cfg.setFileOutConfigList(focList);
    }

    /**
     * 数据源配置
     *
     * @return
     */
    private static DataSourceConfig dataSourceConfig() {
        return new DataSourceConfig().setDriverName(DRIVER).setUrl(URL).setUsername(USER_NAME).setPassword(PASSWORD)
                .setTypeConvert((globalConfig, fieldType) -> {
                    String t = fieldType.toLowerCase();
                    if (t.contains("char") || t.contains("text")) {
                        return DbColumnType.STRING;
                    } else if (t.contains("bigint")) {
                        return DbColumnType.LONG;
                    } else if (t.contains("int")) {
                        return DbColumnType.INTEGER;
                    } else if (t.contains("date") || t.contains("time") || t.contains("year")) {
                        return DbColumnType.LOCAL_DATE_TIME;
                    } else if (t.contains("bit") || t.contains("bool")) {
                        return DbColumnType.BOOLEAN;
                    } else if (t.contains("decimal") || t.contains("numeric")) {
                        return DbColumnType.BIG_DECIMAL;
                    } else if (t.contains("clob")) {
                        return DbColumnType.CLOB;
                    } else if (t.contains("blob")) {
                        return DbColumnType.BLOB;
                    } else if (t.contains("binary")) {
                        return DbColumnType.BYTE_ARRAY;
                    } else if (t.contains("float")) {
                        return DbColumnType.FLOAT;
                    } else if (t.contains("double")) {
                        return DbColumnType.DOUBLE;
                    } else if (t.contains("json") || t.contains("enum")) {
                        return DbColumnType.STRING;
                    }
                    return DbColumnType.STRING;
                });
    }

    /**
     * 根据自己的需要，修改哪些包下面的 要覆盖还是不覆盖
     *
     * @return
     */
    private static TemplateConfig initTemplateConfig() {
        //配置自定义输出模板
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        TemplateConfig templateConfig = new TemplateConfig();
        //不是第一次生成，则不生成以下类
        if (!isInitCodeGenerator) {
            templateConfig.setController(null);
            templateConfig.setService(null);
            templateConfig.setServiceImpl(null);
            templateConfig.setMapper(null);
            templateConfig.setXml(null);
        }
        return templateConfig;
    }
}
