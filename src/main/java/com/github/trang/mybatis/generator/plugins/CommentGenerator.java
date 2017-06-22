/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 abel533@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.github.trang.mybatis.generator.plugins;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.MergeConstants;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.Properties;

/**
 * 给 xml 文件和 domain 文件生成注释
 *   1. xml 文件中的 resultMap 注释改为一行
 *   2. 优化 domain 文件中的字段注释，如果数据库注释为多行则字段注释也为多行
 *   3. 去掉 getter、setter 方法的注释
 *
 * @author trang
 */
public class CommentGenerator implements org.mybatis.generator.api.CommentGenerator {

    private String beginningDelimiter = "";
    private String endingDelimiter = "";

    public CommentGenerator() {
        super();
    }

    /**
     * xml 文件的注释
     */
    public void addComment(XmlElement xmlElement) {
        xmlElement.addElement(new TextElement("<!-- WARNING - @mbg.generated -->"));
    }

    public void addConfigurationProperties(Properties properties) {
        String beginningDelimiter = properties.getProperty("beginningDelimiter");
        if (StringUtility.stringHasValue(beginningDelimiter)) {
            this.beginningDelimiter = beginningDelimiter;
        }
        String endingDelimiter = properties.getProperty("endingDelimiter");
        if (StringUtility.stringHasValue(endingDelimiter)) {
            this.endingDelimiter = endingDelimiter;
        }
    }

    public String getDelimiterName(String name) {
        return beginningDelimiter + name + endingDelimiter;
    }

    /**
     * 删除标记
     */
    protected void addJavadocTag(JavaElement javaElement, boolean markAsDoNotDelete) {
        StringBuilder sb = new StringBuilder();
        sb.append(" * ");
        sb.append(MergeConstants.NEW_ELEMENT_TAG);
        if (markAsDoNotDelete) {
            sb.append(" do_not_delete_during_merge");
        }
        javaElement.addJavaDocLine(sb.toString());
    }

    /**
     * 给 domain 文件中的字段添加数据库备注
     */
    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn
            introspectedColumn) {
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
    public void addGetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn
            introspectedColumn) {}

    /**
     * setter 方法注释
     */
    public void addSetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn
            introspectedColumn) {}

    /**
     * Example 使用
     */
    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable, boolean
            markAsDoNotDelete) {}
    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable) {}
    public void addFieldComment(Field field, IntrospectedTable introspectedTable) {}
    public void addEnumComment(InnerEnum innerEnum, IntrospectedTable introspectedTable) {}

    public void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {}

    public void addGeneralMethodComment(Method method, IntrospectedTable introspectedTable) {}

    public void addRootComment(XmlElement rootElement) {}

    public void addJavaFileComment(CompilationUnit compilationUnit) {}
}
