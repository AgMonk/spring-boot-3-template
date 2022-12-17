package com.gin.springboot3template.sys.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gin.springboot3template.sys.base.BasePo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Objects;
import java.util.regex.Matcher;

import static com.gin.springboot3template.sys.bo.Constant.Evaluator.*;

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
@Schema(name = "接口权限")
public class SystemPermission extends BasePo {
    protected static final String TABLE_NAME = "t_system_entity_permission";
    @Column(length = 50, nullable = false, unique = true)
    @Comment("路径")
    @Schema(description = "路径")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    String path;
    @Column(length = 50)
    @Comment("分组")
    @Schema(description = "分组")
    String groupName;
    @Column(length = 50)
    @Comment("摘要")
    @Schema(description = "摘要")
    String summary;
    @Column(length = 100)
    @Comment("描述")
    @Schema(description = "描述")
    String description;

    @Column(length = 100)
    @Comment("权限检查")
    @Schema(description = "权限检查")
    String preAuthorize;

    @Column(length = 100)
    @Comment("权限报错提示")
    @Schema(description = "权限报错提示")
    String note;

    public SystemPermission(String path, Tag tag, Operation operation, PreAuthorize preAuthorize) {
        super();
        final String authorize = preAuthorize.value();

        this.path = path;
        this.preAuthorize = authorize;
        this.note = parsePreAuthorize(authorize);


        if (operation != null) {
            this.summary = "".equals(operation.summary()) ? null : operation.summary();
            this.description = "".equals(operation.description()) ? null : operation.description();
        }
        if (tag != null) {
            this.groupName = "".equals(tag.name()) ? null : tag.name();
        }
    }

    private static String clearQuote(String s) {
        final Matcher matcher = STRING_PATTERN.matcher(s);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SystemPermission that = (SystemPermission) o;
        return getPath().equals(that.getPath());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPath());
    }

    /**
     * 尝试解析 SpeL表达式 提供报错提示
     * @param preAuthorize SpeL表达式
     * @return 报错提示
     */
    private String parsePreAuthorize(String preAuthorize) {
        final Matcher matcher = HAS_PERMISSION_TYPE_PATTERN.matcher(preAuthorize);
        if (!matcher.find()) {
            return null;
        }
        final String targetId = clearQuote(matcher.group(1));
        final String targetType = clearQuote(matcher.group(2));
        final String permission = clearQuote(matcher.group(3));

        if (TYPE_PATH.equals(targetType)) {
            return "需要这个权限: " + this.path;
        }
        if (TYPE_ROLE.equals(targetType)) {
            return "需要这个角色: " + targetId;
        }

        return null;
    }
}