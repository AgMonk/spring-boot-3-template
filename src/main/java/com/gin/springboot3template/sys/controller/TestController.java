package com.gin.springboot3template.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gin.springboot3template.sys.annotation.MyRestController;
import com.gin.springboot3template.sys.base.BasePageParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/10 11:55
 */
@RestController
@MyRestController("/test")
@Tag(name = "测试")
@Slf4j
public class TestController {

    @GetMapping("page")
    @Operation(summary = "分页查询")
    public void test(@ParameterObject @Validated PageParam pageParam) {
        log.info("{} {} {} {}", pageParam.getPage(), pageParam.getSize(), pageParam.getTitle(),pageParam.getId());
    }

    @Getter
    @Setter
    @Schema(name = "测试查询参数")
    public static class PageParam extends BasePageParam {
        @Schema(description = "标题")
        @NotNull
        Integer title;

        @Schema(description = "ID")
        @NotNull
        Integer id;

        @Override
        protected void handleQueryWrapper(QueryWrapper<?> queryWrapper) {
            System.out.println(title);
        }
    }
}   
