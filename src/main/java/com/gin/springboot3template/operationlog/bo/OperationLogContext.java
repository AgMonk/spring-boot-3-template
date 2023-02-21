package com.gin.springboot3template.operationlog.bo;

import com.gin.springboot3template.operationlog.enums.OperationType;

import java.util.List;

/**
 * 操作日志上下文
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/20 12:39
 */
public record OperationLogContext(
        Class<?> entityClass,
        Long entityId,
        //  方法参数和参数值
        List<ParamArg> paramArgs,
        //方法执行结果
        Object result,
        //表达式计算结果
        List<Object> expressions,
        OperationType type, jakarta.servlet.http.HttpServletRequest request) {

}
