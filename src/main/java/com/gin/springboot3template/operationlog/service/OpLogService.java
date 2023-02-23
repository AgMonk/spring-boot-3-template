package com.gin.springboot3template.operationlog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gin.springboot3template.operationlog.bo.OperationLogPageParam;
import com.gin.springboot3template.operationlog.config.OperationLogProperties;
import com.gin.springboot3template.operationlog.entity.BaseOperationLog;
import com.gin.springboot3template.operationlog.entity.SystemOperationLog;
import com.gin.springboot3template.operationlog.entity.SystemOperationLogOld;
import com.gin.springboot3template.operationlog.vo.SystemOperationLogVo;
import com.gin.springboot3template.sys.response.ResPage;
import com.gin.springboot3template.sys.service.SystemUserInfoService;
import com.gin.springboot3template.sys.vo.PageOption;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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
    private final SystemUserInfoService systemUserInfoService;

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

    /**
     * 列出指定主实体类型 及id 下的操作类型
     * @param mainClass 主实体类型
     * @param mainId    主实体id
     * @param old       是否查询旧日志
     * @return 操作类型
     */
    public List<PageOption> listTypes(@NotNull Class<?> mainClass, Long mainId, boolean old) {
        return getService(old).listTypes(mainClass, mainId);
    }

    /**
     * 日志查询方法
     * @param param 分页参数
     * @param old   是否查询旧日志
     * @return 日志
     */
    public ResPage<SystemOperationLogVo> pageByParam(OperationLogPageParam param, boolean old) {
        final ResPage<SystemOperationLogVo> resPage = getService(old).pageByParam(param, new QueryWrapper<>(), SystemOperationLogVo::new);
        final List<SystemOperationLogVo> data = resPage.getData();
        fillNickname(data);
        return resPage;
    }

    /**
     * 填充昵称
     * @param data 数据
     */
    private void fillNickname(List<SystemOperationLogVo> data) {
        final List<Long> userId = data.stream().map(BaseOperationLog::getUserId).filter(Objects::nonNull).distinct().toList();
        final HashMap<Long, String> idNameMap = systemUserInfoService.getIdNameMap(userId);
        data.forEach(i -> i.setUserNickname(idNameMap.get(i.getUserId())));
    }

    private OperationLogService<? extends BaseOperationLog> getService(boolean old) {
        return old ? systemOperationLogOldService : systemOperationLogService;
    }
}
