package com.github.trang.mbg.extension;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.DefaultJavaFormatter;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * @author trang
 */
public class AdditionalServicePlugin extends PluginAdapter {

    private String baseService;
    private String targetProject;

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        String baseService = this.properties.getProperty("baseService");
        if (StringUtility.stringHasValue(baseService)) {
            this.baseService = baseService;
        } else {
            throw new RuntimeException("baseService 不能为空！");
        }
        String targetProject = this.properties.getProperty("targetProject");
        if (StringUtility.stringHasValue(targetProject)) {
            this.targetProject = targetProject;
        } else {
            throw new RuntimeException("targetProject 不能为空！");
        }
    }

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(
            IntrospectedTable introspectedTable) {
        CompilationUnit interfacz = new Interface(baseService);
        GeneratedJavaFile file = new GeneratedJavaFile(interfacz, targetProject, new DefaultJavaFormatter());
        return Collections.singletonList(file);
    }
}
