package com.gin.springboot3template.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gin.springboot3template.sys.base.BasePo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;

/**
 * 系统角色
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/14 15:32
 */
@Getter
@Setter
@TableName(value = SystemRole.TABLE_NAME, autoResultMap = true)
@Entity(name = SystemRole.TABLE_NAME)
@NoArgsConstructor
public class SystemRole extends BasePo {
    public static final String TABLE_NAME = "t_system_entity_role";
    @Column(length = 50, nullable = false, unique = true)
    @Comment("名称")
    String name;
    @Column(length = 50, nullable = false, unique = true)
    @Comment("中文名称")
    String nameZh;
    @Column(length = 200)
    @Comment("备注")
    String remark;


    @Getter
    @Setter
    @Schema(name = "参数对象(添加/修改)")
    @Validated
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Param {
        @Schema(description = "名称")
        @NotNull
        String name;
        @Schema(description = "中文名称")
        @NotNull
        String nameZh;
        @Schema(description = "备注")
        String remark;

        public Param(String name, String nameZh) {
            this.name = name;
            this.nameZh = nameZh;
        }

        public SystemRole build() {
            final SystemRole systemRole = new SystemRole();
            BeanUtils.copyProperties(this, systemRole);
            return systemRole;
        }
    }

}