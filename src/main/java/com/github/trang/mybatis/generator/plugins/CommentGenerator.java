package com.github.trang.mybatis.generator.plugins;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.Properties;

import static com.github.trang.mybatis.generator.plugins.Constants.WARNING;

/**
 * 给 xml 文件和 domain 文件生成注释
 *   1. xml 文件中的 resultMap 注释改为一行
 *   2. 优化 domain 文件中的字段注释，如果数据库注释为多行则字段注释也为多行
 *   3. 去掉 getter、setter 方法的注释
 *
 * @author trang
 */
public class CommentGenerator implements org.mybatis.generator.api.CommentGenerator {

    public CommentGenerator() {
        super();
    }

    /**
     * xml 文件的注释
     */
    @Override
    public void addComment(XmlElement xmlElement) {
        xmlElement.addElement(new TextElement(WARNING));
    }

    @Override
    public void addConfigurationProperties(Properties properties) {}

    /**
     * 给 domain 文件中的字段添加数据库备注
     */
    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        if (StringUtility.stringHasValue(introspectedColumn.getRemarks())) {
            field.addJavaDocLine("/**");
            String remark = introspectedColumn.getRemarks();
            String[] remarks = remark.split("\r\n");
            for (String s : remarks) {
                field.addJavaDocLine(" * " + s);
            }
            field.addJavaDocLine(" */");
        }
    }

    /**
     * getter 方法注释
     */
    @Override
    public void addGetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {}

    /**
     * setter 方法注释
     */
    @Override
    public void addSetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {}

    @Override
    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable, boolean markAsDoNotDelete) {}

    @Override
    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable) {}

    @Override
    public void addEnumComment(InnerEnum innerEnum, IntrospectedTable introspectedTable) {}

    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable) {}

    @Override
    public void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {}

    @Override
    public void addGeneralMethodComment(Method method, IntrospectedTable introspectedTable) {}

    @Override
    public void addRootComment(XmlElement rootElement) {}

    @Override
    public void addJavaFileComment(CompilationUnit compilationUnit) {}

}