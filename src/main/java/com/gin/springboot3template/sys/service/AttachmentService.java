package com.gin.springboot3template.sys.service;

import com.gin.springboot3template.sys.config.SystemProperties;
import com.gin.springboot3template.sys.utils.SpringContextUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 附件服务
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/24 13:45
 */
public interface AttachmentService<T> extends MyService<T> {
    /**
     * 附件在根目录下保存的目录名 默认使用 T 类型名称 + "/attach"
     * @return 附近目录
     */
    default String attachPath() {
        final String path = "/attach";
        final Type superclass = this.getClass().getGenericSuperclass();
        if (superclass instanceof ParameterizedType type) {
            if (type.getActualTypeArguments()[0] instanceof Class<?> clazz) {
                return clazz.getSimpleName() + path;
            }
            return type.getTypeName() + path;
        }
        return null;
    }

    /**
     * 保存附件的根目录 默认使用容器中的 SystemProperties 的 homePath
     * @return 根目录
     */
    default String homePath() {
        return SpringContextUtils.getContext().getBean(SystemProperties.class).getHomePath();
    }
}
