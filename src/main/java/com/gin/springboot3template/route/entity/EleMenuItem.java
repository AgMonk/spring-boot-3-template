package com.gin.springboot3template.route.entity;

import com.gin.springboot3template.route.base.EleMenuComponent;
import com.gin.springboot3template.route.enums.MenuComponentType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 对应Element的MenuItem组件
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/3/25 16:03
 */
@Schema(description = "MenuItem组件")
@Getter
@Setter
@NoArgsConstructor
public class EleMenuItem extends EleMenuComponent {
    private final MenuComponentType type = MenuComponentType.menu_item;
    @Schema(description = "组件的index属性")
    String index;
    @Schema(description = "组件的index属性")
    String route;
    @Schema(description = "是否禁用")
    boolean disabled;
}   
