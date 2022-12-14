package com.gin.springboot3template.sys.init.quest;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

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

    /**
     * 任务内容
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        // todo 自动创建权限 根据扫描出的接口信息
        // todo 自动创建 admin 角色
        // todo 自动创建 admin 账号 , 赋予admin角色 , 每次启动赋予随机密码
    }
}
