package com.gin.springboot3template.operationlog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gin.springboot3template.operationlog.entity.BaseOperationLog;
import com.gin.springboot3template.sys.service.MyService;
import com.gin.springboot3template.sys.vo.PageOption;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/23 15:01
 */
public interface OperationLogService<T extends BaseOperationLog> extends MyService<T> {
    /**
     * 列出指定主实体类型 (及id) 下的操作类型
     * @param mainClass 主实体类型
     * @param mainId    主实体id
     * @return 操作类型
     */
    default List<PageOption> listTypes(@NotNull Class<?> mainClass, Long mainId) {
        final QueryWrapper<T> qw = new QueryWrapper<>();
        qw.eq("main_class", mainClass.getName());
        if (mainId != null) {
            qw.eq("main_id", mainId);
        }
        final List<T> list = countGroupBy(qw, "type");
        return PageOption.of(list, log -> new PageOption(log.getCount(), log.getType().getName(), log.getType().name()));
    }
}
