package com.gin.springboot3template.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gin.springboot3template.sys.annotation.MyRestController;
import com.gin.springboot3template.sys.base.BasePageParam;
import com.gin.springboot3template.sys.entity.SystemUser;
import com.gin.springboot3template.sys.response.Res;
import com.gin.springboot3template.sys.service.SystemUserService;
import com.gin.springboot3template.sys.service.impl.SystemUserServiceImpl;
import com.gin.springboot3template.sys.validation.EntityId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class TestController {

    private final SystemUserService systemUserService;

    @GetMapping("page")
    @Operation(summary = "分页查询")
    public Res<Void> test(@ParameterObject @Validated PageParam pageParam) {
        System.out.println("pageParam.getId() = " + pageParam.getId());
        return Res.of(null);
    }

    @GetMapping("page1")
    @Operation(summary = "分页查询")
    public Res<Void> test(Integer id) {
        final SystemUser systemUser = systemUserService.getById(id);
        System.out.println("systemUser = " + systemUser);
        return Res.of(null);
    }


    @Getter
    @Setter
    @Schema(name = "测试查询参数")
    public static class PageParam extends BasePageParam {
        @Schema(description = "ID")
        @NotNull
        @EntityId(service = SystemUserServiceImpl.class)
        Integer id;

        @Override
        protected void handleQueryWrapper(QueryWrapper<?> queryWrapper) {
            System.out.println("id = " + id);
        }
    }
}   
