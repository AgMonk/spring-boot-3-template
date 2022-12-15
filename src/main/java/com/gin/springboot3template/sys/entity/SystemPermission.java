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
 * 系统权限
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/14 15:33
 */
@Getter
@Setter
@TableName(value = SystemPermission.TABLE_NAME, autoResultMap = true)
@Entity(name = SystemPermission.TABLE_NAME)
@NoArgsConstructor
public class SystemPermission extends BasePo {
    public static final String TABLE_NAME = "t_system_entity_permission";
    @Column(length = 50, nullable = false, unique = true)
    @Comment("路径")
    String path;
    @Column(length = 100)
    @Comment("摘要")
    String summary;
    @Column(length = 100)
    @Comment("描述")
    String description;

    public SystemPermission(String path) {
        super();
        this.path = path;
    }
}