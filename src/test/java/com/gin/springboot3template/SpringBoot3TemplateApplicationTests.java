package com.gin.springboot3template;

import com.gin.springboot3template.operationlog.service.OpLogService;
import com.gin.springboot3template.sys.entity.SystemUser;
import com.gin.springboot3template.sys.utils.JacksonUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringBoot3TemplateApplicationTests {

    @Autowired
    OpLogService opLogService;

    @Test
    void contextLoads() {
        JacksonUtils.printPretty(opLogService.countGroupBySubClass(SystemUser.class, 1L, false));

//        final OperationLogPageParam param = new OperationLogPageParam(SystemUser.class);
//        param.setSize(5);
//        param.setPage(2);
//        JacksonUtils.printPretty(opLogService.pageByParam(param, false).getData());
    }

}
