<#assign dateTime = .now>
package ${package};

import com.lianjia.mls.common.core.service.impl.BaseServiceImpl;
import ${projectPackage}.dao.${tableClass.shortClassName}Mapper;
import ${tableClass.fullClassName};
import ${projectPackage}.service.${tableClass.shortClassName}Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ${tableClass.shortClassName}${mapperSuffix}
 *
 * @mbg.generated
 * @author mbg
 * @since ${dateTime?date}
 */
@Service("${tableClass.variableName}Service")
public class ${tableClass.shortClassName}${mapperSuffix} extends BaseServiceImpl<${tableClass.shortClassName}, ${tableClass.pkFields[0].shortTypeName}> implements ${tableClass.shortClassName}Service {

    @Autowired
    private ${tableClass.shortClassName}Mapper ${tableClass.variableName}Mapper;

}