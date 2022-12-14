package com.gin.springboot3template.sys.security.component;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;


/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/7 17:34
 */
@Component
public class MyRememberMeServices extends PersistentTokenBasedRememberMeServices {
    public static final String REMEMBER_ME_KEY = "rememberMe";
    public static final List<String> TRUE_VALUES = List.of("true", "yes", "on", "1");

    public MyRememberMeServices(UserDetailsService userDetailsService, PersistentTokenRepository tokenRepository) {
        super(UUID.randomUUID().toString(), userDetailsService, tokenRepository);
    }

    @Override
    protected boolean rememberMeRequested(HttpServletRequest request, String parameter) {
        final String rememberMe = (String) request.getAttribute(REMEMBER_ME_KEY);
        if (rememberMe != null) {
            for (String trueValue : TRUE_VALUES) {
                if (trueValue.equalsIgnoreCase(rememberMe)) {
                    return true;
                }
            }
        }
        return super.rememberMeRequested(request, parameter);
    }
}