package com.gin.springboot3template.sys.utils;

import java.lang.reflect.Field;

/**
 * 反射工具类
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/18 17:15
 */
public class ReflectUtils {

    public static Object get(Field field, Object obj) throws IllegalAccessException {
        field.setAccessible(true);
        final Object res = field.get(obj);
        field.setAccessible(false);
        return res;
    }

    public static long getLong(Field field, Object obj) throws IllegalAccessException {
        final Object res = get(field, obj);
        if (res == null) {
            return 0;
        }
        return Long.parseLong(String.valueOf(res));
    }

    public static String getString(Field field, Object obj) throws IllegalAccessException {
        final Object res = get(field, obj);
        if (res == null) {
            return null;
        }
        return String.valueOf(res);
    }


}   
