package com.github.trang.mybatis.generator.plugins.other;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.JavaTypeResolver;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.util.StringUtility;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.*;

/**
 * @author trang
 */
public class CustomJavaTypeResolver implements JavaTypeResolver {

    protected List<String> warnings;

    protected Properties properties = new Properties();

    protected Context context;

    protected boolean forceBigDecimals;

    protected Map<Integer, JdbcTypeInformation> typeMap = new HashMap<>();

    public CustomJavaTypeResolver() {
        super();
        typeMap.put(Types.ARRAY, new JdbcTypeInformation("ARRAY", new FullyQualifiedJavaType(Object.class.getName())));
        typeMap.put(Types.BIGINT, new JdbcTypeInformation("BIGINT", new FullyQualifiedJavaType(Long.class.getName())));
        typeMap.put(Types.BINARY, new JdbcTypeInformation("BINARY", new FullyQualifiedJavaType("byte[]")));
        typeMap.put(Types.BIT, new JdbcTypeInformation("BIT", new FullyQualifiedJavaType(Boolean.class.getName())));
        typeMap.put(Types.BLOB, new JdbcTypeInformation("BLOB", new FullyQualifiedJavaType("byte[]")));
        typeMap.put(Types.BOOLEAN, new JdbcTypeInformation("BOOLEAN", new FullyQualifiedJavaType(Boolean.class.getName())));
        typeMap.put(Types.CHAR, new JdbcTypeInformation("CHAR", new FullyQualifiedJavaType(String.class.getName())));
        typeMap.put(Types.CLOB, new JdbcTypeInformation("CLOB", new FullyQualifiedJavaType(String.class.getName())));
        typeMap.put(Types.DATALINK, new JdbcTypeInformation("DATALINK", new FullyQualifiedJavaType(Object.class.getName())));
        typeMap.put(Types.DATE, new JdbcTypeInformation("DATE", new FullyQualifiedJavaType(Date.class.getName())));
        typeMap.put(Types.DECIMAL, new JdbcTypeInformation("DECIMAL", new FullyQualifiedJavaType(BigDecimal.class.getName())));
        typeMap.put(Types.DISTINCT, new JdbcTypeInformation("DISTINCT", new FullyQualifiedJavaType(Object.class.getName())));
        typeMap.put(Types.DOUBLE, new JdbcTypeInformation("DOUBLE", new FullyQualifiedJavaType(Double.class.getName())));
        typeMap.put(Types.FLOAT, new JdbcTypeInformation("FLOAT", new FullyQualifiedJavaType(Double.class.getName())));
        typeMap.put(Types.INTEGER, new JdbcTypeInformation("INTEGER", new FullyQualifiedJavaType(Integer.class.getName())));
        typeMap.put(Types.JAVA_OBJECT, new JdbcTypeInformation("JAVA_OBJECT", new FullyQualifiedJavaType(Object.class.getName())));
        typeMap.put(Types.LONGNVARCHAR, new JdbcTypeInformation("LONGNVARCHAR", new FullyQualifiedJavaType(String.class.getName())));
        typeMap.put(Types.LONGVARBINARY, new JdbcTypeInformation("LONGVARBINARY", new FullyQualifiedJavaType("byte[]")));
        typeMap.put(Types.LONGVARCHAR, new JdbcTypeInformation("LONGVARCHAR", new FullyQualifiedJavaType(String.class.getName())));
        typeMap.put(Types.NCHAR, new JdbcTypeInformation("NCHAR", new FullyQualifiedJavaType(String.class.getName())));
        typeMap.put(Types.NCLOB, new JdbcTypeInformation("NCLOB", new FullyQualifiedJavaType(String.class.getName())));
        typeMap.put(Types.NVARCHAR, new JdbcTypeInformation("NVARCHAR", new FullyQualifiedJavaType(String.class.getName())));
        typeMap.put(Types.NULL, new JdbcTypeInformation("NULL", new FullyQualifiedJavaType(Object.class.getName())));
        typeMap.put(Types.NUMERIC, new JdbcTypeInformation("NUMERIC", new FullyQualifiedJavaType(BigDecimal.class.getName())));
        typeMap.put(Types.OTHER, new JdbcTypeInformation("OTHER", new FullyQualifiedJavaType(Object.class.getName())));
        typeMap.put(Types.REAL, new JdbcTypeInformation("REAL", new FullyQualifiedJavaType(Float.class.getName())));
        typeMap.put(Types.REF, new JdbcTypeInformation("REF", new FullyQualifiedJavaType(Object.class.getName())));
        typeMap.put(Types.SMALLINT, new JdbcTypeInformation("SMALLINT", new FullyQualifiedJavaType(Short.class.getName())));
        typeMap.put(Types.STRUCT, new JdbcTypeInformation("STRUCT", new FullyQualifiedJavaType(Object.class.getName())));
        typeMap.put(Types.TIME, new JdbcTypeInformation("TIME", new FullyQualifiedJavaType(Date.class.getName())));
        typeMap.put(Types.TIMESTAMP, new JdbcTypeInformation("TIMESTAMP", new FullyQualifiedJavaType(Date.class.getName())));
        // 更改默认的 Byte 为 Integer
        typeMap.put(Types.TINYINT, new JdbcTypeInformation("TINYINT", new FullyQualifiedJavaType(Integer.class.getName())));
        typeMap.put(Types.VARBINARY, new JdbcTypeInformation("VARBINARY", new FullyQualifiedJavaType("byte[]")));
        typeMap.put(Types.VARCHAR, new JdbcTypeInformation("VARCHAR", new FullyQualifiedJavaType(String.class.getName())));
    }

