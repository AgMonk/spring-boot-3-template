package com.gin.springboot3template.sys.controller;

import com.gin.springboot3template.sys.annotation.MyRestController;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/10 11:55
 */
@MyRestController("/test")
@Tag(name = "测试")
@Slf4j
@RequiredArgsConstructor
public class TestController {

}
