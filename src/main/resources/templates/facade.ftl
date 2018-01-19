<#assign dateTime = .now>
<#assign pkField = tableClass.pkFields[0]>
package ${package};

import com.lianjia.mls.common.base.BaseFacade;
import ${projectPackage}.model.io.${tableClass.shortClassName}IO;
import ${projectPackage}.model.qo.${tableClass.shortClassName}QO;
import ${projectPackage}.model.ro.${tableClass.shortClassName}RO;
import ${tableClass.fullClassName};

/**
 * ${tableClass.shortClassName}${mapperSuffix}
 *
 * @author mbg
 * @mbg.generated
 * @since ${dateTime?date}
 */
public interface ${tableClass.shortClassName}${mapperSuffix} extends BaseFacade<${tableClass.shortClassName}IO, ${tableClass.shortClassName}QO, ${tableClass.shortClassName}RO, ${tableClass.shortClassName}, ${pkField.shortTypeName}> {

}