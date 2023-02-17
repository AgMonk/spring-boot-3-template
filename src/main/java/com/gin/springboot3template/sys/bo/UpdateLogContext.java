package com.gin.springboot3template.sys.bo;

import com.gin.springboot3template.sys.base.BaseUpdateLog;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Field;

/**
 * 更新操作日志上下文
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/17 15:36
 */
@Getter
@RequiredArgsConstructor
public abstract class UpdateLogContext<T extends BaseUpdateLog, N, O> {
    /**
     * 用户提供的请求表单（新数据）
     */
    final N newData;
    /**
     * 从数据库查询得到的需要操作的实体对象（旧数据）
     */
    final O oldData;
    /**
     * 日志对象，应当已填写好必须字段
     */
    final T updateLog;

    /**
     * 根据旧数据的字段获取该字段的中文名
     * @param oldField 旧数据的字段
     * @return 字段中文名
     */
    abstract public String getFieldNameFromOldData(Field oldField);

    /**
     * 返回旧数据字段数组中与该新数据中字段匹配的字段
     * @param newField  新数据的字段
     * @param oldFields 旧数据的字段
     * @return 旧数据的字段
     */
    abstract public Field getMatchedField(Field newField, Field[] oldFields);

    /**
     * 将字段值转换为可读性更高的文字
     * @param value 新旧数据的字段值
     * @return 可读性更高的文字
     */
    abstract public String transformValue(Object value);

    /**
     * 生成操作描述
     * @param fieldName 被操作字段中文名
     * @param oldStr    修改前的值
     * @param newStr    修改后的值
     * @return 操作描述
     */
    abstract public String generateDescription(String fieldName, String oldStr, String newStr);
}   
