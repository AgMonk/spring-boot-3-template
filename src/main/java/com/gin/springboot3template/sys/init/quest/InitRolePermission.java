package com.gin.springboot3template.sys.init.quest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gin.springboot3template.sys.annotation.MyRestController;
import com.gin.springboot3template.sys.base.BasePo;
import com.gin.springboot3template.sys.entity.RelationRolePermission;
import com.gin.springboot3template.sys.entity.SystemPermission;
import com.gin.springboot3template.sys.service.RelationRolePermissionService;
import com.gin.springboot3template.sys.service.SystemPermissionService;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * 初始化任务模板
 * @author bx002
 */
@Component
@Getter
@Slf4j
@RequiredArgsConstructor
@Order(1)
public class InitRolePermission implements ApplicationRunner {
    public static final String HAS_PERMISSION = "hasPermission";
    private final SystemPermissionService systemPermissionService;
    private final RelationRolePermissionService relationRolePermissionService;

    /**
     * 所有权限
     */
    private List<SystemPermission> fullPermission;

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


        // todo 自动创建 admin 角色
        // todo 自动创建 admin 账号 , 赋予admin角色 , 每次启动赋予随机密码
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
                if (CollectionUtils.isEmpty(apiPaths) || preAuthorize == null || preAuthorize.value().contains(HAS_PERMISSION)) {
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
            qw.in("permission_id", this.fullPermission.stream().map(BasePo::getId).toList());
            relationRolePermissionService.remove(qw);
        }
    }
}
