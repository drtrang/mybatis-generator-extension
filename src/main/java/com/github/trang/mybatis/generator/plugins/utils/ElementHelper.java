package com.github.trang.mybatis.generator.plugins.utils;

import org.mybatis.generator.api.dom.java.JavaElement;
import org.mybatis.generator.config.MergeConstants;

/**
 * @author trang
 */
public class ElementHelper {

    /**
     * 为 Java 类生成 JavaDoc
     *
     * @param element element
     */
    public static void addAuthorTag(JavaElement element) {
        addAuthorTag(element, true);
    }

    public static void addAuthorTag(JavaElement element, boolean markAsDoNotDelete) {
        element.addJavaDocLine("/**");
        element.addJavaDocLine(" * ");
        element.addJavaDocLine(" * ");
        if (markAsDoNotDelete) {
            element.addJavaDocLine(" * " + MergeConstants.NEW_ELEMENT_TAG);
        }
        element.addJavaDocLine(" * @author mbg");
        element.addJavaDocLine(" */");
    }

    public static void addMergeTag(JavaElement element) {
        element.addJavaDocLine("/**");
        element.addJavaDocLine(" * " + MergeConstants.NEW_ELEMENT_TAG);
        element.addJavaDocLine(" */");
    }

}