<#assign dateTime = .now>
<#assign pkField = tableClass.pkFields[0]>
package ${package};

import com.google.common.base.MoreObjects;
import com.lianjia.mls.common.base.BaseModel;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * ${tableClass.tableName} 表数据模型
 *
 * @author mbg
 * @mbg.generated
 * @since ${dateTime?date}
 */
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Setter
@Table(name = "${tableClass.tableName}")
public class ${tableClass.shortClassName} extends BaseModel<${pkField.shortTypeName}> implements Serializable {

    private static final long serialVersionUID = 1L;

<#if tableClass.pkFields??>
    <#list tableClass.pkFields as field>
    /**
    <#list field.remarks?split("\r\n") as remarks>
     * ${remarks}
    </#list>
     *
     * @mbg.generated
     * @since ${dateTime?date}
     */
    @Id
    @Column(name = "${field.columnName}")
    @GeneratedValue(generator = "JDBC")
    private ${field.shortTypeName} ${field.fieldName};

    </#list>
</#if>

<#if tableClass.baseFields??>
    <#list tableClass.baseFields as field>
    /**
    <#list field.remarks?split("\r\n") as remarks>
     * ${remarks}
    </#list>
     *
     * @mbg.generated
     * @since ${dateTime?date}
     */
    @Column(name = "${field.columnName}")
    private ${field.shortTypeName} ${field.fieldName};

    </#list>
</#if>

<#if tableClass.blobFields??>
    <#list tableClass.blobFields as field>
    /**
    <#list field.remarks?split("\r\n") as remarks>
     * ${remarks}
    </#list>
     *
     * @mbg.generated
     * @since ${dateTime?date}
     */
    @Column(name = "${field.columnName}")
    private ${field.shortTypeName} ${field.fieldName};

    </#list>
</#if>
    @Override
    @Transient
    public ${pkField.shortTypeName} getPk() {
        return ${pkField.fieldName};
    }

<#if tableClass.allFields??>
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
    <#list tableClass.allFields as field>
                .add("${field.fieldName}", ${field.fieldName})
    </#list>
                .add("super", super.toString())
                .toString();
    }
</#if>

}