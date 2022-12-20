package com.gin.springboot3template.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gin.springboot3template.sys.base.BasePageParam;
import com.gin.springboot3template.sys.response.ResPage;
import com.gin.springboot3template.sys.utils.JacksonUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
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
        final List<Map<String, Object>> maps = entities.stream().map(JacksonUtils::obj2Map).toList();
        final List<Long> idExists = maps.stream().map(i -> Long.parseLong(String.valueOf(i.get("id")))).toList();
        return ids.stream().filter(i -> !idExists.contains(i)).toList();
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

}
