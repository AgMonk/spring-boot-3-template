package com.gin.springboot3template.operationlog.strategy;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.gin.springboot3template.operationlog.annotation.LogStrategy;
import com.gin.springboot3template.operationlog.bo.FieldDifference;
import com.gin.springboot3template.operationlog.bo.OperationLogContext;
import com.gin.springboot3template.operationlog.enums.OperationType;
import com.gin.springboot3template.sys.bo.FieldValue;
import com.gin.springboot3template.sys.utils.TimeUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.annotations.Comment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 默认更新策略
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/22 10:41
 */
@Component
@LogStrategy(type = OperationType.UPDATE)
public class DefaultUpdateStrategy extends AbstractUpdateStrategy {

    /**
     * 比较两组字段值的不同
     * @param beforeFieldValues 修改前的实体对象
     * @param updateFieldValues 修改内容的实体对象
     * @return 字段值不同
     */
    @NotNull
    @Override
    public List<FieldDifference<Field, Object>> compare(List<FieldValue> beforeFieldValues, List<FieldValue> updateFieldValues) {
        final ArrayList<FieldDifference<Field, Object>> differences = new ArrayList<>();
        beforeFieldValues.forEach(bfv -> {
            // 字段
            final Field field = bfv.field();
            // 原字段值
            final Object beforeValue = bfv.value();
            // 修改值
            final Object updateValue = updateFieldValues.stream().filter(f -> f.field().equals(field)).map(FieldValue::value).findFirst().orElse(null);
            // 差异
            final FieldDifference<Field, Object> dif = new FieldDifference<>(field, beforeValue, updateValue);

            final TableField tableField = field.getAnnotation(TableField.class);
            if (tableField == null || tableField.updateStrategy() == FieldStrategy.DEFAULT || tableField.updateStrategy() == FieldStrategy.NOT_NULL) {
                // 修改值不为 null 且与原值不同时  认为不同
                if (updateValue != null && !updateValue.equals(beforeValue)) {
                    differences.add(dif);
                }
            } else if (tableField.updateStrategy() == FieldStrategy.NOT_EMPTY) {
                // 修改值不为 空 且与原值不同时  认为不同
                if (!ObjectUtils.isEmpty(updateValue) && !updateValue.equals(beforeValue)) {
                    differences.add(dif);
                }
            } else if (tableField.updateStrategy() == FieldStrategy.IGNORED) {
                differences.add(dif);
            }
        });
        return differences;
    }


    /**
     * 格式化字段名
     * @param field 字段
     * @return 字段名
     */
    @Override
    public String formatField(Field field) {
        final Comment comment = field.getAnnotation(Comment.class);
        final Schema schema = field.getAnnotation(Schema.class);
        // 字段名翻译
        return comment != null ? comment.value() : (schema != null ? schema.description() : field.getName());
    }

    /**
     * 格式化字段值
     * @param field 字段
     * @param value 字段值
     * @return 字段值
     */
    @Override
    public String formatValue(Field field, Object value) {
        if (value instanceof Long time && field.getName().contains("time")) {
            // 属于时间戳字段
            return TimeUtils.format(time);
        }
        return String.valueOf(value);
    }

    /**
     * 获取修改前的实体对象
     * @param context 上下文
     * @return 修改前的实体对象
     */
    @Nullable
    @Override
    public Object getBeforeEntity(OperationLogContext context) {
        // 从preExp第1个元素传入
        final List<Object> list = context.preExp();
        return list.size() > 0 ? list.get(0) : null;
    }

    /**
     * 获取修改内容的实体对象
     * @param context 上下文
     * @return 修改内容的实体对象
     */
    @Nullable
    @Override
    public Object getUpdateEntity(OperationLogContext context) {
        // 从preExp第2个元素传入
        final List<Object> list = context.preExp();
        return list.size() > 1 ? list.get(1) : null;
    }
}
