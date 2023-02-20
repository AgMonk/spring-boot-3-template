package com.gin.springboot3template.operationlog.config;

import com.gin.springboot3template.operationlog.annotation.LogStrategy;
import com.gin.springboot3template.operationlog.annotation.OpLog;
import com.gin.springboot3template.operationlog.entity.SystemOperationLog;
import com.gin.springboot3template.operationlog.enums.OperationType;
import com.gin.springboot3template.operationlog.service.SystemOperationLogService;
import com.gin.springboot3template.operationlog.strategy.OperationLogStrategy;
import com.gin.springboot3template.sys.security.utils.MySecurityUtils;
import com.gin.springboot3template.sys.utils.SpringContextUtils;
import com.gin.springboot3template.sys.utils.WebUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.StandardReflectionParameterNameDiscoverer;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
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
     * 生成表达式上下文
     * @param joinPoint 接触点
     * @return spEl表达式上下文
     */
    private static StandardEvaluationContext createContext(JoinPoint joinPoint) {
        final StandardEvaluationContext context = new StandardEvaluationContext(SpringContextUtils.getContext());

        Object[] args = joinPoint.getArgs();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method targetMethod = methodSignature.getMethod();
        StandardReflectionParameterNameDiscoverer parameterNameDiscoverer = new StandardReflectionParameterNameDiscoverer();
        String[] parametersName = parameterNameDiscoverer.getParameterNames(targetMethod);

        if (args == null || args.length == 0) {
            return context;
        }
        for (int i = 0; i < args.length; i++) {
            //noinspection DataFlowIssue
            context.setVariable(parametersName[i], args[i]);
        }

        return context;
    }

    /**
     * 从spring容器中查找匹配的日志生成策略
     * @param clazz 操作实体类型
     * @param type  操作类型
     * @return 日志生成策略
     */
    private static List<OperationLogStrategy> findStrategies(Class<?> clazz, OperationType type) {
        // 查找匹配的日志生成策略
        return SpringContextUtils.getContext().getBeansOfType(OperationLogStrategy.class).values().stream()
                .filter(strategy -> {
                    final LogStrategy annotation = strategy.getClass().getAnnotation(LogStrategy.class);
                    if (annotation == null) {
                        return false;
                    }
                    // 返回操作实体类型 和 操作类型均匹配的策略
                    return annotation.clazz().equals(clazz) && annotation.type() == type;
                }).toList();
    }

    /**
     * 通过表达式获取值
     */
    private static Object getElValue(String spEl, StandardEvaluationContext context) {
        return ObjectUtils.isEmpty(spEl) ? null : new SpelExpressionParser().parseExpression(spEl).getValue(context);
    }

    /**
     * 解析器上下文
     */

    @Around("@annotation(com.gin.springboot3template.operationlog.annotation.OpLog)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        //签名
        final MethodSignature signature = (MethodSignature) pjp.getSignature();
        //方法
        final Method method = signature.getMethod();
        //记录注解
        final OpLog annotation = method.getAnnotation(OpLog.class);
        // 被操作的实体类型
        final Class<?> clazz = annotation.clazz();
        // 操作类型
        final OperationType type = annotation.type();
        // 解析spEL表达式
        final StandardEvaluationContext evaluationContext = createContext(pjp);
        // 请求参数
        final List<Object> params = Arrays.stream(annotation.param()).map(param -> getElValue(param, evaluationContext)).toList();
        // 请求结果
        final Object result = pjp.proceed();
        // 查找匹配的日志生成策略
        final List<OperationLogStrategy> strategies = findStrategies(clazz, type);
        // 策略不存在 直接放行
        if (CollectionUtils.isEmpty(strategies)) {
            log.warn("未找到日志生成策略 class: {} type: {}", clazz, type);
            return result;
        }
        // 生成日志对象
        final List<SystemOperationLog> logs = strategies.stream().map(strategy -> {
            final SystemOperationLog operationLog = new SystemOperationLog();
            operationLog.setType(type);
            operationLog.setUserId(MySecurityUtils.currentUserDetails().getId());
            operationLog.setUserIp(WebUtils.getRemoteHost());
            //  使用请求结果+生成策略获取 关联实体类型，关联实体ID，描述
            operationLog.setEntityId(strategy.getEntityId(result, params));
            operationLog.setEntityClass(strategy.getEntityClass(result, params));
            operationLog.setDescription(strategy.getDescription(result, params));
            return operationLog;
        }).toList();

        // 保存日志
        logService.write(logs);

        return result;

    }

}
