package com.gin.springboot3template.sys.dto.param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gin.springboot3template.sys.base.BasePageParam;
import com.gin.springboot3template.sys.entity.SystemUserInfo;
import com.gin.springboot3template.sys.exception.BusinessException;
import com.gin.springboot3template.sys.service.RelationUserRoleService;
import com.gin.springboot3template.sys.service.SystemUserInfoService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;

/**
 * 用户分页查询参数
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/19 16:29
 */
@Getter
@Setter
@Schema(description = "用户分页查询参数")
@RequiredArgsConstructor
public class SystemUserPageParam extends BasePageParam {
    @Schema(hidden = true)
    private final RelationUserRoleService relationUserRoleService;
    @Schema(hidden = true)
    private final SystemUserInfoService systemUserInfoService;
    @Schema(description = "昵称")
    String nickname;
    @Schema(description = "联系电话")
    String phone;
    @Schema(description = "用户名")
    String username;
    @Schema(description = "持有的角色id")
    Long roleId;

    @Override
    public void handleQueryWrapper(QueryWrapper<?> queryWrapper) {
        queryWrapper.orderByDesc("id");

        if (!ObjectUtils.isEmpty(username)) {
            queryWrapper.and(qw -> qw.eq("username", username).or().like("username", username));
        }

        if (roleId != null) {
            final List<Long> userId = relationUserRoleService.listUserIdByRoleId(Collections.singleton(roleId));
            if (userId.size() == 0) {
                throw BusinessException.of(HttpStatus.BAD_REQUEST, "未找到持有该角色的用户");
            }
            queryWrapper.in("id", userId);
        }

        if (!ObjectUtils.isEmpty(nickname)) {
            final QueryWrapper<SystemUserInfo> qw = new QueryWrapper<>();
            qw.eq("nickname", nickname).or().like("nickname", nickname).select("user_id");
            final List<Long> userId = systemUserInfoService.list(qw).stream().map(SystemUserInfo::getUserId).toList();
            if (userId.size() == 0) {
                throw BusinessException.of(HttpStatus.BAD_REQUEST, "未找到该昵称");
            }
            queryWrapper.in("id", userId);
        }

        if (!ObjectUtils.isEmpty(phone)) {
            final QueryWrapper<SystemUserInfo> qw = new QueryWrapper<>();
            qw.eq("phone", phone).or().like("phone", phone).select("user_id");
            final List<Long> userId = systemUserInfoService.list(qw).stream().map(SystemUserInfo::getUserId).toList();
            if (userId.size() == 0) {
                throw BusinessException.of(HttpStatus.BAD_REQUEST, "未找到该电话");
            }
            queryWrapper.in("id", userId);
        }
    }
}
