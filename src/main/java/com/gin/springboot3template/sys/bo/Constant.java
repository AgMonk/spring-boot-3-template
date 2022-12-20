package com.gin.springboot3template.sys.bo;

import java.util.List;

/**
 * 常量
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/16 12:26
 */
public class Constant {
    public static final String APPLICATION_JSON_CHARSET_UTF_8 = "application/json;charset=UTF-8";

    /**
     * 角色相关
     */
    public static class Role {
        /**
         * 预设角色 超管
         */
        public static final String ADMIN = "admin";
        /**
         * 预设角色 角色管理员
         */
        public static final String ROLE_MANAGER = "roleManager";
        /**
         * 预设角色集合
         */
        public static final List<String> DEFAULT_ROLES = List.of(ADMIN, ROLE_MANAGER);
    }

    /**
     * 安全相关
     */
    public static class Security {

        public static final String DEFAULT_ROLE_PREFIX = "ROLE_";
        public static final String LOGOUT_URI = "/sys/user/logout";
        /**
         * 密码最大位数
         */
        public static final int PASSWORD_MAX_LENGTH = 20;
        /**
         * 密码最小位数
         */
        public static final int PASSWORD_MIN_LENGTH = 6;
        /**
         * preAuthority注解的内容,含义为:需要访问当前接口uri的权限,或者是admin角色
         */
        public static final String PRE_AUTHORITY_URI_OR_ADMIN = "hasAuthority(#request.requestURI) or hasRole('admin')";
        public static final String REMEMBER_ME_KEY = "rememberMe";
        public static final String VERIFY_CODE_KEY = "vc";
    }

    /**
     * 消息通知类
     */
    public static class Messages {
        public static final String ACCESS_DENIED = "禁止访问";
        public static final String DATA_NOT_FOUND = "没有找到数据";
        public static final String FORBIDDEN_CONFIG_ADMIN = "不能分配/取消分配 admin 角色";
        public static final String NOT_CONFIG_ADMIN = "不能对持有 admin 角色 的用户进行操作";
    }

    /**
     * api 路径名
     */
    public static class Api {
        /**
         * 添加接口路径
         */
        public static final String ADD = "add";
        /**
         * 删除接口路径
         */
        public static final String DEL = "del";
        /**
         * 通过关键字检索对象接口路径
         */
        public static final String FILTER = "filter";
        /**
         * 查询全部接口路径
         */
        public static final String LIST_ALL = "listAll";
        /**
         * 查询筛选条件接口路径
         */
        public static final String OPTIONS = "options";
        /**
         * 分页查询接口路径
         */
        public static final String PAGE = "page";
        /**
         * 更新接口路径
         */
        public static final String UPDATE = "update";
    }

}
