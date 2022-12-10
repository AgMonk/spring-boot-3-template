package com.gin.springboot3template.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gin.springboot3template.sys.base.BasePageParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/10 11:55
 */
@RestController
@RequestMapping("/test")
@Api(tags = "测试")
@Slf4j
public class TestController {

    @GetMapping("page")
    @ApiOperation("分页")
    public void test(PageParam pageParam) {
        log.info("{} {} {} ", pageParam.getPage(),pageParam.getSize(),pageParam.getTitle());
    }


    @Getter
    @Setter
    @ApiModel("测试查询参数")
    public static class PageParam extends BasePageParam {
        @ApiModelProperty("标题")
        String title;

        @Override
        protected void handleQueryWrapper(QueryWrapper<?> queryWrapper) {
            System.out.println(title);
        }
    }
}   
