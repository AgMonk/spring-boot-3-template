package com.gin.springboot3template.sys.bo;

import java.util.List;
import java.util.regex.Pattern;

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
    public static final String MESSAGE_DATA_NOT_FOUND = "没有找到数据";
    /**
     * preAuthority注解的内容,含义为:需要访问当前接口uri的权限,或者是admin角色
     */
    public static final String PRE_AUTHORITY_URI_OR_ADMIN = "hasAuthority(#request.requestURI) or hasRole('admin')";
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

    /**
     * 权限评估相关
     */
    public static class Evaluator {

        public static final Pattern HAS_PERMISSION_CLASS_PATTERN = Pattern.compile("^hasPermission\\((.+?),(.+?)\\)$");
        public static final Pattern HAS_PERMISSION_TYPE_PATTERN = Pattern.compile("^hasPermission\\((.+?),(.+?),(.+?)\\)$");
        /**
         * 判断根据路径访问的权限
         */
        public static final String STRING_PATH = "hasPermission(#request.requestURI,'路径','访问')";
        public static final Pattern STRING_PATTERN = Pattern.compile("^'(.*)'$");
        /**
         * 权限评估期负责类型
         */
        public static final String TYPE_PATH = "路径";
        /**
         * 权限评估期负责类型
         */
        public static final String TYPE_ROLE = "角色";
    }
}
