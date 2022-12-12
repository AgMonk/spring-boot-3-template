package com.gin.springboot3template.sys.security.component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.Map;

import static com.gin.springboot3template.sys.controller.VerifyCodeController.VERIFY_CODE_KEY;
import static com.gin.springboot3template.sys.security.component.MyAuthenticationHandler.APPLICATION_JSON_CHARSET_UTF_8;

/**
 * 登陆过滤
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/6 15:40
 */
@Component
public class MyLoginFilter extends UsernamePasswordAuthenticationFilter {
    public static final String REMEMBER_ME_KEY = "rememberMe";

    private final ObjectMapper objectMapper = new ObjectMapper();

    public MyLoginFilter(AuthenticationConfiguration authenticationConfiguration, MyAuthenticationHandler authenticationHandler) throws Exception {
        super(authenticationConfiguration.getAuthenticationManager());
        setAuthenticationFailureHandler(authenticationHandler);
        setAuthenticationSuccessHandler(authenticationHandler);
    }

    private static boolean isContentTypeJson(HttpServletRequest request) {
        final String contentType = request.getContentType();
        return APPLICATION_JSON_CHARSET_UTF_8.equalsIgnoreCase(contentType) || MimeTypeUtils.APPLICATION_JSON_VALUE.equalsIgnoreCase(contentType);
    }

    /**
     * @param request  from which to extract parameters and perform the authentication
     * @param response the response, which may be needed if the implementation has to do a
     *                 redirect as part of a multi-stage authentication process (such as OpenID).
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        if (!HttpMethod.POST.name().equalsIgnoreCase(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        String username = null;
        String password = null;
        String verifyCode = null;
        String rememberMe = null;
        if (isContentTypeJson(request)) {
            try {
                Map<String, String> map = objectMapper.readValue(request.getInputStream(), new TypeReference<>() {
                });
                username = map.get(getUsernameParameter());
                password = map.get(getPasswordParameter());
                verifyCode = map.get(VERIFY_CODE_KEY);
                rememberMe = map.get(REMEMBER_ME_KEY);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            username = obtainUsername(request);
            password = obtainPassword(request);
            verifyCode = request.getParameter(VERIFY_CODE_KEY);
            rememberMe = request.getParameter(REMEMBER_ME_KEY);
        }
        //校验验证码
        final String vc = (String) request.getSession().getAttribute(VERIFY_CODE_KEY);
        if (vc == null) {
            throw new BadCredentialsException("验证码不存在,请先获取验证码");
        } else if (verifyCode == null || "".equals(verifyCode)) {
            throw new BadCredentialsException("请输入验证码");
        } else if (!vc.equalsIgnoreCase(verifyCode)) {
            throw new BadCredentialsException("验证码错误");
        }

        //将 rememberMe 状态存入 attr中
        if (!ObjectUtils.isEmpty(rememberMe)) {
            request.setAttribute(REMEMBER_ME_KEY, rememberMe);
        }

        username = (username != null) ? username.trim() : "";
        password = (password != null) ? password : "";
        UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    @PostConstruct
    public void init() {
        setFilterProcessesUrl("/sys/user/login");
    }

}
