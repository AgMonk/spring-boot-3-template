package com.gin.springboot3template.sys.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gin.springboot3template.sys.base.BasePo;
import com.gin.springboot3template.sys.base.BaseVo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.springframework.beans.BeanUtils;

/**
 * 用户个人信息
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/17 17:32
 */
@Getter
@Setter
@TableName(value = SystemUserInfo.TABLE_NAME, autoResultMap = true)
@Entity(name = SystemUserInfo.TABLE_NAME)
@NoArgsConstructor
public class SystemUserInfo extends BasePo {
    public static final String TABLE_NAME = "t_system_entity_user_info";
    @Column(nullable = false, unique = true)
    @Comment("用户id")
    Long userId;
    @Column(length = 50, nullable = false)
    @Comment("昵称")
    String nickname;
    @Column
    @Comment("联系电话")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    String phone;
    @Column
    @Comment("生日(UNIX秒)")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    Long birthday;

    @Getter
    @Setter
    @NoArgsConstructor
    @Schema(description = "用户个人信息")
    public static class Vo extends BaseVo {
        @Schema(description = "用户id")
        Long userId;
        @Schema(description = "昵称")
        String nickname;
        @Schema(description = "联系电话")
        String phone;
        @Schema(description = "生日(UNIX秒)")
        Long birthday;

        public Vo(SystemUserInfo systemUserInfo) {
            BeanUtils.copyProperties(systemUserInfo, this);
        }
    }
}