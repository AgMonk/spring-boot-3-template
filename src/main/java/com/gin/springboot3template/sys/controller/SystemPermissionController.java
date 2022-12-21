package com.gin.springboot3template.sys.controller;

import com.gin.springboot3template.sys.annotation.MyRestController;
import com.gin.springboot3template.sys.bo.Constant;
import com.gin.springboot3template.sys.dto.param.SystemPermissionPageParam;
import com.gin.springboot3template.sys.entity.SystemPermission;
import com.gin.springboot3template.sys.response.Res;
import com.gin.springboot3template.sys.response.ResPage;
import com.gin.springboot3template.sys.service.SystemPermissionService;
import com.gin.springboot3template.sys.vo.PermissionGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 权限
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/20 14:02
 */

@MyRestController(SystemPermissionController.API_PREFIX)
@RequiredArgsConstructor
@Tag(name = SystemPermissionController.GROUP_NAME)
@Slf4j
public class SystemPermissionController {
    /**
     * 接口路径前缀
     */
    public static final String API_PREFIX = "/sys/permission";
    public static final String GROUP_NAME = "权限接口";
    private final SystemPermissionService systemPermissionService;

    @GetMapping(Constant.Api.GROUP)
    @Operation(summary = "分组查询")
    @PreAuthorize(Constant.Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<List<PermissionGroup>> group(@SuppressWarnings("unused") HttpServletRequest request) {
        final Map<String, List<SystemPermission>> map = systemPermissionService.list().stream().collect(Collectors.groupingBy(SystemPermission::getGroupName));
        final List<PermissionGroup> data = map.keySet().stream()
                .map(name -> new PermissionGroup(name, map.get(name)))
                .sorted(Comparator.comparing(PermissionGroup::getGroupName))
                .toList();
        return Res.of(data);
    }

    @GetMapping(Constant.Api.PAGE)
    @Operation(summary = "分页查询")
    @PreAuthorize(Constant.Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<ResPage<SystemPermission>> page(
            @ParameterObject SystemPermissionPageParam param,
            @SuppressWarnings("unused") HttpServletRequest request
    ) {
        return Res.of(systemPermissionService.pageByParam(param, i -> i));
    }

}