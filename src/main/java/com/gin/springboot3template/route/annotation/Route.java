package com.gin.springboot3template.route.annotation;

import com.gin.springboot3template.route.enums.Logic;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 路由, 在类上标注，表示这是一个路由 对应一个 {@link  com.gin.springboot3template.route.entity.EleMenuItem} 组件
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/3/25 17:42
 */
@Schema(description = "路由")
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Route {
    @Schema(description = "组件的index属性")
    String route();

    @Schema(description = "展示路由需要的权限,空表示总是展示")
    String[] requires() default {};

    @Schema(description = "requires 字段有多项时互相的逻辑关系")
    Logic logic() default Logic.or;
}
