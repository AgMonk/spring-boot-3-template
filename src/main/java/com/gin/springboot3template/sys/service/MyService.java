package com.gin.springboot3template.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gin.springboot3template.sys.base.BasePageParam;
import com.gin.springboot3template.sys.response.ResPage;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/10 16:46
 */
public interface MyService<T> extends IService<T> {
    /**
     * 根据参数执行分页查询
     * @param param 参数
     * @return 分页数据
     */
    default ResPage<T> pageByParam(BasePageParam param) {
        return pageByParam(param, new QueryWrapper<>());
    }

    /**
     * 根据参数执行分页查询
     * @param param 参数
     * @param qw    查询条件
     * @return 分页数据
     */
    default ResPage<T> pageByParam(BasePageParam param, QueryWrapper<T> qw) {
        qw = qw != null ? qw : new QueryWrapper<>();
        //添加条件
        param.handleQueryWrapper(qw);
        return ResPage.of(page(new Page<>(param.getPage(), param.getSize()), qw));
    }
}
