<#assign dateTime = .now>
package ${package};

import com.google.common.base.MoreObjects;
import com.lianjia.mls.common.base.BaseQO;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * ${tableClass.shortClassName} 查询对象
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
public class ${tableClass.shortClassName}QO extends BaseQO implements Serializable {

    private static final long serialVersionUID = 1L;

<#if tableClass.allFields??>
    <#list tableClass.allFields as field>
    /**
    <#list field.remarks?split("\r\n") as remarks>
     * ${remarks}
    </#list>
     *
     * @mbg.generated
     * @since ${dateTime?date}
     */
    private ${field.shortTypeName} ${field.fieldName};

    </#list>
</#if>

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