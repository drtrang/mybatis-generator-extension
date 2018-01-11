<#assign dateTime = .now>
package ${package};

import com.lianjia.mls.common.core.service.BaseService;
import ${tableClass.fullClassName};

/**
 * ${tableClass.shortClassName}${mapperSuffix}
 *
 * @mbg.generated
 * @author mbg
 * @since ${dateTime?date}
 */
public interface ${tableClass.shortClassName}${mapperSuffix} extends BaseService<${tableClass.shortClassName}, ${tableClass.pkFields[0].shortTypeName}> {

}