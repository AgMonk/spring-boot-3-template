package com.gin.springboot3template.sys.docket;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author bx002
 */
@Configuration
@EnableKnife4j
public class Knife4jConfiguration {

    @Bean(value = "verifyCode")
    public Docket verifyCode() {
        return DocketUtils.createApi("验证码","/sys/verifyCode/.+");
    }

    @Bean(value = "test")
    public Docket test() {
        return DocketUtils.createApi("测试","/test/.+");
    }


}
