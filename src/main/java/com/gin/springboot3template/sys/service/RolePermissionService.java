package com.gin.springboot3template.sys.service;

import com.gin.springboot3template.sys.bo.RolePermission;
import com.gin.springboot3template.sys.bo.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * 角色和权限统一服务
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/14 17:10
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RolePermissionService {
    // todo 添加、修改、删除角色

    private final SystemRoleService systemRoleService;
    private final SystemPermissionService systemPermissionService;
    private final RelationRolePermissionService relationRolePermissionService;
    private final RelationUserRoleService relationUserRoleService;

    /**
     * 为指定角色添加权限
     * @param roleId  角色id
     * @param permIds 权限id
     */
    public void addPermissionsByRoleId(long roleId, Collection<Long> permIds) {
        //todo
    }

    /**
     * 为指定用户添加角色
     * @param userId  用户id
     * @param roleIds 角色id
     */
    public void addRoleByUserId(long userId, Collection<Long> roleIds) {
        //todo

    }

    /**
     * 为指定角色删除权限
     * @param roleId  角色id
     * @param permIds 权限id
     */
    public void delPermissionsByRoleId(long roleId, Collection<Long> permIds) {
        //todo
    }

    /**
     * 为指定用户删除角色
     * @param userId  用户id
     * @param roleIds 角色id
     */
    public void delRoleByUserId(long userId, Collection<Long> roleIds) {
        //todo

    }

    /**
     * 查询指定用户的角色及其权限
     * @param userId 用户id
     * @return 角色及其权限
     */
    public List<RolePermission> listAuthorityByUserId(long userId) {
        //todo
        return null;
    }

    /**
     * 查询指定用户的角色及其权限
     * @param userId 用户id
     * @return 角色及其权限
     */
    public List<UserRole> listAuthorityByUserId(Collection<Long> userId) {
        //todo
        return null;
    }

    /**
     * 查询指定角色的权限
     * @param roleIds 角色id
     * @return 权限
     */
    public List<RolePermission> listPermissionsByRoleId(Collection<Long> roleIds) {
        //todo
        return null;
    }

    /**
     * 查询指定用户的角色
     * @param userId 用户id
     * @return 角色
     */
    public List<RolePermission> listRolesByUserId(long userId) {
        //todo
        return null;
    }

    /**
     * 查询指定用户的角色
     * @param userId 用户id
     * @return 角色
     */
    public List<UserRole> listRolesByUserId(Collection<Long> userId) {
        //todo
        return null;
    }


}
