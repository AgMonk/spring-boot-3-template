package com.gin.springboot3template.sys.base;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

/**
 * 基础附件对象
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/26 10:45
 */
@Getter
@Setter
@MappedSuperclass
@Schema(description = "基础附件对象")
public class BaseAttach extends BasePo {
    @Column(length = 100, nullable = false)
    @Comment("文件原名称")
    String originalFilename;
    @Column
    @TableField(updateStrategy = FieldStrategy.NEVER)
    @Comment("附件所有者Id")
    @Schema(description = "附件所有者Id")
    Long ownerId;
    @Column(length = 300, nullable = false, unique = true)
    @Comment("文件路径")
    String filePath;
    @Column(length = 10, nullable = false)
    @Comment("文件后缀")
    String ext;
    @Column(length = 100, nullable = false)
    @Comment("备注")
    String remark;
}