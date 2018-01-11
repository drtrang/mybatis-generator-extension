package com.github.trang.mybatis.generator.plugins.utils;

import org.mybatis.generator.api.dom.java.JavaElement;
import org.mybatis.generator.config.MergeConstants;

import java.text.SimpleDateFormat;
import java.util.Date;

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
        element.addJavaDocLine(" * ");
        StringBuilder sb = new StringBuilder();
        sb.append(" * ");
        sb.append(MergeConstants.NEW_ELEMENT_TAG);
        if (markAsDoNotDelete) {
            sb.append(" do_not_delete_during_merge");
        }
        element.addJavaDocLine(sb.toString());
        element.addJavaDocLine(" * @author mbg");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String s = format.format(new Date());
        element.addJavaDocLine(" * @since " + s);
    }

    public static void addMergeTag(JavaElement element) {
        addMergeTag(element, true);
    }

    public static void addMergeTag(JavaElement element, boolean markAsDoNotDelete) {
        element.addJavaDocLine(" *");
        StringBuilder sb = new StringBuilder();
        sb.append(" * ");
        sb.append(MergeConstants.NEW_ELEMENT_TAG);
        if (markAsDoNotDelete) {
            sb.append(" do_not_delete_during_merge");
        }
        element.addJavaDocLine(sb.toString());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String s = format.format(new Date());
        element.addJavaDocLine(" * @since " + s);
    }

}