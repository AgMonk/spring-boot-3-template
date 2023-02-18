package com.gin.springboot3template.operationlog.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gin.springboot3template.operationlog.enums.OperationType;
import com.gin.springboot3template.sys.base.BasePo;
import com.gin.springboot3template.sys.handler.ClassTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

/**
 * 系统操作日志
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/18 11:29
 */
@Getter
@Setter
@TableName(value = SystemOperationLog.TABLE_NAME, autoResultMap = true)
@Entity(name = SystemOperationLog.TABLE_NAME)
@NoArgsConstructor
@Table(indexes = {
        @Index(columnList = "entityClass,entityId,userId"),
})
public class SystemOperationLog extends BasePo {
    public static final String TABLE_NAME = "t_system_entity_operation_log";
    @Comment("操作类型")
    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    OperationType type;
    @Comment("关联实体类")
    @Column(nullable = false, length = 100)
    @TableField(typeHandler = ClassTypeHandler.class)
    Class<?> entityClass;
    @Comment("关联实体ID")
    @Schema(description = "关联实体ID")
    @Column(nullable = false)
    Long entityId;
    @Comment("操作人ID")
    @Schema(description = "操作人ID")
    Long userId;
    @Comment("操作人IP")
    @Schema(description = "操作人IP")
    @Column(length = 40)
    String userIp;
    @Comment("操作描述")
    @Schema(description = "操作描述")
    @Column(length = 10000)
    String description;


}