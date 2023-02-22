package com.gin.springboot3template.operationlog.strategy;

import com.gin.springboot3template.operationlog.bo.FieldDifference;
import com.gin.springboot3template.operationlog.bo.OperationLogContext;
import com.gin.springboot3template.sys.bo.FieldValue;
import com.gin.springboot3template.sys.utils.ReflectUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 抽象更新策略
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/22 09:18
 */
@Slf4j
public abstract class AbstractUpdateStrategy implements DescriptionStrategy {

    /**
     * 生成描述(比较修改了哪些字段
     * @param context 上下文
     * @return 描述
     */
    @Override
    public final String generateDescription(OperationLogContext context) {
        final Object beforeEntity = getBeforeEntity(context);
        final Object updateEntity = getUpdateEntity(context);
        if (beforeEntity == null || updateEntity == null) {
            log.warn("两个实体不全, 无法比较, 原实体: {} 修改内容: {}", beforeEntity != null, updateEntity != null);
            return null;
        }
        if (!beforeEntity.getClass().equals(updateEntity.getClass())) {
            log.warn("两个实体的类型不同, 无法比较: {} -> {}", beforeEntity.getClass(), updateEntity.getClass());
            return null;
        }
        //获取他们的所有字段和字段值
        final List<FieldValue> beforeFieldValues = ReflectUtils.getAllFieldValues(beforeEntity);
        final List<FieldValue> updateFieldValues = ReflectUtils.getAllFieldValues(updateEntity);
        // 字段差异
        return compare(beforeFieldValues, updateFieldValues).stream()
                // 字段差异格式化
                .map(dif -> {
                    final String fieldName = formatField(dif.field());
                    final String beforeValue = formatValue(dif.field(), dif.beforeValue());
                    final String updateValue = formatValue(dif.field(), dif.updateValue());
                    return new FieldDifference<>(fieldName, beforeValue, updateValue);
                })
                // 连接成字符串
                .map(d -> String.format("字段 [%s] 从 '%s' 更新为 '%s'",
                                        d.field(),
                                        d.beforeValue(),
                                        d.updateValue())).collect(Collectors.joining(", "));
    }

    /**
     * 格式化字段值
     * @param field 字段
     * @param value 字段值
     * @return 字段值
     */
    public abstract String formatValue(Field field, Object value);

    /**
     * 格式化字段名
     * @param field 字段
     * @return 字段名
     */
    public abstract String formatField(Field field);

    /**
     * 获取修改内容的实体对象
     * @param context 上下文
     * @return 修改内容的实体对象
     */
    @Nullable
    public abstract Object getUpdateEntity(OperationLogContext context);

    /**
     * 获取修改前的实体对象
     * @param context 上下文
     * @return 修改前的实体对象
     */
    @Nullable
    public abstract Object getBeforeEntity(OperationLogContext context);

    /**
     * 比较两组字段值的不同
     * @param beforeFieldValues 修改前的实体对象
     * @param updateFieldValues 修改内容的实体对象
     * @return 字段值不同
     */
    @NotNull
    public abstract List<FieldDifference<Field, Object>> compare(List<FieldValue> beforeFieldValues, List<FieldValue> updateFieldValues);

}
