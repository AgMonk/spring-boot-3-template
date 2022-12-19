package com.gin.springboot3template.sys.dto.param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gin.springboot3template.sys.base.BasePageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户分页查询参数
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/19 16:29
 */
@Getter
@Setter
@Schema(description = "用户分页查询参数")
public class SystemUserPageParam extends BasePageParam {

    @Override
    public void handleQueryWrapper(QueryWrapper<?> queryWrapper) {
        queryWrapper
                .orderByDesc("id")
        ;
    }
}
