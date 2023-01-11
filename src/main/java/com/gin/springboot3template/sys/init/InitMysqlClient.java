package com.gin.springboot3template.sys.init;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 安装mysql client
 * @author bx002
 */
@Component
@Getter
@Slf4j
@RequiredArgsConstructor
@Order(50)
public class InitMysqlClient implements ApplicationRunner {
    /**
     * 任务内容
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
    }
}
