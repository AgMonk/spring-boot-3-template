package com.gin.springboot3template.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gin.springboot3template.sys.entity.RelationUserRole;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/14 16:58
 */

@Transactional(rollbackFor = Exception.class)
public interface RelationUserRoleService extends MyService<RelationUserRole> {
    /**
     * 为指定用户添加角色
     * @param userId 用户id
     * @param params 参数
     * @return 添加好的角色
     */
    default List<RelationUserRole> add(long userId, Collection<RelationUserRole.Param> params) {
        final List<RelationUserRole> userRoles = params.stream().map(i -> i.build(userId)).toList();
        saveBatch(userRoles);
        return userRoles;
    }

    /**
     * 为指定用户配置角色
     * @param userId 用户id
     * @param params 参数
     */
    default void config(long userId, Collection<RelationUserRole.Param> params) {
        // 查询指定用户持有的角色id
        //已有数据 (含有id)
        final List<RelationUserRole> oldData = listByUserId(Collections.singleton(userId));
        //新数据 (不含id)
        final List<RelationUserRole> newData = new ArrayList<>(params.stream().map(i -> i.build(userId)).toList());

        //过滤出不存在的，进行删除
        final List<RelationUserRole> data2Del = oldData.stream().filter(o -> !newData.contains(o)).toList();
        if (data2Del.size() > 0) {
            removeBatchByIds(data2Del.stream().map(RelationUserRole::getRoleId).collect(Collectors.toList()));
            oldData.removeAll(data2Del);
        }

        //过滤出新增的，进行添加
        final List<RelationUserRole> data2Add = newData.stream().filter(o -> !oldData.contains(o)).toList();
        if (data2Add.size() > 0) {
            saveBatch(data2Add);
            newData.removeAll(data2Add);
        }

        //过滤出已经存在的，进行修改
        if (newData.size() > 0) {
            final long now = System.currentTimeMillis() / 1000;
            final List<RelationUserRole> data2Update = newData.stream().peek(nd -> {
                // 补充id
                nd.setId(oldData.stream().filter(od -> od.equals(nd)).toList().get(0).getId());
                // 设置修改时间
                nd.setTimeUpdate(now);
            }).toList();
            updateBatchById(data2Update);
        }
    }

    /**
     * 为指定用户删除角色
     * @param userId  用户id
     * @param roleIds 角色id
     */
    default void del(long userId, Collection<Long> roleIds) {
        final QueryWrapper<RelationUserRole> qw = new QueryWrapper<>();
        qw.in("role_id", roleIds).eq("user_id", userId);
        remove(qw);
    }

    /**
     * 根据用户id查询
     * @param userId 用户id
     * @return 用户持有的角色
     */
    default List<RelationUserRole> listByUserId(Collection<Long> userId) {
        final QueryWrapper<RelationUserRole> qw = new QueryWrapper<>();
        qw.in("user_id", userId);
        return list(qw);
    }
}