package com.gin.springboot3template;

import com.gin.springboot3template.operationlog.entity.SystemOperationLog;
import com.gin.springboot3template.operationlog.enums.OperationType;
import com.gin.springboot3template.operationlog.service.SystemOperationLogService;
import com.gin.springboot3template.sys.entity.SystemUser;
import com.gin.springboot3template.sys.utils.JacksonUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringBoot3TemplateApplicationTests {
    @Autowired
    SystemOperationLogService service;

    @Test
    void contextLoads() {
        final SystemOperationLog entity = new SystemOperationLog();
        entity.setDescription("测试测试");
        entity.setEntityClass(SystemUser.class);
        entity.setEntityId(1L);
        entity.setUserId(1L);
        entity.setType(OperationType.ADD);
        entity.setUserIp("192.168.0.200");

        service.save(entity);

        JacksonUtils.printPretty(service.getById(entity.getId()));
    }

}
