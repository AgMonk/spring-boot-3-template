package com.gin.springboot3template.operationlog.strategy;

import com.gin.springboot3template.operationlog.annotation.LogStrategy;
import com.gin.springboot3template.operationlog.bo.OperationLogContext;
import com.gin.springboot3template.operationlog.enums.OperationType;
import com.gin.springboot3template.sys.base.BaseVo;
import com.gin.springboot3template.sys.response.Res;
import com.gin.springboot3template.sys.utils.ReflectUtils;
import com.gin.springboot3template.sys.utils.TimeUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 默认添加策略
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/18 16:38
 */
@Component
@LogStrategy(type = OperationType.ADD)
public class DefaultAddLogStrategy extends AbstractLogStrategy {
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

}
