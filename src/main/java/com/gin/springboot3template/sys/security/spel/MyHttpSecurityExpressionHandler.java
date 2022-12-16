package com.gin.springboot3template.sys.security.spel;

import org.springframework.security.access.expression.SecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.expression.DefaultHttpSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebSecurityExpressionRoot;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

import static com.gin.springboot3template.sys.bo.Constant.ROLE_ADMIN;

/**
 * 增强表达式上下文
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/16 14:40
 */
@Component
public class MyHttpSecurityExpressionHandler extends DefaultHttpSecurityExpressionHandler {
    private final AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();

    private final String defaultRolePrefix = ROLE_ADMIN;

    @Override
    protected SecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, RequestAuthorizationContext context) {
        return createSecurityExpressionRoot(() -> authentication, context);
    }

    private WebSecurityExpressionRoot createSecurityExpressionRoot(Supplier<Authentication> authentication,
                                                                   RequestAuthorizationContext context) {
        MyWebSecurityExpressionRoot root = new MyWebSecurityExpressionRoot(authentication, context.getRequest());
        root.setRoleHierarchy(getRoleHierarchy());
        root.setPermissionEvaluator(getPermissionEvaluator());
        root.setTrustResolver(this.trustResolver);
        root.setDefaultRolePrefix(this.defaultRolePrefix);
        return root;
    }
}
