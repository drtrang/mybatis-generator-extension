<#assign dateTime = .now>
<#assign pkField = tableClass.pkFields[0]>

package ${package};

import com.lianjia.mls.common.core.annotation.DubboProvider;
import com.lianjia.mls.common.core.base.facade.impl.BaseFacadeImpl;
import ${projectPackage}.model.io.${tableClass.shortClassName}IO;
import ${projectPackage}.model.qo.${tableClass.shortClassName}QO;
import ${projectPackage}.model.ro.${tableClass.shortClassName}RO;
import ${projectPackage}.api.rpc.${tableClass.shortClassName}Facade;
import ${tableClass.fullClassName};
import ${projectPackage}.service.${tableClass.shortClassName}Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * ${tableClass.shortClassName}${mapperSuffix}
 *
 * @author mbg
 * @mbg.generated
 * @since ${dateTime?date}
 */
@DubboProvider("${tableClass.variableName}Facade")
public class ${tableClass.shortClassName}${mapperSuffix} extends BaseFacadeImpl<${tableClass.shortClassName}IO, ${tableClass.shortClassName}QO, ${tableClass.shortClassName}RO, ${tableClass.shortClassName}, ${pkField.shortTypeName}> implements ${tableClass.shortClassName}Facade {

    @Autowired
    private ${tableClass.shortClassName}Service ${tableClass.variableName}Service;

}