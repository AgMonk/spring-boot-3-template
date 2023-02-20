package com.gin.springboot3template.operationlog.strategy;

import com.gin.springboot3template.operationlog.annotation.LogStrategy;
import com.gin.springboot3template.operationlog.bo.OperationLogContext;
import com.gin.springboot3template.operationlog.bo.ParamArg;
import com.gin.springboot3template.operationlog.enums.OperationType;
import com.gin.springboot3template.sys.base.BasePo;
import com.gin.springboot3template.sys.base.BaseVo;
import com.gin.springboot3template.sys.response.Res;
import com.gin.springboot3template.sys.utils.ReflectUtils;
import com.gin.springboot3template.sys.utils.TimeUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 默认添加策略
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/18 16:38
 */
@Component
@LogStrategy(type = OperationType.ADD)
public class DefaultAddLogStrategy implements OperationLogStrategy {


    /**
     * 尝试从请求参数中获取, 如果请求参数的类中有一个返回 BasePo 子类的方法，使用该返回类型
     * @param paramArgs 参数和参数值
     * @return 类对象
     */
    private static Class<?> findEntityClassFromParam(List<ParamArg> paramArgs) {
        return paramArgs.stream()
                .map(ParamArg::parameter)
                .map(Parameter::getType)
                .flatMap(clazz -> Arrays.stream(clazz.getDeclaredMethods()).map(Method::getReturnType))
                .filter(BasePo.class::isAssignableFrom)
                .findFirst().orElse(null);
    }

    /**
     * 尝试从返回结果获取 , 如果返回结果为 Res<? extents BaseVo> , 有一个单参构造函数，且参数为 BasePo 子类，使用该参数类
     * @param result 返回结果
     * @return 类对象
     */
    private static Class<?> findEntityClassFromResult(Object result) {
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
     * 获取日志描述
     * @param context 上下文
     * @return 日志描述
     */
    @NotNull
    @Override
    public String getDescription(OperationLogContext context) {
        // 如果返回对象为 Res 类型 且 data 字段为 Vo 类型
        List<String> list = new ArrayList<>();

        if (context.result() instanceof Res<?> res && res.getData() instanceof BaseVo vo) {
            list.add("Id: " + vo.getId());
            list.add("创建时间: " + TimeUtils.format(vo.getTimeCreate()));

            ReflectUtils.getFieldValues(vo).stream().filter(f -> f.value() != null).forEach(fieldValue -> {
                final Field field = fieldValue.field();
                final Object value = fieldValue.value();
                final Schema schema = field.getAnnotation(Schema.class);
                final String label = schema != null ? schema.description() : field.getName();
                list.add(label + ": " + value);
            });
        }
        return String.join(", ", list);
    }

    /**
     * 获取关联实体类型
     * @param context 上下文
     * @return 关联实体类型
     */
    @NotNull
    @Override
    public Class<?> getEntityClass(OperationLogContext context) {
        //尝试从其他地方获取
        final Class<?> fromResult = findEntityClassFromResult(context.result());
        if (fromResult != null) {
            return fromResult;
        }

        final Class<?> fromParam = findEntityClassFromParam(context.paramArgs());
        if (fromParam != null) {
            return fromParam;
        }


        return Object.class;
    }

    /**
     * 获取关联实体ID
     * @param context 上下文
     * @return 关联实体ID
     */
    @NotNull
    @Override
    public Long getEntityId(OperationLogContext context) {
        // 如果返回对象为 Res 类型 且 data 字段为 Vo 类型
        if (context.result() instanceof Res<?> res && res.getData() instanceof BaseVo vo) {
            return vo.getId();
        }
        // 表示未知
        return -1L;
    }
}
