package com.gin.springboot3template.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gin.springboot3template.sys.base.BasePo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

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
}