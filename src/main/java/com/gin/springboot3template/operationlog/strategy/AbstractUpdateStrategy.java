package com.gin.springboot3template.operationlog.strategy;

import com.gin.springboot3template.operationlog.bo.FieldDifference;
import com.gin.springboot3template.operationlog.bo.OperationLogContext;
import com.gin.springboot3template.sys.bo.FieldValue;
import com.gin.springboot3template.sys.utils.ReflectUtils;
import lombok.extern.slf4j.Slf4j;

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
            log.warn("实体对象数据不全, 无法比较 修改前对象: {} , 修改内容对象: {}", beforeEntity != null, updateEntity != null);
            return null;
        }
        //获取他们的所有字段和字段值
        final List<FieldValue> beforeFieldValues = ReflectUtils.getAllFieldValues(beforeEntity);
        final List<FieldValue> updateFieldValues = ReflectUtils.getAllFieldValues(updateEntity);
        final List<FieldDifference<Field, Object>> differences = compare(beforeFieldValues, updateFieldValues);
        final List<FieldDifference<String, String>> diff = format(differences);
        return diff.stream().map(d -> String.format("字段 %s 从 '%s' 更新为 '%s'",
                                                    d.field(),
                                                    d.beforeValue(),
                                                    d.afterValue())).collect(Collectors.joining(", "));
    }

    /**
     * 获取修改内容的实体对象
     * @param context 上下文
     * @return 修改内容的实体对象
     */
    public abstract Object getUpdateEntity(OperationLogContext context);

    /**
     * 获取修改前的实体对象
     * @param context 上下文
     * @return 修改前的实体对象
     */
    public abstract Object getBeforeEntity(OperationLogContext context);

    /**
     * 比较两组字段值的不同
     * @param beforeFieldValues 修改前的实体对象
     * @param updateFieldValues 修改内容的实体对象
     * @return 字段值不同
     */
    public abstract List<FieldDifference<Field, Object>> compare(List<FieldValue> beforeFieldValues, List<FieldValue> updateFieldValues);

    /**
     * 字段差异格式化
     * @param differences 字段差异
     * @return 格式化之后的字段差异
     */
    public abstract List<FieldDifference<String, String>> format(List<FieldDifference<Field, Object>> differences);
}
