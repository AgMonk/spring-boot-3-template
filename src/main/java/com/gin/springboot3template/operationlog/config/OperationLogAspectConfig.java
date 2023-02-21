package com.gin.springboot3template.operationlog.config;

import com.gin.springboot3template.operationlog.annotation.LogStrategy;
import com.gin.springboot3template.operationlog.annotation.OpLog;
import com.gin.springboot3template.operationlog.bo.OperationLogContext;
import com.gin.springboot3template.operationlog.bo.ParamArg;
import com.gin.springboot3template.operationlog.entity.SystemOperationLog;
import com.gin.springboot3template.operationlog.enums.OperationType;
import com.gin.springboot3template.operationlog.service.SystemOperationLogService;
import com.gin.springboot3template.operationlog.strategy.DescriptionStrategy;
import com.gin.springboot3template.sys.utils.SpElUtils;
import com.gin.springboot3template.sys.utils.SpringContextUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.ObjectUtils;

import java.util.List;

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

    /**
     * 查找匹配的策略
     * @param entityClass 操作的实体类型
     * @return 策略
     */
    private static DescriptionStrategy findStrategy(Class<?> entityClass) {
        return SpringContextUtils.getContext().getBeansOfType(DescriptionStrategy.class).values().stream().filter(s -> {
            // 过滤出有注解的，操作类型匹配的，实体类型包含的策略
            final LogStrategy annotation = s.getClass().getAnnotation(LogStrategy.class);
            return annotation != null && annotation.value().isAssignableFrom(entityClass);
        }).min((o1, o2) -> {
            // 排序 ，子类在前
            final Class<?> c1 = o1.getClass().getAnnotation(LogStrategy.class).value();
            final Class<?> c2 = o2.getClass().getAnnotation(LogStrategy.class).value();
            if (c1.equals(c2)) {
                return 0;
            }
            if (c1.isAssignableFrom(c2)) {
                return -1;
            }
            if (c2.isAssignableFrom(c1)) {
                return 1;
            }
            return 0;
        }).orElse(null);
    }

    @Around("@annotation(com.gin.springboot3template.operationlog.annotation.OpLog)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        // 注解
        final OpLog opLog = ((MethodSignature) pjp.getSignature()).getMethod().getAnnotation(OpLog.class);
        final List<ParamArg> paramArgs = ParamArg.parse(pjp);
        final Class<?> mainClass = opLog.mainClass();
        // 副类型如果为 object 置为null
        final Class<?> subClass = !Object.class.equals(opLog.subClass()) ? opLog.subClass() : null;
        // 操作类型
        final OperationType type = opLog.type();

        final Object result = pjp.proceed();

        // SpEl上下文
        final StandardEvaluationContext evaluationContext = SpElUtils.createContext(pjp);
        evaluationContext.setVariable("result", result);
        // 计算 SpEl表达式
        final Long mainId = SpElUtils.getElNotnullLong(evaluationContext, opLog.mainId()).stream().findFirst().orElse(null);
        final Long subId = subClass != null ? SpElUtils.getElNotnullLong(evaluationContext, opLog.subId()).stream().findFirst().orElse(null) : null;

        if (mainId == null) {
            log.warn("日志注解配置错误: mainId 计算结果为null");
            return result;
        }
        // 计算其他表达式
        final List<Object> expressions = SpElUtils.getElValues(evaluationContext, opLog.expression());


        // 匹配描述策略
        // 实际操作的实体类对象，用于匹配策略
        final Class<?> entityClass = subClass != null ? subClass : mainClass;
        final Long entityId = subClass != null ? subId : mainId;

        // 上下文
        final OperationLogContext context = new OperationLogContext(entityClass, entityId, paramArgs, result, expressions, type);
        // 日志
        final SystemOperationLog operationLog = new SystemOperationLog(type);
        operationLog.setMainClass(mainClass);
        operationLog.setMainId(mainId);
        operationLog.setSubClass(subClass);
        operationLog.setSubId(subId);

        //描述生成策略
        DescriptionStrategy descriptionStrategy = findStrategy(entityClass);
        final String msg = "未找到匹配的描述策略";
        if (descriptionStrategy == null) {
            final String des = String.format("%s class:%s type:%s mainId:%s", msg, entityClass, type, mainId);
            log.warn(des);
            operationLog.setDescription(msg);
        } else {
            operationLog.setDescription(descriptionStrategy.generateDescription(context));
        }

        // 如果描述非空 保存日志
        if (!ObjectUtils.isEmpty(operationLog.getDescription())) {
            logService.write(operationLog);
        }

        return result;

    }

}
