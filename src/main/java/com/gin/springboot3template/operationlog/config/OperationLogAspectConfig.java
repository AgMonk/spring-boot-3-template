package com.gin.springboot3template.operationlog.config;

import com.gin.springboot3template.operationlog.annotation.OpLog;
import com.gin.springboot3template.operationlog.bo.DescriptionContext;
import com.gin.springboot3template.operationlog.enums.OperationType;
import com.gin.springboot3template.operationlog.strategy.DescriptionStrategy;
import com.gin.springboot3template.sys.utils.SpringContextUtils;
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
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;

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
     * 通过表达式获取值
     */
    private static Object getElValue(String spEl, StandardEvaluationContext context) {
        return ObjectUtils.isEmpty(spEl) ? null : new SpelExpressionParser().parseExpression(spEl).getValue(context);
    }

    private static <T> T getElValue(String spEl, StandardEvaluationContext context, Class<T> clazz) {
        return ObjectUtils.isEmpty(spEl) ? null : new SpelExpressionParser().parseExpression(spEl).getValue(context, clazz);
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
        // 描述生成策略
        final DescriptionStrategy strategy = SpringContextUtils.getContext().getBeansOfType(DescriptionStrategy.class).values().stream().filter(
                bean -> bean.clazz().equals(clazz)).findFirst().orElse(null);
        // 策略不存在 直接放行
        if (strategy == null) {
            return pjp.proceed();
        }
        // 解析spEL表达式
        final StandardEvaluationContext context = createContext(pjp);
        // 请求参数
        final Object param = getElValue(annotation.param(), context);
        // 实体id
        final Long id = getElValue(annotation.id(), context, Long.class);
        // 操作类型
        final OperationType type = annotation.type();
        // 描述上下文
        final DescriptionContext dc = new DescriptionContext(param, id, type);

        switch (type) {
            case ADD -> {
                return strategy.add(pjp, dc);
            }
            case DEL -> {
                return strategy.del(pjp, dc);
            }
            case UPDATE -> {
                return strategy.update(pjp, dc);
            }
            case QUERY -> {
                return strategy.query(pjp, dc);
            }
            default -> {
                return pjp.proceed();
            }
        }

    }

}
