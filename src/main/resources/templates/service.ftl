<#assign dateTime = .now>
<#assign pkField = tableClass.pkFields[0]>
package ${package};

import com.lianjia.mls.common.core.base.service.BaseService;
import ${tableClass.fullClassName};

/**
 * ${tableClass.shortClassName}${mapperSuffix}
 *
 * @author mbg
 * @mbg.generated
 * @since ${dateTime?date}
 */
public interface ${tableClass.shortClassName}${mapperSuffix} extends BaseService<${tableClass.shortClassName}, ${pkField.shortTypeName}> {

}