    @Override
    public void addConfigurationProperties(Properties properties) {
        this.properties.putAll(properties);
        forceBigDecimals = StringUtility.isTrue(properties.getProperty(PropertyRegistry.TYPE_RESOLVER_FORCE_BIG_DECIMALS));
    }

    @Override
    public FullyQualifiedJavaType calculateJavaType(IntrospectedColumn introspectedColumn) {
        FullyQualifiedJavaType answer = null;
        JdbcTypeInformation jdbcTypeInformation = typeMap.get(introspectedColumn.getJdbcType());

        if (jdbcTypeInformation != null) {
            answer = jdbcTypeInformation.getFullyQualifiedJavaType();
            answer = overrideDefaultType(introspectedColumn, answer);
        }

        return answer;
    }

    protected FullyQualifiedJavaType overrideDefaultType(IntrospectedColumn column, FullyQualifiedJavaType defaultType) {
        FullyQualifiedJavaType answer = defaultType;

        switch (column.getJdbcType()) {
            case Types.BIT:
                answer = calculateBitReplacement(column, defaultType);
                break;
            case Types.DECIMAL:
            case Types.NUMERIC:
                answer = calculateBigDecimalReplacement(column, defaultType);
                break;
        }

        return answer;
    }

    protected FullyQualifiedJavaType calculateBitReplacement(IntrospectedColumn column, FullyQualifiedJavaType defaultType) {
        FullyQualifiedJavaType answer;

        if (column.getLength() > 1) {
            answer = new FullyQualifiedJavaType("byte[]");
        } else {
            answer = defaultType;
        }

        return answer;
    }

    protected FullyQualifiedJavaType calculateBigDecimalReplacement(IntrospectedColumn column, FullyQualifiedJavaType defaultType) {
        FullyQualifiedJavaType answer;

        if (column.getScale() > 0 || column.getLength() > 18 || forceBigDecimals) {
            answer = defaultType;
        } else if (column.getLength() > 9) {
            answer = new FullyQualifiedJavaType(Long.class.getName());
        } else if (column.getLength() > 4) {
            answer = new FullyQualifiedJavaType(Integer.class.getName());
        } else {
            answer = new FullyQualifiedJavaType(Short.class.getName());
        }

        return answer;
    }

    @Override
    public String calculateJdbcTypeName(IntrospectedColumn introspectedColumn) {
        String answer = null;
        JdbcTypeInformation jdbcTypeInformation = typeMap.get(introspectedColumn.getJdbcType());

        if (jdbcTypeInformation != null) {
            answer = jdbcTypeInformation.getJdbcTypeName();
        }

        return answer;
    }

    @Override
    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

    public static class JdbcTypeInformation {
        private String jdbcTypeName;

        private FullyQualifiedJavaType fullyQualifiedJavaType;

        public JdbcTypeInformation(String jdbcTypeName, FullyQualifiedJavaType fullyQualifiedJavaType) {
            this.jdbcTypeName = jdbcTypeName;
            this.fullyQualifiedJavaType = fullyQualifiedJavaType;
        }

        public String getJdbcTypeName() {
            return jdbcTypeName;
        }

        public FullyQualifiedJavaType getFullyQualifiedJavaType() {
            return fullyQualifiedJavaType;
        }
    }

}