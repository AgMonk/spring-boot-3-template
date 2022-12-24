package com.gin.springboot3template.sys.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 集合工具类
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/24 11:47
 */
public class CollUtils {

    /**
     * 交集的补集（析取）
     * @param colA 集合A
     * @param colB 集合B
     * @param <T>  集合成员类型
     * @return 交集
     */
    public static <T> List<T> disjunction(Collection<T> colA, Collection<T> colB) {
        final List<T> union = union(colA, colB);
        final List<T> intersection = intersection(colA, colB);

        return subtract(union, intersection);
    }

    /**
     * 求交集
     * @param colA 集合A
     * @param colB 集合B
     * @param <T>  集合成员类型
     * @return 交集
     */
    public static <T> List<T> intersection(Collection<T> colA, Collection<T> colB) {
        assert colA != null;
        assert colB != null;
        return colA.stream().filter(colB::contains).collect(Collectors.toList());
    }

    /**
     * 差集（扣除）
     * @param colA 集合A
     * @param colB 集合B
     * @param <T>  集合成员类型
     * @return 差集（扣除）
     */
    public static <T> List<T> subtract(Collection<T> colA, Collection<T> colB) {
        return colA.stream().filter(o -> !colB.contains(o)).collect(Collectors.toList());
    }

    /**
     * 求并集
     * @param colA 集合A
     * @param colB 集合B
     * @param <T>  集合成员类型
     * @return 并集
     */
    public static <T> List<T> union(Collection<T> colA, Collection<T> colB) {
        final HashSet<T> set = new HashSet<>();
        set.addAll(colA);
        set.addAll(colB);
        return new ArrayList<>(set);
    }

}   
