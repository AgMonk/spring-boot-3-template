package com.gin.springboot3template.operationlog.strategy;

import com.gin.springboot3template.operationlog.bo.DescriptionContext;
import com.gin.springboot3template.sys.utils.JacksonUtils;
import com.gin.springboot3template.test.TestUser;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/18 16:29
 */
@Component
public class TestUserDescriptionStrategy extends AbstractDescriptionStrategy {
    /**
     * 添加策略
     * @param pjp     接触点
     * @param context 上下文
     * @return 返回
     * @throws Throwable 异常
     */
    @Override
    public Object add(ProceedingJoinPoint pjp, DescriptionContext context) throws Throwable {
        final Object proceed = pjp.proceed();
        JacksonUtils.printPretty(proceed);
        return proceed;
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
