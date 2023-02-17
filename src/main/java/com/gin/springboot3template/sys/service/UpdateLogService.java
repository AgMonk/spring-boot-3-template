package com.gin.springboot3template.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gin.springboot3template.sys.base.BaseUpdateLog;
import com.gin.springboot3template.sys.bo.UpdateLogContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/17 15:16
 */
public interface UpdateLogService<T extends BaseUpdateLog> extends IService<T> {
    /**
     * 记录更新操作日志
     * @param context 参数上下文
     * @return 生成的描述
     */
    @Async
    default <N, O> void log(UpdateLogContext<T, N, O> context) {
        final ArrayList<String> descriptions = new ArrayList<>();

        final N newData = context.getNewData();
        final O oldData = context.getOldData();

        final Field[] newFields = newData.getClass().getDeclaredFields();
        final Field[] oldFields = oldData.getClass().getDeclaredFields();

        // 遍历新数据的字段
        for (Field newField : newFields) {
            //匹配的旧数据字段
            final Field oldField = context.getMatchedField(newField, oldFields);
            if (oldField != null) {
                try {
                    //获取两个对应的字段值
                    newField.setAccessible(true);
                    oldField.setAccessible(true);
                    final Object newValue = newField.get(newData);
                    final Object oldValue = oldField.get(oldData);
                    newField.setAccessible(false);
                    oldField.setAccessible(false);
                    if (!ObjectUtils.nullSafeEquals(newValue, oldValue)) {
                        //如果字段值不相同 生成操作描述
                        final String fieldName = context.getFieldNameFromOldData(oldField);
                        final String newStr = context.transformValue(newValue);
                        final String oldStr = context.transformValue(oldValue);
                        descriptions.add(context.generateDescription(fieldName, oldStr, newStr));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        // 循环完毕，写入描述
        final T updateLog = context.getUpdateLog();
        updateLog.setDescription(String.join(" | ", descriptions));
        save(updateLog);
    }


}
