package com.gin.springboot3template.sys.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gin.springboot3template.sys.base.BaseBo;
import com.gin.springboot3template.sys.base.BasePo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.User;

import java.util.List;


/**
 * 系统用户
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/6 14:30
 */
@Getter
@Setter
@TableName(value = SystemUser.TABLE_NAME, autoResultMap = true)
@Entity(name = SystemUser.TABLE_NAME)
public class SystemUser extends BasePo {
    protected static final String TABLE_NAME = "t_system_entity_user";
    @Column(length = 50, nullable = false, unique = true)
    @Comment("用户名")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    String username;
    @Column(nullable = false, length = 100)
    @Comment("密码")
    String password;
    @Column(nullable = false, columnDefinition = "int default 1")
    @Comment("账号未过期")
    Boolean accountNonExpired;
    @Column(nullable = false, columnDefinition = "int default 1")
    @Comment("账号未锁定")
    Boolean accountNonLocked;
    @Column(nullable = false, columnDefinition = "int default 1")
    @Comment("密码未过期")
    Boolean credentialsNonExpired;
    @Column(nullable = false, columnDefinition = "int default 1")
    @Comment("是否可用")
    Boolean enabled;

    public User.UserBuilder createUser() {
        return User.builder()
                .username(username)
                .password(password)
                .accountExpired(!accountNonExpired)
                .accountLocked(!accountNonLocked)
                .credentialsExpired(!credentialsNonExpired)
                .disabled(!enabled);
    }

    @Getter
    @Setter
    @Schema(name = "用户及其持有的角色&权限")
    public static class Bo extends BaseBo {
        @Schema(description = "角色&权限")
        List<RelationUserRole.Bo> roles;
        @Schema(description = "用户名")
        private String username;
        @Schema(description = "账号未过期")
        private boolean accountNonExpired;
        @Schema(description = "账号未锁定")
        private boolean accountNonLocked;
        @Schema(description = "密码未过期")
        private boolean credentialsNonExpired;
        @Schema(description = "是否可用")
        private boolean enabled;

        public Bo(SystemUser systemUser) {
            BeanUtils.copyProperties(systemUser, this);
        }
    }

}
