package com.gin.springboot3template.route.annotation;

import com.gin.springboot3template.route.enums.Logic;
import com.gin.springboot3template.route.strategy.AlwaysFalseStrategy;
import com.gin.springboot3template.route.strategy.VisibleStrategy;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 路由, 在类上标注，表示这是一个路由导航项 对应一个 {@link  com.gin.springboot3template.route.entity.EleMenuItem} 组件
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/3/25 17:42
 */
@Schema(description = "路由")
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MenuItem {
    /**
     * 菜单名称
     */
    String menuName() default "index";

    /**
     * 排序序号,将按此字段降序排列
     */
    int order() default 0;

    /**
     * 组件的route属性
     */
    String route() default "";

    /**
     * 组件的title属性
     */
    String title() default "";

    /**
     * 展示路由需要的条件(权限),空表示总是展示, 非空时将交给 {@link  com.gin.springboot3template.route.strategy.VisibleStrategy} 策略判定
     */
    String[] requires() default {};

    /**
     * requires 字段有多项时互相的逻辑关系
     */
    Logic logic() default Logic.or;

    /**
     * 保存路径，决定SubMenu的层级和分组，只有最后一个成员可以是分组
     */
    MenuPath[] path() default {};

    /**
     * 展示策略，决定 disabled字段值的策略，默认为全显示
     */
    Class<? extends VisibleStrategy> strategy() default AlwaysFalseStrategy.class;

}
