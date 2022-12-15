package com.gin.springboot3template.sys.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gin.springboot3template.sys.base.BaseBo;
import com.gin.springboot3template.sys.base.BasePo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Objects;

/**
 * 用户持有的角色
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/14 16:05
 */
@Getter
@Setter
@TableName(value = RelationUserRole.TABLE_NAME, autoResultMap = true)
@Entity(name = RelationUserRole.TABLE_NAME)
@NoArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "unique_user_role", columnNames = {"userId", "roleId"}),
})
public class RelationUserRole extends BasePo {
    public static final String TABLE_NAME = "t_system_relation_user_role";
    @Column(nullable = false)
    @Comment("用户id")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    Long userId;
    @Column(nullable = false)
    @Comment("角色id")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    Long roleId;

    @Column
    @Comment("修改时间(UNIX秒)")
    Long timeUpdate;

    @Column(nullable = false)
    @Comment("过期时间(UNIX秒)")
    Long timeExpire;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RelationUserRole that = (RelationUserRole) o;
        return getUserId().equals(that.getUserId()) && getRoleId().equals(that.getRoleId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getRoleId());
    }

    @Getter
    @Setter
    @Schema(name = "参数对象(添加/修改)")
    @Validated
    public static class Param {
        @Schema(description = "角色id")
        @NotNull
        Long roleId;

        @Schema(description = "过期时间(UNIX秒) 不小于0 0表示不过期")
        @Min(0L)
        Long timeExpire;

        public RelationUserRole build(long userId) {
            final RelationUserRole userRole = new RelationUserRole();
            BeanUtils.copyProperties(this, userRole);
            userRole.setUserId(userId);
            return userRole;
        }
    }

    @Getter
    @Setter
    @Schema(name = "角色及其持有的权限")
    public static class Bo extends BaseBo {
        @Schema(description = "用户id")
        Long userId;
        @Schema(description = "角色id")
        Long roleId;
        @Schema(description = "修改时间(UNIX秒)")
        Long timeUpdate;
        @Schema(description = "过期时间(UNIX秒)")
        Long timeExpire;


        @Schema(description = "名称")
        String name;
        @Schema(description = "中文名称")
        String nameZh;
        @Schema(description = "备注")
        String remark;
        @Schema(description = "权限")
        List<SystemPermission> permissions;

        public Bo(RelationUserRole userRole) {
            BeanUtils.copyProperties(userRole, this);
        }

        /**
         * 补充三个字段信息
         * @param systemRole 角色
         */
        public void with(SystemRole systemRole) {
            if (systemRole == null) {
                return;
            }
            this.name = systemRole.getName();
            this.nameZh = systemRole.getNameZh();
            this.remark = systemRole.getRemark();
        }
    }
}