package com.gin.springboot3template.operationlog.strategy;

import com.gin.springboot3template.operationlog.service.SystemOperationLogService;
import com.gin.springboot3template.test.TestUser;
import org.springframework.stereotype.Component;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/18 16:29
 */
@Component
public class TestUserDescriptionStrategy extends AbstractDescriptionStrategy {

    public TestUserDescriptionStrategy(SystemOperationLogService logService) {
        super(logService);
    }

    /**
     * 被操作的实体类型
     * @return 类对象
     */
    @Override
    public Class<?> clazz() {
        return TestUser.class;
    }
}
