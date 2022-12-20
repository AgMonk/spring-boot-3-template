package com.gin.springboot3template.sys.dto.param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gin.springboot3template.sys.base.BasePageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @author bx002
 */
@Getter
@Setter
@Schema(description = "角色分页查询参数")
public class SystemRolePageParam extends BasePageParam {
    @Schema(description = "关键字(名称,中文名称,描述,备注)")
    String key;

    @Override
    public void handleQueryWrapper(QueryWrapper<?> queryWrapper) {
        queryWrapper
                .eq("name", key).or()
                .eq("name_zh", key).or()
                .like("name", key).or()
                .like("name_zh", key).or()
                .like("description", key).or()
                .like("remark", key).or()
                .orderByDesc("id")
        ;
    }
}