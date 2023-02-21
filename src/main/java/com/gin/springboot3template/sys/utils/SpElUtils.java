package com.gin.springboot3template.sys.utils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.StandardReflectionParameterNameDiscoverer;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;

/**
 * SpEl表达式工具类
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/21 09:51
 */
public class SpElUtils {

    /**
     * 生成表达式上下文
     * @param joinPoint 接触点
     * @return spEl表达式上下文
     */
    public static StandardEvaluationContext createContext(JoinPoint joinPoint) {
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
    public static Object getElValue(String spEl, StandardEvaluationContext context) {
        return ObjectUtils.isEmpty(spEl) ? null : new SpelExpressionParser().parseExpression(spEl).getValue(context);
    }
}
