package com.gin.springboot3template.sys.init.quest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gin.springboot3template.sys.annotation.MyRestController;
import com.gin.springboot3template.sys.bo.Constant;
import com.gin.springboot3template.sys.controller.SystemPermissionController;
import com.gin.springboot3template.sys.controller.SystemRoleController;
import com.gin.springboot3template.sys.controller.SystemRolePermissionController;
import com.gin.springboot3template.sys.controller.SystemUserRoleController;
import com.gin.springboot3template.sys.dto.form.RegForm;
import com.gin.springboot3template.sys.dto.form.RelationUserRoleForm;
import com.gin.springboot3template.sys.entity.*;
import com.gin.springboot3template.sys.service.*;
import com.gin.springboot3template.sys.utils.SpringContextUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.AnnotatedElement;
import java.util.*;
import java.util.stream.Stream;

/**
 * 初始化 超管用户 角色 权限
 * @author bx002
 */
@Component
@Getter
@Slf4j
@RequiredArgsConstructor
@Order(1)
public class InitAuthority implements ApplicationRunner {
    private final SystemPermissionService systemPermissionService;
    private final RelationRolePermissionService relationRolePermissionService;
    private final SystemRoleService systemRoleService;
    private final RolePermissionService rolePermissionService;
    private final RelationUserRoleService relationUserRoleService;
    private final SystemUserService systemUserService;

    /**
     * 所有权限
     */
    private List<SystemPermission> fullPermission;
    private SystemRole adminRole;
    private SystemRole roleManager;

    /**
     * 返回持有4种注解的元素
     * @param annotatedElement 元素
     * @return 接口方法
     */
    private static List<String> getApiPath(AnnotatedElement annotatedElement) {
        MyRestController a0 = annotatedElement.getAnnotation(MyRestController.class);
        if (a0 != null) {
            return Arrays.asList(a0.value());
        }
        RequestMapping a1 = annotatedElement.getAnnotation(RequestMapping.class);
        if (a1 != null) {
            return Arrays.asList(a1.value());
        }
        PostMapping a2 = annotatedElement.getAnnotation(PostMapping.class);
        if (a2 != null) {
            return Arrays.asList(a2.value());
        }
        GetMapping a3 = annotatedElement.getAnnotation(GetMapping.class);
        if (a3 != null) {
            return Arrays.asList(a3.value());
        }
        return new ArrayList<>();
    }

    private static Class<?> getClass(Object object) throws ClassNotFoundException {
        final String name = object.getClass().getName();
        final String className = name.contains("$") ? name.substring(0, name.indexOf("$")) : name;
        return Class.forName(className);
    }

    /**
     * 任务内容
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("权限初始化");

        initPermissions();

        initRoles();

        initAdminUser();
    }

    /**
     * 初始化超管账号
     */
    private void initAdminUser() {
        SystemUser admin = systemUserService.getByUsername(Constant.User.ADMIN);
        //如果超管账号不存在 则注册
        if (admin == null) {
            final RegForm regForm = new RegForm();
            regForm.setUsername(Constant.User.ADMIN);
            regForm.setPassword(Constant.User.ADMIN_PASSWORD);
            regForm.setNickname(Constant.User.ADMIN_NICKNAME);
            admin = systemUserService.reg(regForm);
        }

        final Long roleId = this.adminRole.getId();
        final RelationUserRoleForm userRole = new RelationUserRoleForm(roleId, 0L);
        final List<RelationUserRole> add = relationUserRoleService.add(admin.getId(), Collections.singleton(userRole));
        if (add.size() > 0) {
            log.info("为超管账号添加超管角色");
        }
    }

    /**
     * 扫描接口,筛选出使用粗粒度校验的接口,将其路径作为权限字符串写入数据库,并记录一些其他信息
     */
    private void initPermissions() {
        // controller 接口
        final Collection<Object> controllers = SpringContextUtils.getContext().getBeansWithAnnotation(RequestMapping.class).values();

        // 扫描类得到的权限数据 新数据 无id
        List<SystemPermission> permissions = controllers.stream().flatMap(controller -> {
            final Class<?> controllerClass;
            try {
                controllerClass = getClass(controller);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return Stream.ofNullable(null);
            }
            //接口路径前缀
            final List<String> prePaths = getApiPath(controllerClass);
            final Tag tag = controllerClass.getAnnotation(Tag.class);

            return Arrays.stream(controllerClass.getDeclaredMethods()).flatMap(method -> {
                final List<String> apiPaths = getApiPath(method);
                final ArrayList<SystemPermission> list = new ArrayList<>();
                final Operation operation = method.getAnnotation(Operation.class);
                final PreAuthorize preAuthorize = method.getAnnotation(PreAuthorize.class);
                if (CollectionUtils.isEmpty(apiPaths) || preAuthorize == null) {
                    return null;
                }
                for (String prePath : prePaths) {
                    for (String apiPath : apiPaths) {
                        final String path = (prePath + "/" + apiPath).replaceAll("//", "/");
                        list.add(new SystemPermission(path, tag, operation, preAuthorize));
                    }
                }
                return list.stream();
            });
        }).toList();

        // 更新数据
        this.fullPermission = systemPermissionService.updateFromController(new ArrayList<>(permissions));

        if (this.fullPermission.size() > 0) {
            log.info("移除角色对已移除权限的持有");
            final QueryWrapper<RelationRolePermission> qw = new QueryWrapper<>();
            qw.notInSql("permission_id", String.format("select id from %s", SystemPermission.TABLE_NAME));
            relationRolePermissionService.remove(qw);
        }
    }

    /**
     * 初始化角色,创建 角色 超管(admin) 角色管理员(roleAdmin)
     */
    private void initRoles() {
        //检查 admin 角色是否存在 不存在则创建 存在则赋值
        final SystemRole adminRole = new SystemRole();
        adminRole.setName(Constant.Role.ADMIN);
        adminRole.setNameZh("超级管理员");
        adminRole.setRemark("预设超级管理员,不允许修改");
        adminRole.setDescription("预设超级管理员,不允许修改");
        this.adminRole = rolePermissionService.initRole(adminRole);

        //检查 角色管理员 角色是否存在 不存在则创建 存在则赋值
        final SystemRole roleManager = new SystemRole();
        roleManager.setName(Constant.Role.ROLE_MANAGER);
        roleManager.setNameZh("角色管理员");
        roleManager.setRemark("预设角色管理员,不允许修改其权限");
        roleManager.setDescription("预设角色管理员,不允许修改其权限");
        this.roleManager = rolePermissionService.initRole(roleManager,
                                                          null,
                                                          List.of(SystemRoleController.GROUP_NAME,
                                                                  SystemRolePermissionController.GROUP_NAME,
                                                                  SystemUserRoleController.GROUP_NAME,
                                                                  SystemPermissionController.GROUP_NAME));


    }
}
