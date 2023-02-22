package com.gin.springboot3template.operationlog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gin.springboot3template.operationlog.config.OperationLogProperties;
import com.gin.springboot3template.operationlog.entity.SystemOperationLog;
import com.gin.springboot3template.operationlog.entity.SystemOperationLogOld;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * 操作日志服务
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/22 15:19
 */
@Service
@RequiredArgsConstructor
public class OpLogService {
    private final SystemOperationLogService systemOperationLogService;
    private final SystemOperationLogOldService systemOperationLogOldService;
    private final OperationLogProperties operationLogProperties;

    /**
     * 每天凌晨5点进行归档, 将创建时间超过一定天数的日志归档到旧表
     */
    @Scheduled(cron = "0 0 5 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void archive() {
        final long maxTime = ZonedDateTime.now()
                .withHour(0).withMinute(0).withSecond(0)
                .minusDays(operationLogProperties.getDays())
                .toEpochSecond();
        final QueryWrapper<SystemOperationLog> qw = new QueryWrapper<>();
        qw.lt("time_create", maxTime);
        final List<SystemOperationLog> oldLogs = systemOperationLogService.list(qw);
        if (oldLogs.size() > 0) {
            final List<SystemOperationLogOld> oldList = oldLogs.stream().map(SystemOperationLogOld::new).toList();

            systemOperationLogOldService.saveBatch(oldList);
            systemOperationLogService.removeBatchByIds(oldLogs);
        }
    }

    /**
     * 定时删除超时的旧日志
     */
    @Scheduled(cron = "0 30 5 * * ?")
    public void clearOld() {
        final long maxTime = ZonedDateTime.now()
                .withHour(0).withMinute(0).withSecond(0)
                .minusDays(operationLogProperties.getDaysOld())
                .toEpochSecond();
        final QueryWrapper<SystemOperationLogOld> qw = new QueryWrapper<>();
        qw.lt("time_create", maxTime);
        systemOperationLogOldService.remove(qw);
    }

    //todo 日志查询方法
}
