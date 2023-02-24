package com.gin.springboot3template.test.controller;

import com.gin.springboot3template.sys.annotation.MyRestController;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 测试接口
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/24 15:18
 */
@MyRestController("test")
public class TestController {


    @GetMapping("pdf")
    public void getPdf() {
    }
}
