package com.github.trang.mybatis.generator.plugins.other;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;
import java.util.Set;

public class BaseInterfacePlugin extends PluginAdapter {
    private String baseInterface;
    private int genericsNum;

    public boolean validate(List<String> warnings) {
        this.baseInterface = this.properties.getProperty("baseInterface");
        this.genericsNum = Integer.parseInt(this.properties.getProperty("genericsNum"));
        return true;
    }

    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        Set<FullyQualifiedJavaType> set = interfaze.getSuperInterfaceTypes();
        String domainName = interfaze.getType().getShortName().replaceAll("(.*)Mapper", "$1");

        String superName = null;
        if (this.genericsNum == 0) {
            superName = this.baseInterface;
        } else if (this.genericsNum == 1) {
            superName = this.baseInterface + "<" + domainName + ">";
        } else {
            String type = introspectedTable.getExampleType();
            superName = this.baseInterface + "<" + domainName + "," + type.substring(type.lastIndexOf(".") + 1) + ">";
        }
        set.add(new FullyQualifiedJavaType(superName));

        if (!isSamePackage(interfaze.getType().getFullyQualifiedName(), superName)) {
            interfaze.addImportedType(new FullyQualifiedJavaType(superName));
        }

        interfaze.getMethods().clear();
        return true;
    }

    private boolean isSamePackage(String name1, String name2) {
        String package1 = name1.substring(0, name1.lastIndexOf("."));
        String package2 = name2.substring(0, name2.lastIndexOf("."));

        return package1.equals(package2);
    }
}