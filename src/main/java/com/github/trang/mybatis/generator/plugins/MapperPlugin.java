package com.github.trang.mybatis.generator.plugins;

import com.github.trang.mybatis.generator.plugins.utils.ElementHelper;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.config.CommentGeneratorConfiguration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.internal.util.StringUtility;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * 通用 Mapper 生成器
 * 1. Mapper 接口增加 @Mapper 注解
 * 2. domain 类增加 Lombok 特性
 * 3. 通用 Mapper 相关内容全部已到此插件
 *
 * @author trang
 */
public class MapperPlugin extends FalseMethodPlugin {

    /** 开始的分隔符，例如 mysql 为 `，sql server 为 [ */
    private String beginningDelimiter = "";
    /** 结束的分隔符，例如 mysql 为 `，sql server 为 ] */
    private String endingDelimiter = "";
     /** 通用 Mapper 接口 */
    private Set<String> mappers = new HashSet<>();
    /** caseSensitive 默认 false，当数据库表名区分大小写时，可以将该属性设置为 true */
    private boolean caseSensitive = false;
    /** 强制生成注解，默认 false，设置为 true 后一定会生成 @Table 和 @Column 注解 */
    private boolean forceAnnotation = false;
    /** 数据库模式 */
    private String schema;
    /** Lombok 插件模式 */
    private LombokType lombok = LombokType.none;
    /** 注释生成器 */
    private CommentGeneratorConfiguration configuration;

    enum LombokType {
        none, simple, builder, accessors
    }

    @Override
    public void setContext(Context context) {
        super.setContext(context);
        // 设置默认的注释生成器
        configuration = new CommentGeneratorConfiguration();
        configuration.setConfigurationType(CommentGenerator.class.getCanonicalName());
        context.setCommentGeneratorConfiguration(configuration);
        // 支持 oracle 获取注释 #114
        context.getJdbcConnectionConfiguration().addProperty("remarksReporting", "true");
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);

        String beginningDelimiter = this.properties.getProperty("beginningDelimiter");
        if (StringUtility.stringHasValue(beginningDelimiter)) {
            this.beginningDelimiter = beginningDelimiter;
        }
        configuration.addProperty("beginningDelimiter", this.beginningDelimiter);

        String endingDelimiter = this.properties.getProperty("endingDelimiter");
        if (StringUtility.stringHasValue(endingDelimiter)) {
            this.endingDelimiter = endingDelimiter;
        }
        configuration.addProperty("endingDelimiter", this.endingDelimiter);

        String mappers = this.properties.getProperty("mappers");
        if (StringUtility.stringHasValue(mappers)) {
            Collections.addAll(this.mappers, mappers.split(","));
        } else {
            throw new RuntimeException("Mapper 插件缺少必要的 mappers 属性!");
        }

        String schema = this.properties.getProperty("schema");
        if (StringUtility.stringHasValue(schema)) {
            this.schema = schema;
        }

        this.forceAnnotation = StringUtility.isTrue(this.properties.getProperty("forceAnnotation"));
        this.caseSensitive = StringUtility.isTrue(this.properties.getProperty("caseSensitive"));

