package com.gin.springboot3template.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gin.springboot3template.sys.base.BasePo;
import com.gin.springboot3template.sys.dto.SystemRoleForm;
import com.gin.springboot3template.sys.entity.SystemRole;
import com.gin.springboot3template.sys.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.gin.springboot3template.sys.bo.Constant.MESSAGE_FORBIDDEN_CONFIG_ADMIN;
import static com.gin.springboot3template.sys.bo.Constant.ROLE_ADMIN;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/14 16:57
 */

@Transactional(rollbackFor = Exception.class)
public interface SystemRoleService extends MyService<SystemRole> {
    /**
     * 添加角色
     * @param param 参数
     * @return 添加好的角色
     */
    default List<SystemRole> addByParam(Collection<SystemRoleForm> param) {
        final List<SystemRole> roles = param.stream().map(SystemRoleForm::build).toList();
        saveBatch(roles);
        return roles;
    }

    /**
     * 不能分配/取消分配 admin 角色
     * @param roleId 角色id
     */
    default void forbiddenConfigAdminRole(Collection<Long> roleId) {
        final SystemRole systemRole = getByName(ROLE_ADMIN);
        if (roleId.contains(systemRole.getId())) {
            throw BusinessException.of(HttpStatus.FORBIDDEN, MESSAGE_FORBIDDEN_CONFIG_ADMIN);
        }
    }

    /**
     * 通过名称查询角色
     * @param name 名称
     * @return 角色
     */
    default SystemRole getByName(String name) {
        final QueryWrapper<SystemRole> qw = new QueryWrapper<>();
        qw.eq("name", name);
        return getOne(qw);
    }

    /**
     * 通过名称查询角色
     * @param name 名称
     * @return 角色
     */
    default List<SystemRole> listByName(Collection<String> name) {
        final QueryWrapper<SystemRole> qw = new QueryWrapper<>();
        qw.in("name", name);
        return list(qw);
    }

    /**
     * 修改角色
     * @param roleId 角色id
     * @param param  参数
     * @return 修改后的角色
     */
    default SystemRole updateByIdParam(long roleId, SystemRoleForm param) {
        final SystemRole entity = param.build();
        entity.setId(roleId);
        entity.setTimeUpdate(System.currentTimeMillis() / 1000);
        updateById(entity);
        return entity;
    }

    /**
     * 校验角色ID存在
     * @param roleId 角色ID
     */
    default void validateRoleId(Collection<Long> roleId) {
        final List<Long> idExists = listByIds(roleId).stream().map(BasePo::getId).toList();
        final List<Long> idNotExists = roleId.stream().filter(i -> !idExists.contains(i)).toList();
        if (idNotExists.size() > 0) {
            throw BusinessException.of(HttpStatus.BAD_REQUEST, "参数错误,如下角色ID不存在", idNotExists.stream().map(String::valueOf).collect(Collectors.toList()));
        }
    }
}