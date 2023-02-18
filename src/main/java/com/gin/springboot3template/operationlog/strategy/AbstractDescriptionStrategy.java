package com.gin.springboot3template.operationlog.strategy;

import com.gin.springboot3template.operationlog.bo.DescriptionContext;
import com.gin.springboot3template.operationlog.entity.SystemOperationLog;
import com.gin.springboot3template.operationlog.service.SystemOperationLogService;
import com.gin.springboot3template.sys.response.Res;
import com.gin.springboot3template.sys.utils.ReflectUtils;
import com.gin.springboot3template.sys.utils.TimeUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述生成策略的默认实现
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/18 16:38
 */
@RequiredArgsConstructor
public abstract class AbstractDescriptionStrategy implements DescriptionStrategy {
    private final SystemOperationLogService logService;

    /**
     * 添加策略
     * @param pjp     接触点
     * @param context 上下文
     * @return 返回
     * @throws Throwable 异常
     */
    @Override
    public Object add(ProceedingJoinPoint pjp, DescriptionContext context) throws Throwable {
        final Object result = pjp.proceed();

        if (result instanceof Res<?> res) {
            final SystemOperationLog log = context.generateLog();

            final Object data = res.getData();
            List<String> fieldsDes = new ArrayList<>();
            // 将所有字段组装为描述
            final Field[] fields = data.getClass().getDeclaredFields();
            for (Field field : fields) {
                //获取注释
                final Schema schema = field.getAnnotation(Schema.class);
                // 字段标题
                final String label = schema != null ? schema.description() : field.getName();
                if (field.getType().equals(Long.class) && field.getName().contains("time")) {
                    //如果字段为Long类型，且名称中含有 time 则认为是日期时间字段
                    fieldsDes.add(String.format("%s: %s", label, TimeUtils.format(ReflectUtils.getLong(field, data))));
                } else {
                    // 将id 字段写入日志的 entityId 字段
                    if ("id".equals(field.getName())) {
                        log.setEntityId(ReflectUtils.getLong(field, data));
                    }
                    // 如果值非空 ，写入描述
                    final String value = ReflectUtils.getString(field, data);
                    if (!ObjectUtils.isEmpty(value)) {
                        fieldsDes.add(String.format("%s: %s", label, value));
                    }
                }
            }
            log.setEntityClass(clazz());
            log.setDescription(String.join(", ", fieldsDes));

            logService.write(log);
        }

        return result;
    }

    /**
     * 删除策略
     * @param pjp     接触点
     * @param context 上下文
     * @return 返回
     * @throws Throwable 异常
     */
    @Override
    public Object del(ProceedingJoinPoint pjp, DescriptionContext context) throws Throwable {
        return pjp.proceed();
    }

    /**
     * 查询策略
     * @param pjp     接触点
     * @param context 上下文
     * @return 返回
     * @throws Throwable 异常
     */
    @Override
    public Object query(ProceedingJoinPoint pjp, DescriptionContext context) throws Throwable {
        return pjp.proceed();
    }

    /**
     * 修改策略
     * @param pjp     接触点
     * @param context 上下文
     * @return 返回
     * @throws Throwable 异常
     */
    @Override
    public Object update(ProceedingJoinPoint pjp, DescriptionContext context) throws Throwable {
        return pjp.proceed();
    }
}
