package com.github.trang.mybaits.generator.extension.other;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.ShellRunner;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.util.List;

public class GeneratedKeyPlugin extends PluginAdapter {
    private String includes;
    private String excludes;

    public static void generate() {
        String config = GeneratedKeyPlugin.class.getClassLoader().getResource("mybatisConfig.xml").getFile();

        String[] arg = {"-configfile", config, "-overwrite"};
        ShellRunner.main(arg);
    }

    public static void main(String[] args) {
        generate();
    }

    public boolean validate(List<String> warnings) {
        this.includes = this.properties.getProperty("includes");
        this.excludes = this.properties.getProperty("excludes");
        return true;
    }

    public boolean sqlMapInsertSelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        String tableName = introspectedTable.getFullyQualifiedTable().getFullyQualifiedTableNameAtRuntime();
        if ((this.excludes != null) && (tableName.matches(this.excludes))) {
            return super.sqlMapInsertSelectiveElementGenerated(element, introspectedTable);
        }
        if ((this.includes != null) && (tableName.matches(this.includes))) {
            List<IntrospectedColumn> lst = introspectedTable.getPrimaryKeyColumns();
            if (lst.size() == 1) {
                addGeneratedKeyAttr(element, ((IntrospectedColumn) lst.get(0)).getActualColumnName());
            }
            return super.sqlMapInsertSelectiveElementGenerated(element, introspectedTable);
        }
        if ((this.includes == null) && (this.excludes == null)) {
            List<IntrospectedColumn> lst = introspectedTable.getPrimaryKeyColumns();
            if (lst.size() == 1) {
                addGeneratedKeyAttr(element, ((IntrospectedColumn) lst.get(0)).getActualColumnName());
            }
            return super.sqlMapInsertSelectiveElementGenerated(element, introspectedTable);
        }
        return super.sqlMapInsertSelectiveElementGenerated(element, introspectedTable);
    }

    public boolean sqlMapInsertElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        String tableName = introspectedTable.getFullyQualifiedTable().getFullyQualifiedTableNameAtRuntime();
        if ((this.excludes != null) && (tableName.matches(this.excludes))) {
            return super.sqlMapInsertElementGenerated(element, introspectedTable);
        }
        if ((this.includes != null) && (tableName.matches(this.includes))) {
            List<IntrospectedColumn> lst = introspectedTable.getPrimaryKeyColumns();
            if (lst.size() == 1) {
                addGeneratedKeyAttr(element, ((IntrospectedColumn) lst.get(0)).getActualColumnName());
            }
            return super.sqlMapInsertElementGenerated(element, introspectedTable);
        }
        if ((this.includes == null) && (this.excludes == null)) {
            List<IntrospectedColumn> lst = introspectedTable.getPrimaryKeyColumns();
            if (lst.size() == 1) {
                addGeneratedKeyAttr(element, ((IntrospectedColumn) lst.get(0)).getActualColumnName());
            }
            return super.sqlMapInsertElementGenerated(element, introspectedTable);
        }
        return super.sqlMapInsertElementGenerated(element, introspectedTable);
    }

    private void addGeneratedKeyAttr(XmlElement element, String pk) {
        element.addAttribute(new Attribute("useGeneratedKeys", "true"));
        element.addAttribute(new Attribute("keyProperty", pk));
    }
}