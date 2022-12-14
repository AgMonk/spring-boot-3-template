package com.gin.springboot3template.sys.security.service;

import com.gin.springboot3template.sys.exception.AuthorityEvaluatorDuplicatedException;
import com.gin.springboot3template.sys.security.bo.MyUserDetails;
import com.gin.springboot3template.sys.security.interfaze.AuthorityEvaluator;
import com.gin.springboot3template.sys.utils.SpringContextUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * 权限评估代理
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/14 13:31
 */
@Service
public class PermissionEvaluatorProxyService implements PermissionEvaluator {
    public static final String ROLE_ADMIN = "admin";
    /**
     * 通过class来选择 AuthorityEvaluator 的Map
     */
    HashMap<Class<?>, AuthorityEvaluator> classMap;
    /**
     * 通过name来选择 AuthorityEvaluator 的Map
     */
    HashMap<String, AuthorityEvaluator> nameMap;

    /**
     * 从容器中获取 AuthorityEvaluator Bean
     * @return AuthorityEvaluator Bean
     */
    @NotNull
    private static Collection<AuthorityEvaluator> getAuthorityEvaluators() {
        return SpringContextUtils.getContext().getBeansOfType(AuthorityEvaluator.class).values();
    }

    /**
     * 判断指定用户对指定资源持有指定权限
     * @param authentication     用户认证信息
     * @param targetDomainObject 判断的资源
     * @param permission         权限
     * @return 是否持有权限
     */
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (targetDomainObject == null) {
            return false;
        }
        final MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        //如果持有 admin 角色 ，直接放行
        if (myUserDetails.hasRole(ROLE_ADMIN)) {
            return true;
        }
        initClassMap();
        //从 classMap 中选择  权限评估器
        final AuthorityEvaluator authorityEvaluator = this.classMap.getOrDefault(targetDomainObject.getClass(), null);
        if (authorityEvaluator == null) {
            return false;
        }
        return authorityEvaluator.hasPermission(myUserDetails, targetDomainObject, permission);
    }

    /**
     * 判断指定用户对指定类型和ID的资源持有指定权限
     * @param authentication 用户认证信息
     * @param targetId       目标资源id
     * @param targetType     目标资源类型
     * @param permission     权限
     * @return 是否持有权限
     */
    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        if (targetId == null) {
            return false;
        }
        final MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        //如果持有 admin 角色 ，直接放行
        if (myUserDetails.hasRole(ROLE_ADMIN)) {
            return true;
        }
        initNameMap();
        //从 nameMap 中选择  权限评估器
        final AuthorityEvaluator authorityEvaluator = this.nameMap.getOrDefault(targetType, null);
        if (authorityEvaluator == null) {
            return false;
        }
        return authorityEvaluator.hasPermission(myUserDetails, targetId, permission);
    }

    /**
     * 初始化 classMap
     */
    public void initClassMap() {
        if (this.classMap != null) {
            return;
        }
        HashMap<Class<?>, AuthorityEvaluator> map = new HashMap<>(1);
        final Collection<AuthorityEvaluator> authorityEvaluators = getAuthorityEvaluators();
        for (AuthorityEvaluator authorityEvaluator : authorityEvaluators) {
            final List<Class<?>> targetClass = authorityEvaluator.getTargetClass();
            for (Class<?> c : targetClass) {
                if (!map.containsKey(c)) {
                    map.put(c, authorityEvaluator);
                } else {
                    throw new AuthorityEvaluatorDuplicatedException("类:" + c.getSimpleName());
                }
            }
        }
        this.classMap = map;
    }

    /**
     * 初始化 nameMap
     */
    public void initNameMap() {
        if (this.nameMap != null) {
            return;
        }
        HashMap<String, AuthorityEvaluator> map = new HashMap<>(1);
        final Collection<AuthorityEvaluator> authorityEvaluators = getAuthorityEvaluators();
        for (AuthorityEvaluator authorityEvaluator : authorityEvaluators) {
            final List<String> targetTypes = authorityEvaluator.getTargetTypes();
            for (String type : targetTypes) {
                if (!map.containsKey(type)) {
                    map.put(type, authorityEvaluator);
                } else {
                    throw new AuthorityEvaluatorDuplicatedException(type);
                }
            }
        }
        this.nameMap = map;
    }

}
