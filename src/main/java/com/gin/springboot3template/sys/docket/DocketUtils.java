package com.gin.springboot3template.sys.docket;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * 文档工具类
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/10 12:10
 */
public class DocketUtils {
    public static Docket createApi(String title, String pathRegex) {
        return createApi(title,
                pathRegex,
                "com.gin",
                "接口文档描述",
                "https//www.baidu.com",
                new Contact("黄俊钢", "https://www.baidu.com", "hjg719@139.com"),
                "1.0");
    }

    public static Docket createApi(String title,
                                   String pathRegex,
                                   String basePackage,
                                   String description,
                                   String termsOfServiceUrl,
                                   Contact contact,
                                   String version) {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title(title + "接口")
                        .description(description)
                        .termsOfServiceUrl(termsOfServiceUrl)
                        .contact(contact)
                        .version(version)
                        .build())
                //分组名称
                .groupName(title + "接口")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                .paths(PathSelectors.regex(pathRegex))
                .build();
    }
}   
