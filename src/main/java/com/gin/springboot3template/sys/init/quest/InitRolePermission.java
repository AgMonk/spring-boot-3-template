package com.gin.springboot3template.sys.init.quest;

import com.gin.springboot3template.sys.utils.SpringContextUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    private static List<String> getApiPath(AnnotatedElement annotatedElement) {
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

    /**
     * 任务内容
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        initPermissions();


        // todo 自动创建 admin 角色
        // todo 自动创建 admin 账号 , 赋予admin角色 , 每次启动赋予随机密码
    }

    /**
     * 根据扫描出的接口信息 自动创建权限
     */
    private void initPermissions() {
        final Map<String, Object> controllers = SpringContextUtils.getContext().getBeansWithAnnotation(RequestMapping.class);
        controllers.forEach((name, controller) -> {
//            try {
            final Class<?> aClass = controller.getClass();
//                final Class<?> aClass = Class.forName(controller.getClass().getCanonicalName());
            final List<Method> methods = Arrays.stream(aClass.getDeclaredMethods()).filter(method -> {
                final List<String> apiPath = getApiPath(method);

                return !CollectionUtils.isEmpty(apiPath);
            }).toList();
            log.info("{}", methods.stream().map(Method::getName).collect(Collectors.toList()));
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }
        });
    }
}
