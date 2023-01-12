package com.gin.springboot3template.sys.controller;

import com.gin.springboot3template.sys.annotation.MyRestController;
import com.gin.springboot3template.sys.bo.Constant;
import com.gin.springboot3template.sys.enums.ServiceStatus;
import com.gin.springboot3template.sys.exception.file.FileDeleteException;
import com.gin.springboot3template.sys.exception.file.FileNotExistsException;
import com.gin.springboot3template.sys.response.Res;
import com.gin.springboot3template.sys.service.DatabaseBackupService;
import com.gin.springboot3template.sys.vo.FileInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

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
    public static final String API_PREFIX = "/database";
    public static final String GROUP_NAME = "数据库备份服务接口";

    private final DatabaseBackupService service;

    @GetMapping(Constant.Api.DOWNLOAD + "/{filename}")
    @Operation(summary = "下载镜像文件")
    @PreAuthorize(Constant.Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public void getDownload(
            @PathVariable @Parameter(description = "文件名") String filename,
            HttpServletResponse response,
            @SuppressWarnings("unused") HttpServletRequest request
    ) throws IOException {
        service.download(filename, response);
    }

    @GetMapping(Constant.Api.LIST)
    @Operation(summary = "查询镜像列表")
    @PreAuthorize(Constant.Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<List<FileInfo>> getList(@SuppressWarnings("unused") HttpServletRequest request) throws IOException {
        return Res.of(service.list());
    }

    @GetMapping(Constant.Api.STATUS)
    @Operation(summary = "查询服务状态")
    @PreAuthorize(Constant.Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<ServiceStatus> getStatus(@SuppressWarnings("unused") HttpServletRequest request) {
        return Res.of(service.getStatus(), service.getStatus().getZh());
    }

    @PostMapping("backup")
    @Operation(summary = "执行备份", description = "返回备份好的文件信息")
    @PreAuthorize(Constant.Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<FileInfo> postBackup(
            @RequestParam(required = false, defaultValue = "true") @Parameter(description = "是否使用gzip压缩,默认true") Boolean gzip,
            @SuppressWarnings("unused") HttpServletRequest request
    ) throws IOException {
        return Res.of(service.backup(gzip), "备份成功");
    }

    @PostMapping(Constant.Api.DEL)
    @Operation(summary = "删除镜像文件", description = "返回被删除的文件信息")
    @PreAuthorize(Constant.Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<FileInfo> postDel(
            @RequestParam @Parameter(description = "文件名") String filename,
            @SuppressWarnings("unused") HttpServletRequest request
    ) throws FileNotExistsException, FileDeleteException {
        return Res.of(service.del(filename), "删除成功");
    }

    @PostMapping("recover")
    @Operation(summary = "执行还原", description = "返回被还原的文件信息")
    @PreAuthorize(Constant.Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<FileInfo> postRecover(
            @RequestParam @Parameter(description = "文件名") String filename,
            @SuppressWarnings("unused") HttpServletRequest request
    ) throws IOException {
        return Res.of(service.recover(filename), "还原成功");
    }

    @PostMapping(value = Constant.Api.UPLOAD, consumes = {MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "上传镜像文件", description = "文件后缀必须为 sql 或 gz;返回被上传的文件信息")
    @PreAuthorize(Constant.Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<FileInfo> postUpload(
            MultipartFile file,
            @SuppressWarnings("unused") HttpServletRequest request
    ) throws IOException {
        return Res.of(service.upload(file), "上传成功");
    }
}