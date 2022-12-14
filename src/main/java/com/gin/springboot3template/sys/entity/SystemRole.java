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

    public SystemRole(String name) {
        super();
        this.name = name;
    }
}