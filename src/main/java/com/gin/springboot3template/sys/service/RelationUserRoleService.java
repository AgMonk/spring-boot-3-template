package com.gin.springboot3template.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gin.springboot3template.sys.entity.RelationUserRole;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/14 16:58
 */

@Transactional(rollbackFor = Exception.class)
public interface RelationUserRoleService extends MyService<RelationUserRole> {
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