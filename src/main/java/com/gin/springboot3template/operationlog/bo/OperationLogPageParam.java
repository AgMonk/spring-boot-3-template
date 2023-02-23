package com.gin.springboot3template.operationlog.bo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gin.springboot3template.operationlog.enums.OperationType;
import com.gin.springboot3template.sys.base.BasePageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 操作日志查询参数
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/23 13:33
 */
@Getter
@Setter
@Schema(description = "操作日志查询条件")
public final class OperationLogPageParam extends BasePageParam {
    @Schema(description = "主实体类型", hidden = true)
    @JsonIgnore
    Class<?> mainClass;
    @Schema(description = "主实体ID", hidden = true)
    @JsonIgnore
    Long mainId;
    @Schema(description = "操作类型")
    List<OperationType> type;
    @Schema(description = "副实体类型")
    Class<?> subClass;
    @Schema(description = "最晚时间")
    Long maxTime;
    @Schema(description = "最早时间")
    Long minTime;


    /**
     * 向queryWrapper中添加条件
     * @param queryWrapper 查询条件
     */
    @Override
    public void handleQueryWrapper(QueryWrapper<?> queryWrapper) {
        queryWrapper.orderByAsc("time_create");
        if (mainId != null) {
            queryWrapper.eq("main_id", mainId);
        }
        if (mainClass != null) {
            queryWrapper.eq("main_class", mainClass.getName());
        }
        if (subClass != null && !subClass.equals(mainClass)) {
            queryWrapper.eq("sub_class", subClass.getName());
        }
        if (maxTime != null) {
            queryWrapper.lt("time_create", maxTime);
        }
        if (minTime != null) {
            queryWrapper.ge("time_create", minTime);
        }
        if (!CollectionUtils.isEmpty(type)) {
            queryWrapper.in("type", type.stream().map(Enum::name).toList());
        }
    }
}
