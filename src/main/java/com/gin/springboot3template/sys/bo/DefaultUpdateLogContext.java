package com.gin.springboot3template.sys.bo;

import com.gin.springboot3template.sys.base.BaseUpdateLog;
import lombok.Getter;
import org.hibernate.annotations.Comment;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/17 17:00
 */
@Getter
public class DefaultUpdateLogContext<T extends BaseUpdateLog, N, O> extends UpdateLogContext<T, N, O> {

    public DefaultUpdateLogContext(T updateLog, O oldData, N newData) {
        super(newData, oldData, updateLog);
    }

    /**
     * 生成操作描述
     * @param fieldName 被操作字段中文名
     * @param oldStr    修改前的值
     * @param newStr    修改后的值
     * @return 操作描述
     */
    @Override
    public String generateDescription(String fieldName, String oldStr, String newStr) {
        return String.format("修改 字段 '%s' 从 '%s' 更新为 '%s'", fieldName, oldStr, newStr);
    }

    /**
     * 根据旧数据的字段获取该字段的中文名
     * @param oldField 旧数据的字段
     * @return 字段中文名
     */
    @Override
    public String getFieldNameFromOldData(Field oldField) {
        //从注解中获取名称，如果注解不存在则使用字段原名
        final Comment comment = oldField.getAnnotation(Comment.class);
        if (comment != null) {
            return comment.value();
        }
        return oldField.getName();
    }

    /**
     * 返回旧数据字段数组中与该新数据中字段匹配的字段
     * @param newField  新数据的字段
     * @param oldFields 旧数据的字段
     * @return 旧数据的字段
     */
    @Override
    public Field getMatchedField(Field newField, Field[] oldFields) {
        //通过字段名匹配
        return Arrays.stream(oldFields).filter(f -> f.getName().equals(newField.getName())).findFirst().orElse(null);
    }

    /**
     * 将字段值转换为可读性更高的文字
     * @param value 新旧数据的字段值
     * @return 可读性更高的文字
     */
    @Override
    public String transformValue(Object value) {
        //原样返回
        return String.valueOf(value);
    }
}
