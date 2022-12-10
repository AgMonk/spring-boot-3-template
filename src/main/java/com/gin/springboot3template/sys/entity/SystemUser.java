package com.gin.springboot3template.sys.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gin.springboot3template.sys.base.BasePo;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.TableComment;
import com.gitee.sunchenbin.mybatis.actable.annotation.Unique;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统用户
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/6 14:30
 */
@Getter
@Setter
@TableName(value = "t_system_entity_user", autoResultMap = true)
@TableComment("系统用户")
public class SystemUser extends BasePo {
    @Column(comment = "用户名", isNull = false, length = 50)
    @Unique
    @TableField(updateStrategy = FieldStrategy.NEVER)
    String username;
    @Column(comment = "密码", isNull = false, length = 100)
    String password;
    @Column(comment = "账号未过期", isNull = false, type = MySqlTypeConstant.TINYINT, defaultValue = "1")
    Boolean accountNonExpired;
    @Column(comment = "账号未锁定", isNull = false, type = MySqlTypeConstant.TINYINT, defaultValue = "1")
    Boolean accountNonLocked;
    @Column(comment = "密码未过期", isNull = false, type = MySqlTypeConstant.TINYINT, defaultValue = "1")
    Boolean credentialsNonExpired;
    @Column(comment = "是否可用", isNull = false, type = MySqlTypeConstant.TINYINT, defaultValue = "1")
    Boolean enabled;

}
