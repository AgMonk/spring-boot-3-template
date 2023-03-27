package com.gin.springboot3template.test.controller;

import com.gin.springboot3template.route.base.EleMenuComponent;
import com.gin.springboot3template.route.service.MenuService;
import com.gin.springboot3template.sys.annotation.MyRestController;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 测试接口
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/24 15:18
 */
@MyRestController("test")
@RequiredArgsConstructor
public class TestController {
    private final MenuService menuService;

    @GetMapping("route")
    public List<EleMenuComponent> getPdf(@RequestParam(defaultValue = "index") String name) {
        return menuService.listItemByMenuName(name);
    }

}
