package com.gin.springboot3template.sys.security.handler;

import com.gin.springboot3template.sys.utils.SpringContextUtils;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.stereotype.Component;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/16 17:01
 */
@Component
public class MyMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {

    @Override
    protected PermissionEvaluator getPermissionEvaluator() {
        return SpringContextUtils.getContext().getBean(PermissionEvaluator.class);
    }
}
