package com.gin.springboot3template.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
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
    Long userId;
    @Column(nullable = false)
    @Comment("角色id")
    Long roleId;

    @Column(nullable = false)
    @Comment("过期时间(UNIX秒)")
    Long timeExpire;

    @Getter
    @Setter
    @Schema(name = "参数对象(添加)")
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
}