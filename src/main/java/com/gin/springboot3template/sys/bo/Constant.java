package com.gin.springboot3template.sys.bo;

import java.util.List;

/**
 * 常量
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/16 12:26
 */
public class Constant {
    public static final String ACCESS_DENIED = "禁止访问";
    /**
     * 添加接口路径
     */
    public static final String API_ADD = "add";
    /**
     * 删除接口路径
     */
    public static final String API_DEL = "del";
    /**
     * 通过关键字检索对象接口路径
     */
    public static final String API_FILTER = "filter";
    /**
     * 查询全部接口路径
     */
    public static final String API_LIST_ALL = "listAll";
    /**
     * 查询筛选条件接口路径
     */
    public static final String API_OPTIONS = "options";
    /**
     * 分页查询接口路径
     */
    public static final String API_PAGE = "page";
    /**
     * 更新接口路径
     */
    public static final String API_UPDATE = "update";
    public static final String APPLICATION_JSON_CHARSET_UTF_8 = "application/json;charset=UTF-8";
    public static final String DEFAULT_ROLE_PREFIX = "ROLE_";
    public static final String HAS_PERMISSION = "hasPermission";
    public static final String REMEMBER_ME_KEY = "rememberMe";
    /**
     * 预设角色 超管
     */
    public static final String ROLE_ADMIN = "admin";
    /**
     * 预设角色 角色管理员
     */
    public static final String ROLE_ROLE_ADMIN = "roleAdmin";
    /**
     * 预设角色 角色分配员
     */
    public static final String ROLE_ROLE_DISTRIBUTOR = "roleDistributor";
    /**
     * 预设角色集合
     */
    public static final List<String> DEFAULT_ROLES = List.of(ROLE_ADMIN, ROLE_ROLE_ADMIN, ROLE_ROLE_DISTRIBUTOR);
    public static final String VERIFY_CODE_KEY = "vc";
}
