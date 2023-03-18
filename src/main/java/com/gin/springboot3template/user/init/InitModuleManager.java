package com.gin.springboot3template.user.init;

import com.gin.springboot3template.user.entity.SystemRole;
import com.gin.springboot3template.user.service.RolePermissionService;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.Collection;

/**
 * 初始化单个模块管理员 @Order 应当在10-50之间
 * @author bx002
 */
@Getter
@Slf4j
@RequiredArgsConstructor
public abstract class InitModuleManager implements ApplicationRunner {
    private final RolePermissionService rolePermissionService;

    /**
     * 接口分组名称
     * @return 接口分组名称
     */
    @Nullable
    public abstract Collection<String> groupName();

    /**
     * 接口路径
     * @return 接口路径
     */
    @Nullable
    public abstract Collection<String> path();

    /**
     * 角色信息
     * @return 角色信息
     */
    @NotNull
    public abstract SystemRole systemRole();

    /**
     * 任务内容
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        final SystemRole role = systemRole();
        log.info("初始化模块管理员: " + role.getNameZh());
        rolePermissionService.initRole(role, path(), groupName());
    }
}
