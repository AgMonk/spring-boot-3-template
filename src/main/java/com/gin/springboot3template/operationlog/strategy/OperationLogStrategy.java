package com.gin.springboot3template.operationlog.strategy;

import com.gin.springboot3template.operationlog.annotation.LogStrategy;
import com.gin.springboot3template.operationlog.bo.OperationLogContext;
import com.gin.springboot3template.operationlog.bo.ParamArg;
import com.gin.springboot3template.sys.base.BasePo;
import com.gin.springboot3template.sys.base.BaseVo;
import com.gin.springboot3template.sys.response.Res;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;

/**
 * 日志生成策略
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/18 15:51
 */
public interface OperationLogStrategy {
    /**
     * 尝试从请求参数中获取EntityClass, 如果请求参数的类中有一个返回 BasePo 子类的方法，使用该返回类型
     * @param paramArgs 参数和参数值
     * @return 类对象
     */
    static Class<?> findEntityClassFromParam(List<ParamArg> paramArgs) {
        return paramArgs.stream()
                .map(ParamArg::parameter)
                .map(Parameter::getType)
                .flatMap(clazz -> Arrays.stream(clazz.getDeclaredMethods()).map(Method::getReturnType))
                .filter(BasePo.class::isAssignableFrom)
                .findFirst().orElse(null);
    }

    /**
     * 尝试从返回结果获取EntityClass , 如果返回结果为 Res<? extents BaseVo> , 有一个单参构造函数，且参数为 BasePo 子类，使用该参数类
     * @param result 返回结果
     * @return 类对象
     */
    static Class<?> findEntityClassFromResult(Object result) {
        if (result instanceof Res<?> res && res.getData() instanceof BaseVo vo) {
            // 单参构造函数  且参数为 BasePo 子类
            final Constructor<?> constructor = Arrays.stream(vo.getClass().getConstructors()).filter(con -> {
                final Parameter[] parameters = con.getParameters();
                if (parameters.length != 1) {
                    return false;
                }
                return BasePo.class.isAssignableFrom(parameters[0].getType());
            }).findFirst().orElse(null);

            if (constructor != null) {
                return constructor.getParameters()[0].getType();
            }
        }
        return null;
    }

    /**
     * 获取关联实体ID
     * @param context              上下文
     * @param specifiedEntityClass 是否指定了 EntityClass
     * @return 关联实体ID
     */
    @NotNull
    Long getEntityId(OperationLogContext context, boolean specifiedEntityClass);

    /**
     * 获取关联实体类型
     * @param context 上下文
     * @return 关联实体类型
     */
    @NotNull
    Class<?> getEntityClass(OperationLogContext context);

    /**
     * 获取日志描述
     * @param context 上下文
     * @return 日志描述
     */
    @NotNull
    String getDescription(OperationLogContext context);

    /**
     * 获取日志策略注解
     * @return 日志策略注解
     */
    @NotNull
    default LogStrategy getLogStrategy() {
        return this.getClass().getAnnotation(LogStrategy.class);
    }

}
