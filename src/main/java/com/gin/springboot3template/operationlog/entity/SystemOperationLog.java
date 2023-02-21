package com.gin.springboot3template.operationlog.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gin.springboot3template.operationlog.enums.OperationType;
import com.gin.springboot3template.sys.base.BasePo;
import com.gin.springboot3template.sys.handler.ClassTypeHandler;
import com.gin.springboot3template.sys.security.utils.MySecurityUtils;
import com.gin.springboot3template.sys.utils.WebUtils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.jetbrains.annotations.NotNull;

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
        @Index(columnList = "mainClass,mainId,subClass,subId,userId"),
})
public class SystemOperationLog extends BasePo {
    public static final String TABLE_NAME = "t_system_entity_operation_log";
    @Comment("操作类型")
    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    @NotNull
    OperationType type;
    @Comment("操作人ID")
    Long userId;
    @Comment("操作人IP")
    @Column(length = 40)
    String userIp;


    @Comment("主实体类型")
    @Column(nullable = false, length = 100)
    @TableField(typeHandler = ClassTypeHandler.class)
    @NotNull
    Class<?> mainClass;
    @Comment("主实体ID")
    @Column(nullable = false)
    @NotNull
    Long mainId;
    @Comment("副实体类型")
    @Column(length = 100)
    @TableField(typeHandler = ClassTypeHandler.class)
    Class<?> subClass;
    @Comment("使用的策略类")
    @Column(length = 100)
    @TableField(typeHandler = ClassTypeHandler.class)
    Class<?> strategyClass;
    @Comment("副实体ID")
    @Column
    Long subId;
    @Comment("请求参数")
    @Column(length = 2000)
    String requestParam;
    @Comment("返回结果")
    @Column(length = 2000)
    String responseResult;

    @Comment("操作描述")
    @Column(length = 10000)
    String description;

    public SystemOperationLog(@NotNull OperationType type) {
        this.type = type;
        this.userId = MySecurityUtils.currentUserDetails().getId();
        this.userIp = WebUtils.getRemoteHost();
    }
}