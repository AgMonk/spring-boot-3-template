package com.gin.springboot3template.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gin.springboot3template.sys.base.BasePageParam;
import com.gin.springboot3template.sys.response.ResPage;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.cglib.beans.BeanMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/10 16:46
 */
public interface MyService<T> extends IService<T> {
    /**
     * 返回不存在的主键id
     * @param ids id
     * @return 不存在的id
     */
    default List<Long> findNotExistsId(Collection<Long> ids) {
        final List<T> entities = listByIds(ids);
        if (entities.size() == 0) {
            return new ArrayList<>(ids);
        }
        final BeanMap beanMap = BeanMap.create(entities.get(0));
        final List<Long> idExists = entities.stream().map(entity -> {
            beanMap.setBean(entity);
            return Long.parseLong(String.valueOf(beanMap.get(getPrimaryKey())));
        }).toList();
        return ids.stream().filter(i -> !idExists.contains(i)).toList();
    }

    /**
     * 返回本实体的主键字段名
     * @return 本实体的主键字段名
     */
    default String getPrimaryKey() {
        return "id";
    }

    /**
     * 根据指定列分组并仅查询这些列 , 一般用于查询这些列中已被使用过的值 , 用于分页查询的条件
     * @param columns 列名
     * @return 这些列中已被使用过的值
     */
    default List<T> listGroup(String... columns) {
        if (ArrayUtils.isEmpty(columns)) {
            return new ArrayList<>();
        }
        final QueryWrapper<T> qw = new QueryWrapper<>();
        qw.select(columns).groupBy(Arrays.asList(columns));
        return list(qw);
    }

    /**
     * 根据参数执行分页查询 ,并返回转换后的对象
     * @param param 参数
     * @param func  转换方法
     * @return 分页数据
     */
    default <R> ResPage<R> pageByParam(BasePageParam param, Function<T, R> func) {
        return pageByParam(param, null, func);
    }

    /**
     * 根据参数执行分页查询 ,并返回转换后的对象
     * @param param 参数
     * @param qw    查询条件
     * @param func  转换方法
     * @param <R>   转换目标类型
     * @return 分页数据
     */
    default <R> ResPage<R> pageByParam(BasePageParam param, QueryWrapper<T> qw, Function<T, R> func) {
        return ResPage.of(pageByParam(param, qw), func);
    }

    /**
     * 根据参数执行分页查询
     * @param param 参数
     * @return 分页数据
     */
    default Page<T> pageByParam(BasePageParam param) {
        return pageByParam(param, new QueryWrapper<>());
    }

    /**
     * 根据参数执行分页查询
     * @param param 参数
     * @param qw    查询条件
     * @return 分页数据
     */
    default Page<T> pageByParam(BasePageParam param, QueryWrapper<T> qw) {
        qw = qw != null ? qw : new QueryWrapper<>();
        //添加条件
        param.handleQueryWrapper(qw);
        return page(new Page<>(param.getPage(), param.getSize()), qw);
    }

}
