package com.gin.springboot3template.route.strategy;

import com.gin.springboot3template.route.annotation.MenuItem;
import com.gin.springboot3template.route.entity.EleMenuItem;
import com.gin.springboot3template.sys.bo.Constant;
import com.gin.springboot3template.user.security.bo.MyUserDetails;
import com.gin.springboot3template.user.security.utils.MySecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 路由导航展示策略的SpringSecurity实现, 根据用户持有的权限来判断路由是否展示
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/3/27 14:53
 */
@Component
@Slf4j
public class SpringSecurityStrategy implements VisibleStrategy {
    /**
     * 匿名用户
     */
    public static final String ANONYMOUS_USER = "anonymousUser";

    /**
     * 判断{@link  EleMenuItem} 的disabled字段
     * @param menuItem   Controller上的注解
     * @param method     请求方法
     * @param requestUrl 请求地址
     * @return 是否disabled
     */
    @Override
    public boolean isDisable(MenuItem menuItem, Method method, String requestUrl) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated() || ANONYMOUS_USER.equals(authentication.getPrincipal())) {
            return true;
        }
        final MyUserDetails myUserDetails = MySecurityUtils.currentUserDetails();
        if (myUserDetails.hasRole(Constant.Role.ADMIN)) {
            // 超管权限 全部展示
            return false;
        } else {
            return !myUserDetails.hasAuthority(requestUrl);
        }
    }
}
