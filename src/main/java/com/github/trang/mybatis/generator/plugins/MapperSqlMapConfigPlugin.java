package com.github.trang.mybatis.generator.plugins;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.List;
import java.util.Properties;

/**
 * Mapper.xml 生成器
 *   1. 加入 BaseColumns 属性
 *
 * @author trang
 */
public class MapperSqlMapConfigPlugin extends PluginAdapter {

    /** sql 标签的 id 属性 */
    private String id = "BaseColumns";

    @Override
    public void setContext(Context context) {
        super.setContext(context);
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);

        String id = this.properties.getProperty("id");
        if (StringUtility.stringHasValue(id)) {
            this.id = id;
        }

    }

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    /**
     * 生成 Mapper 文件中的 Element
     */
    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        generateSqlBaseColumns(document, introspectedTable);
        return true;
    }

    /**
     * 生成包含全部列的 sql 元素
     */
    private void generateSqlBaseColumns(Document document, IntrospectedTable introspectedTable) {
        // 新建 sql 元素标签
        XmlElement sqlElement = new XmlElement("sql");
        // 新建 sql 元素属性
        Attribute attr = new Attribute("id", id);
        sqlElement.addAttribute(attr);
        // 新建 sql 元素内容，填写注释
        sqlElement.addElement(new TextElement(Constants.WARNING));
        // 获取全部列名称
        StringBuilder columnsBuilder = new StringBuilder();
        List<IntrospectedColumn> columnList = introspectedTable.getAllColumns();
        for (IntrospectedColumn column : columnList) {
            columnsBuilder.append(MyBatis3FormattingUtilities.getSelectListPhrase(column)).append(", ");
        }
        // 删除最后一个逗号
        String columns = columnsBuilder.substring(0, columnsBuilder.length() - 2);
        // 新建 sql 元素内容，填写列名称
        sqlElement.addElement(new TextElement(columns));
        // 将 sql 元素放到根元素下
        XmlElement rootElement = document.getRootElement();
        rootElement.addElement(new TextElement(""));
        rootElement.addElement(sqlElement);
        rootElement.addElement(new TextElement(""));
    }

}