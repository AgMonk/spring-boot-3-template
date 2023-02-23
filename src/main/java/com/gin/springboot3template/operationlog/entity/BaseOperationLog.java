package com.gin.springboot3template.operationlog.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.gin.springboot3template.operationlog.enums.OperationType;
import com.gin.springboot3template.sys.base.BasePo;
import com.gin.springboot3template.sys.handler.ClassTypeHandler;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.jetbrains.annotations.NotNull;

/**
 * 操作日志字段
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/22 15:07
 */
@Getter
@Setter
@MappedSuperclass
public class BaseOperationLog extends BasePo {
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
    Class<?> mainClass;
    @Comment("主实体ID")
    @Column(nullable = false)
    Long mainId;
    @Comment("副实体类型")
    @Column(length = 100)
    @TableField(typeHandler = ClassTypeHandler.class)
    Class<?> subClass;
    @Comment("副实体ID")
    @Column
    Long subId;
    @Comment("使用的策略类")
    @Column(length = 100)
    @TableField(typeHandler = ClassTypeHandler.class)
    Class<?> strategyClass;
    @Comment("请求参数")
    @Column(length = 2000)
    String requestParam;
    @Comment("返回结果")
    @Column(length = 2000)
    String responseResult;

    @Comment("操作描述")
    @Column(length = 10000)
    String description;

    @Comment("会话ID")
    @Column(nullable = false, length = 36)
    String sessionId;

    @Comment("执行耗时(ms)")
    @Column(nullable = false)
    Long timeCost;
}   
