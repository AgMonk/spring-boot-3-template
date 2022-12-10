package com.gin.springboot3template.sys.base;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * 基础分页查询参数
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/10 11:39
 */
@Getter
@Setter
@Validated
@ApiModel("分页查询条件")
public abstract class BasePageParam {
    @ApiModelProperty(value = "当前页", example = "1", notes = "最小值为1")
    @Min(value = 1L, message = "页码最小为1")
    int page = 1;

    @ApiModelProperty(value = "每页条数", example = "10", notes = "最小值为10，最大值为50")
    @Min(value = 10L, message = "条数最小为1")
    @Max(value = 50L, message = "条数最大为50")
    int size = 1;

    /**
     * 向queryWrapper中添加条件
     * @param queryWrapper 查询条件
     */
    protected abstract void handleQueryWrapper(QueryWrapper<?> queryWrapper);

}   
