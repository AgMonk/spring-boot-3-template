package com.gin.springboot3template.sys.utils;

import com.gin.springboot3template.sys.bo.FieldValue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 反射工具类
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/18 17:15
 */
public class ReflectUtils {

    /**
     * 返回一个对象所有的字段和字段值(含父类)
     * @param obj 对象
     * @return 字段和字段值
     */
    public static List<FieldValue> getAllFieldValues(Object obj) {
        return getAllFields(obj.getClass()).stream().map(field -> {
            final Object value = getFieldValue(field, obj);
            return new FieldValue(field, value);
        }).toList();
    }

    /**
     * 返回一个类的所有字段，含所有父类字段
     * @param clazz 类对象
     * @return 字段
     */
    public static List<Field> getAllFields(Class<?> clazz) {
        List<Field> list = new ArrayList<>(List.of(clazz.getDeclaredFields()));
        final Class<?> superclass = clazz.getSuperclass();
        if (superclass != null) {
            list.addAll(0, getAllFields(superclass));
        }
        return list;
    }

    public static Object getFieldValue(Field field, Object obj) {
        try {
            field.setAccessible(true);
            final Object res = field.get(obj);
            field.setAccessible(false);
            return res;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 返回一个对象所有的字段和字段值
     * @param obj 对象
     * @return 字段和字段值
     */
    public static List<FieldValue> getFieldValues(Object obj) {
        return Arrays.stream(obj.getClass().getDeclaredFields()).map(field -> {
            final Object value = getFieldValue(field, obj);
            return new FieldValue(field, value);
        }).toList();
    }

}   