        String lombok = this.properties.getProperty("lombok");
        if (StringUtility.stringHasValue(lombok)) {
            this.lombok = LombokType.valueOf(lombok);
        }
    }

    public String getDelimiterName(String name) {
        StringBuilder nameBuilder = new StringBuilder();
        if (StringUtility.stringHasValue(schema)) {
            nameBuilder.append(schema).append(".");
        }
        nameBuilder.append(beginningDelimiter);
        nameBuilder.append(name);
        nameBuilder.append(endingDelimiter);
        return nameBuilder.toString();
    }

    /**
     * 生成的 Mapper 接口
     */
    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        // 获取实体类
        FullyQualifiedJavaType entityType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        // import 实体类
        interfaze.addImportedType(entityType);
        // import 接口
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Mapper"));
        // 添加 @Mapper 注解
        interfaze.addAnnotation("@Mapper");
        for (String mapper : mappers) {
            // import mappers
            interfaze.addImportedType(new FullyQualifiedJavaType(mapper));
            // 添加父类
            interfaze.addSuperInterface(new FullyQualifiedJavaType(mapper + "<" + entityType.getShortName() + ">"));
        }
        ElementHelper.addAuthorTag(interfaze, false);
        return true;
    }

    /**
     * 处理实体类的包和 @Table 注解
     */
    private void processEntityClass(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        // import Lombok
        switch (lombok) {
            case none:
                break;
            case simple:
                topLevelClass.addImportedType("lombok.*");
                break;
            case builder:
                topLevelClass.addImportedType("lombok.*");
                break;
            case accessors:
                topLevelClass.addImportedType("lombok.*");
                topLevelClass.addImportedType("lombok.experimental.Accessors");
                break;
            default:
                break;
        }
        // import JPA
        topLevelClass.addImportedType("javax.persistence.*");

        // 添加 Lombok 注解
        switch (lombok) {
            case none:
                break;
            case builder:
                topLevelClass.addAnnotation("@NoArgsConstructor");
                topLevelClass.addAnnotation("@AllArgsConstructor(access = AccessLevel.PRIVATE)");
                topLevelClass.addAnnotation("@Builder");
                topLevelClass.addAnnotation("@Getter");
                topLevelClass.addAnnotation("@Setter");
                break;
            case accessors:
                topLevelClass.addAnnotation("@NoArgsConstructor");
                topLevelClass.addAnnotation("@Accessors(fluent = true)");
                topLevelClass.addAnnotation("@Getter");
                topLevelClass.addAnnotation("@Setter");
                break;
            case simple:
                topLevelClass.addAnnotation("@NoArgsConstructor");
                topLevelClass.addAnnotation("@Getter");
                topLevelClass.addAnnotation("@Setter");
                break;
            default:
                break;
        }

        String tableName = introspectedTable.getFullyQualifiedTableNameAtRuntime();
        // 如果包含空格，或者需要分隔符，需要完善
        if (StringUtility.stringContainsSpace(tableName)) {
            tableName = context.getBeginningDelimiter() + tableName + context.getEndingDelimiter();
        }
        // 是否忽略大小写，对于区分大小写的数据库，会有用
        if (caseSensitive && !topLevelClass.getType().getShortName().equals(tableName)) {
            topLevelClass.addAnnotation("@Table(name = \"" + getDelimiterName(tableName) + "\")");
        } else if (!topLevelClass.getType().getShortName().equalsIgnoreCase(tableName)) {
            topLevelClass.addAnnotation("@Table(name = \"" + getDelimiterName(tableName) + "\")");
        } else if (StringUtility.stringHasValue(schema)
                || StringUtility.stringHasValue(beginningDelimiter)
                || StringUtility.stringHasValue(endingDelimiter)) {
            topLevelClass.addAnnotation("@Table(name = \"" + getDelimiterName(tableName) + "\")");
        } else if (forceAnnotation) {
            topLevelClass.addAnnotation("@Table(name = \"" + getDelimiterName(tableName) + "\")");
        }
    }

    /**
     * 生成基础实体类
     */
    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        processEntityClass(topLevelClass, introspectedTable);
        return true;
    }

    /**
     * 生成实体类注解 KEY 对象
     */
    @Override
    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        processEntityClass(topLevelClass, introspectedTable);
        return true;
    }

    /**
     * 生成带 BLOB 字段的对象
     */
    @Override
    public boolean modelRecordWithBLOBsClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        processEntityClass(topLevelClass, introspectedTable);
        return true;
    }

    /**
     * 处理实体类的字段
     */
    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        // 添加注解
        if (field.isTransient()) {
            field.addAnnotation("@Transient");
        }
        for (IntrospectedColumn column : introspectedTable.getPrimaryKeyColumns()) {
            if (introspectedColumn == column) {
                field.addAnnotation("@Id");
                break;
            }
        }
        String column = introspectedColumn.getActualColumnName();
        if (StringUtility.stringContainsSpace(column) || introspectedTable.getTableConfiguration().isAllColumnDelimitingEnabled()) {
            column = introspectedColumn.getContext().getBeginningDelimiter()
                    + column
                    + introspectedColumn.getContext().getEndingDelimiter();
        }
        // @Column
        if (!column.equals(introspectedColumn.getJavaProperty())) {
            field.addAnnotation("@Column(name = \"" + getDelimiterName(column) + "\")");
        } else if (StringUtility.stringHasValue(beginningDelimiter) || StringUtility.stringHasValue(endingDelimiter)) {
            field.addAnnotation("@Column(name = \"" + getDelimiterName(column) + "\")");
        } else if (forceAnnotation){
            field.addAnnotation("@Column(name = \"" + getDelimiterName(column) + "\")");
        }
        if (introspectedColumn.isIdentity()) {
            if (introspectedTable.getTableConfiguration().getGeneratedKey().getRuntimeSqlStatement().equals("JDBC")) {
                field.addAnnotation("@GeneratedValue(generator = \"JDBC\")");
            } else {
                field.addAnnotation("@GeneratedValue(strategy = GenerationType.IDENTITY)");
            }
        } else if (introspectedColumn.isSequenceColumn()) {
            //在 Oracle 中，如果需要是 SEQ_TABLENAME，那么可以配置为 select SEQ_{1} from dual
            String tableName = introspectedTable.getFullyQualifiedTableNameAtRuntime();
            String sql = MessageFormat.format(introspectedTable.getTableConfiguration().getGeneratedKey().getRuntimeSqlStatement(), tableName, tableName.toUpperCase());
            field.addAnnotation("@GeneratedValue(strategy = GenerationType.IDENTITY, generator = \"" + sql + "\")");
        }
        return true;
    }

    @Override
    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return lombok == LombokType.none;
    }

    @Override
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return lombok == LombokType.none;
    }

}