package com.gin.springboot3template;

import com.gin.springboot3template.operationlog.service.OpLogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringBoot3TemplateApplicationTests {

    @Autowired
    OpLogService opLogService;

    @Test
    void contextLoads() {
    }

}
