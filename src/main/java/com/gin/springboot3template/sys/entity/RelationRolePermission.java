package com.gin.springboot3template.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gin.springboot3template.sys.base.BasePo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

/**
 * 角色持有的权限
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/14 16:06
 */
@Getter
@Setter
@TableName(value = RelationRolePermission.TABLE_NAME, autoResultMap = true)
@Entity(name = RelationRolePermission.TABLE_NAME)
@NoArgsConstructor
public class RelationRolePermission extends BasePo {
    public static final String TABLE_NAME = "t_system_relation_role_permission";
    @Column(nullable = false)
    @Comment("角色id")
    Long roleId;

    @Column(nullable = false)
    @Comment("权限id")
    Long permissionId;

}