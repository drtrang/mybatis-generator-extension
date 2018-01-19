<#assign dateTime = .now>
<#assign pkField = tableClass.pkFields[0]>
package ${package};

import com.lianjia.mls.common.core.base.service.impl.BaseServiceImpl;
import ${projectPackage}.dao.${tableClass.shortClassName}Mapper;
import ${tableClass.fullClassName};
import ${projectPackage}.service.${tableClass.shortClassName}Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ${tableClass.shortClassName}${mapperSuffix}
 *
 * @author mbg
 * @mbg.generated
 * @since ${dateTime?date}
 */
@Service("${tableClass.variableName}Service")
public class ${tableClass.shortClassName}${mapperSuffix} extends BaseServiceImpl<${tableClass.shortClassName}, ${pkField.shortTypeName}> implements ${tableClass.shortClassName}Service {

    @Autowired
    private ${tableClass.shortClassName}Mapper ${tableClass.variableName}Mapper;

}