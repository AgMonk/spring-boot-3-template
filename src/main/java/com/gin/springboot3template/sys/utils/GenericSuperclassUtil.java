package com.gin.springboot3template.sys.utils;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @since : 2023/2/18 16:13
 */
public class GenericSuperclassUtil {

    public static Class<?> getActualTypeArgument(Class<?> clazz) {
        return getActualTypeArgument(clazz, 0);
    }

    public static Class<?> getActualTypeArgument(Class<?> clazz, int i) {
        Class<?> entitiClass = null;
        Type genericSuperclass = clazz.getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass)
                    .getActualTypeArguments();
            if (actualTypeArguments != null && actualTypeArguments.length > 0) {
                entitiClass = (Class<?>) actualTypeArguments[i];
            }
        }
        return entitiClass;
    }


}