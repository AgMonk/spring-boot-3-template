package com.gin.springboot3template.sys.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;
import java.util.List;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/1/11 17:03
 */
@Configuration
@ConfigurationProperties(prefix = "system.database")
@Getter
@Setter
public class DatabaseProperties implements Serializable {
    List<String> mysqlClient;

}   
