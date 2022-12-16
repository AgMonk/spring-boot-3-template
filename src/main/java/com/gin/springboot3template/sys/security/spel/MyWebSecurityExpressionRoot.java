package com.gin.springboot3template.sys.security.spel;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.WebSecurityExpressionRoot;

import java.util.function.Supplier;

import static com.gin.springboot3template.sys.bo.Constant.ROLE_ADMIN;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/16 14:45
 */
public class MyWebSecurityExpressionRoot extends WebSecurityExpressionRoot {

    public MyWebSecurityExpressionRoot(Authentication a, FilterInvocation fi) {
        super(a, fi);
    }

    public MyWebSecurityExpressionRoot(Supplier<Authentication> authentication, HttpServletRequest request) {
        super(authentication, request);
    }

    public boolean hasPermOrAdmin(String... authorities) {
        return hasRole(ROLE_ADMIN) || hasAnyAuthority(authorities);
    }
}
