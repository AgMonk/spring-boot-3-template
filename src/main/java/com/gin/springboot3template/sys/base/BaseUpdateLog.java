package com.gin.springboot3template.sys.base;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/17 13:45
 */
@Getter
@Setter
@MappedSuperclass
@Schema(description = "基础更新操作日志对象")
@Table(indexes = {
        @Index(columnList = "entityId,userId"),
})
public class BaseUpdateLog {
    @TableId(type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Comment("ID")
    @Schema(description = "ID")
    Long id;
    @Comment("关联实体ID")
    @Schema(description = "关联实体ID")
    @Column(nullable = false)
    Long entityId;
    @Comment("操作人ID")
    @Schema(description = "操作人ID")
    @Column(nullable = false)
    Long userId;
    @Comment("操作人IP")
    @Schema(description = "操作人IP")
    @Column(length = 40)
    String userIp;
    @Comment("操作描述")
    @Schema(description = "操作描述")
    @Column(length = 10000)
    String description;
    @Column(nullable = false)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    @Comment("记录创建时间(UNIX毫秒)")
    @Schema(description = "记录创建时间(UNIX毫秒)")
    Long timeCreate;

    public BaseUpdateLog(Long entityId, Long userId) {
        this.timeCreate = System.currentTimeMillis();
        this.userId = userId;
        this.entityId = entityId;
    }

    public BaseUpdateLog(Long entityId, Long userId, String userIp) {
        this.entityId = entityId;
        this.userId = userId;
        this.userIp = userIp;
    }
}
