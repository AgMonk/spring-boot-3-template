package com.gin.springboot3template.sys.service;

import com.gin.springboot3template.sys.base.BasePo;
import com.gin.springboot3template.sys.entity.SystemPermission;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/14 16:58
 */

@Transactional(rollbackFor = Exception.class)
public interface SystemPermissionService extends MyService<SystemPermission> {
    org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SystemPermissionService.class);

    /**
     * 根据包扫描结果更新权限数据
     * @param newData 新数据
     * @return 新的完整数据
     */
    default List<SystemPermission> updateFromController(List<SystemPermission> newData) {
        //已有数据 (含有id)
        final List<SystemPermission> oldData = list();
        final List<SystemPermission> returnData = new ArrayList<>();

        //过滤出不存在的，进行删除
        final List<SystemPermission> data2Del = oldData.stream().filter(o -> !newData.contains(o)).toList();
        if (data2Del.size() > 0) {
            removeBatchByIds(data2Del.stream().map(BasePo::getId).collect(Collectors.toList()));
            oldData.removeAll(data2Del);
            for (SystemPermission perm : data2Del) {
                log.info("移除权限: {}", perm.getPath());
            }
        }

        //过滤出新增的，进行添加
        final List<SystemPermission> data2Add = newData.stream().filter(o -> !oldData.contains(o)).toList();
        if (data2Add.size() > 0) {
            saveBatch(data2Add);
            returnData.addAll(data2Add);
            newData.removeAll(data2Add);
            for (SystemPermission perm : data2Add) {
                log.info("添加权限: {}", perm.getPath());
            }
        }

        //过滤出已经存在的，进行修改
        if (newData.size() > 0) {
            final long now = System.currentTimeMillis() / 1000;
            final List<SystemPermission> data2Update = newData.stream()
                    .peek(nd -> nd.setId(oldData.stream().filter(od -> od.equals(nd)).toList().get(0).getId()))
                    .toList();
            updateBatchById(data2Update);
            returnData.addAll(data2Update);
            for (SystemPermission perm : data2Update) {
                log.info("更新权限: {}", perm.getPath());
            }
        }

        returnData.sort((o1, o2) -> Math.toIntExact(o1.getId() - o2.getId()));
        return returnData;

    }

}