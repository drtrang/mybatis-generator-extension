package ${package};

import com.lianjia.mls.common.core.annotation.DubboProvider;
import ${projectPackage}.api.rpc.${tableClass.shortClassName}Facade;
import ${projectPackage}.service.${tableClass.shortClassName}Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * ${tableClass.shortClassName}${mapperSuffix}
 *
 * @author mbg
 */
@DubboProvider("${tableClass.variableName}Facade")
public class ${tableClass.shortClassName}${mapperSuffix} implements ${tableClass.shortClassName}Facade {

    @Autowired
    private ${tableClass.shortClassName}Service ${tableClass.variableName}Service;

}