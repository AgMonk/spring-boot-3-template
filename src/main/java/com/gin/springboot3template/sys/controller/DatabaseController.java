package com.gin.springboot3template.sys.controller;

import com.gin.springboot3template.sys.annotation.MyRestController;
import com.gin.springboot3template.sys.service.DatabaseBackupService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 数据库备份服务
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/1/12 14:47
 */

@MyRestController(DatabaseController.API_PREFIX)
@RequiredArgsConstructor
@Tag(name = DatabaseController.GROUP_NAME)
@Slf4j
public class DatabaseController {
    /**
     * 接口路径前缀
     */
    public static final String API_PREFIX = "/sys/database";
    public static final String GROUP_NAME = "数据库备份服务接口";

    private final DatabaseBackupService service;

    //todo 查询服务状态
    //todo 查询镜像列表
    //todo 执行备份
    //todo 执行还原
    //todo 上传镜像文件
    //todo 下载镜像文件

}