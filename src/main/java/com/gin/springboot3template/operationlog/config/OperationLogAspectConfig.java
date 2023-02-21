package com.gin.springboot3template.operationlog.config;

import com.gin.springboot3template.operationlog.annotation.OpLog;
import com.gin.springboot3template.operationlog.service.SystemOperationLogService;
import com.gin.springboot3template.sys.utils.SpElUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * 修改操作日志切面配置
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/18 09:49
 */
@Configuration
@Aspect
@Slf4j
@RequiredArgsConstructor
public class OperationLogAspectConfig {
    private final SystemOperationLogService logService;

    @Around("@annotation(com.gin.springboot3template.operationlog.annotation.OpLog)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        // 注解
        final OpLog opLog = ((MethodSignature) pjp.getSignature()).getMethod().getAnnotation(OpLog.class);
        final Class<?> mainClass = opLog.mainClass();
        final Class<?> subClass = opLog.subClass();
        final String mainId = opLog.mainId();
        final String subId = opLog.subId();

        final Object result = pjp.proceed();

        final StandardEvaluationContext context = SpElUtils.createContext(pjp);
        context.setVariable("result", result);


        // 保存日志

        return result;

    }

}
