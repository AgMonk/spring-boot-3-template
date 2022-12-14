package com.gin.springboot3template.sys.security.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gin.springboot3template.sys.response.Res;
import com.gin.springboot3template.sys.security.vo.MyUserDetailsVo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.security.web.csrf.MissingCsrfTokenException;
import org.springframework.security.web.server.csrf.CsrfException;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.gin.springboot3template.sys.controller.VerifyCodeController.VERIFY_CODE_KEY;


/**
 * 校验成功、失败，会话过期处理
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/5 10:04
 */
@Component
public class MyAuthenticationHandler implements AuthenticationSuccessHandler
        , AuthenticationFailureHandler
        , LogoutSuccessHandler
        , SessionInformationExpiredStrategy
        , AccessDeniedHandler, AuthenticationEntryPoint {

    public static final String APPLICATION_JSON_CHARSET_UTF_8 = "application/json;charset=UTF-8";
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 认证失败处理
     * @param request       that resulted in an <code>AuthenticationException</code>
     * @param response      so that the user agent can begin authentication
     * @param authException that caused the invocation
     * @throws IOException      异常
     * @throws ServletException 异常
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        String detailMessage = e.getClass().getSimpleName() + " " + e.getLocalizedMessage();
        if (e instanceof InsufficientAuthenticationException) {
            detailMessage = "请登陆后再访问";
        }
        response.setContentType(APPLICATION_JSON_CHARSET_UTF_8);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().println(OBJECT_MAPPER.writeValueAsString(Res.of(detailMessage, "认证异常")));
    }

    /**
     * 权限不足时的处理
     * @param request               that resulted in an <code>AccessDeniedException</code>
     * @param response              so that the user agent can be advised of the failure
     * @param accessDeniedException that caused the invocation
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        String detailMessage = null;
        if (accessDeniedException instanceof MissingCsrfTokenException) {
            detailMessage = "缺少CSRF TOKEN,请从表单或HEADER传入";
        } else if (accessDeniedException instanceof InvalidCsrfTokenException) {
            detailMessage = "无效的CSRF TOKEN";
        } else if (accessDeniedException instanceof CsrfException) {
            detailMessage = accessDeniedException.getLocalizedMessage();
        } else if (accessDeniedException instanceof AuthorizationServiceException) {
            detailMessage = AuthorizationServiceException.class.getSimpleName() + " " + accessDeniedException.getLocalizedMessage();
        }
        response.setContentType(APPLICATION_JSON_CHARSET_UTF_8);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.getWriter().println(OBJECT_MAPPER.writeValueAsString(Res.of(detailMessage, "禁止访问")));
    }

    /**
     * 认证失败时的处理
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType(APPLICATION_JSON_CHARSET_UTF_8);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().println(OBJECT_MAPPER.writeValueAsString(Res.of(exception.getLocalizedMessage(), "登陆失败")));
    }

    /**
     * 认证成功时的处理
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType(APPLICATION_JSON_CHARSET_UTF_8);
        response.setStatus(HttpStatus.OK.value());
        response.getWriter().println(OBJECT_MAPPER.writeValueAsString(Res.of(MyUserDetailsVo.of(authentication), "登陆成功")));
        //清理使用过的验证码
        request.getSession().removeAttribute(VERIFY_CODE_KEY);
    }

    /**
     * 会话过期处理
     * @throws IOException      异常
     * @throws ServletException 异常
     */
    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
        String message = "该账号已从其他设备登陆,如果不是您自己的操作请及时修改密码";
        final HttpServletResponse response = event.getResponse();
        response.setContentType(APPLICATION_JSON_CHARSET_UTF_8);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().println(OBJECT_MAPPER.writeValueAsString(Res.of(event.getSessionInformation(), message)));
    }

    /**
     * 登出成功处理
     * @param request        请求
     * @param response       响应
     * @param authentication 认证信息
     * @throws IOException      异常
     * @throws ServletException 异常
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType(APPLICATION_JSON_CHARSET_UTF_8);
        response.setStatus(HttpStatus.OK.value());
        response.getWriter().println(OBJECT_MAPPER.writeValueAsString(Res.of(MyUserDetailsVo.of(authentication), "注销成功")));
    }
}
