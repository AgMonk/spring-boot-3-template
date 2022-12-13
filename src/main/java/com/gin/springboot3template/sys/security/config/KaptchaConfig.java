package com.gin.springboot3template.sys.security.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * 验证码配置
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/7 09:45
 */
@Configuration
public class KaptchaConfig {
    @Bean
    public Producer kaptcha() {
        final Properties properties = new Properties();
        //高度
        properties.setProperty("kaptcha.image.width", "150");
        //宽度
        properties.setProperty("kaptcha.image.height", "50");
        //可选字符串
        properties.setProperty("kaptcha.textproducer.char.string", "0123456789");
        //验证码长度
        properties.setProperty("kaptcha.textproducer.char.length", "4");

        final DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(new Config(properties));
        return defaultKaptcha;
    }
}
