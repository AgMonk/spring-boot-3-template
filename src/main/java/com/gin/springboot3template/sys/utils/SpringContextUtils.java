package com.gin.springboot3template.sys.utils;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 上下文工具
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/10 16:20
 */
@Component
public class SpringContextUtils implements ApplicationContextAware {
    public static ApplicationContext applicationContext;

    /**
     * 根据指定的泛型类型 查找对应的 IService Bean
     * @param entityClass 泛型类型
     * @param <T>         泛型类型
     * @return IService Bean
     */
    public static <T> IService<T> findService(Class<T> entityClass) {
        if (applicationContext == null) {
            return null;
        }
        //noinspection unchecked
        return applicationContext.getBeansOfType(IService.class).values().stream()
                .filter(i -> i.getEntityClass().equals(entityClass))
                .findAny()
                .orElse(null);
    }

    public static ApplicationContext getContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        SpringContextUtils.applicationContext = applicationContext;
    }
}
