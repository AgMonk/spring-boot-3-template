package com.gin.springboot3template.sys.service;

import com.gin.springboot3template.sys.config.DatabaseProperties;
import com.gin.springboot3template.sys.config.SystemProperties;
import com.gin.springboot3template.sys.enums.OsType;
import com.gin.springboot3template.sys.enums.ServiceStatus;
import com.gin.springboot3template.sys.exception.BusinessException;
import com.gin.springboot3template.sys.exception.file.DirCreateException;
import com.gin.springboot3template.sys.exception.file.FileExistsException;
import com.gin.springboot3template.sys.utils.FileUtils;
import com.gin.springboot3template.sys.utils.IoUtils;
import com.gin.springboot3template.sys.utils.ProcessUtils;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * 数据库备份服务
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/1/11 16:09
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DatabaseBackupService {
    /**
     * 备份命令
     */
    private static final String DUMP = "mysqldump";
    private static final String PATH_BACKUP = "/database_backup";
    private static final String PATH_TEMP = "/mysql_client";
    private final SystemProperties systemProperties;
    private final DatabaseProperties databaseProperties;
    private final DataSourceProperties dataSourceProperties;
    @Getter
    ServiceStatus status = ServiceStatus.disable;
    private File backupDir;
    private File tempDir;

    public void autoBackup() {
        //todo 自动备份
    }

    public void autoClear() {
        //todo 自动清理
    }

    @Async
    public void backup() {
        //todo 备份当前数据库
    }

    public void del() {
        //todo 删除镜像
    }

    @PostConstruct
    public void init() throws DirCreateException, FileExistsException {
        //初始化 创建文件夹
        tempDir = new File(systemProperties.getHomePath() + PATH_TEMP);
        backupDir = new File(systemProperties.getHomePath() + PATH_BACKUP);
        FileUtils.mkdir(tempDir);
        FileUtils.mkdir(backupDir);
    }

    public void list() {
        //todo 列出备份镜像
    }

    /**
     * 检查mysql client是否安装完毕 , 如果未安装 执行安装
     */
    @Async
    public void prepare() {
        if (OsType.find() != OsType.LINUX) {
            log.warn("当前系统非 Linux ,不执行数据库备份相关操作");
            return;
        }
        //如果状态为可用 不执行相关操作
        if (status == ServiceStatus.enable) {
            log.info("MysqlClient 可用");
            return;
        }
        //如果状态为可用 不执行相关操作
        if (status == ServiceStatus.preparing) {
            throw BusinessException.of(HttpStatus.SERVICE_UNAVAILABLE, "服务正在准备中");
        }

        //检查dump是否可用
        if (!checkDumpCmd()) {
            //命令不可用
            try {
                status = ServiceStatus.preparing;
                //下载
                download();
                //安装
                install();
                //再次检查
                if (checkDumpCmd()) {
                    log.info("Mysql Client安装完毕 , 备份和还原功能可用");
                }
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Async
    public void recover() {
        //todo 指定一个文件进行还原
    }

    /**
     * 检查mysqldump命令是否可用
     * @return 是否可用
     */
    private boolean checkDumpCmd() {
        try {
            log.info("检查 mysqldump 命令是否可用");
            final ProcessBuilder pb = new ProcessBuilder(DUMP);
            pb.redirectErrorStream(true);
            final Process process = pb.start();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            IoUtils.readLine(reader, log::info);
            status = ServiceStatus.enable;
            return true;
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
            status = ServiceStatus.disable;
            return false;
        }
    }

    /**
     * 下载 mysql client 所需文件
     */
    private void download() throws IOException, InterruptedException {
        final List<String> files = FileUtils.listFiles(tempDir).stream().map(File::getName).toList();
        for (String url : databaseProperties.getMysqlClient()) {
            final String filename = url.substring(url.lastIndexOf("/") + 1);
            if (!files.contains(filename)) {
                log.info("开始下载: " + url);
                final Process process = ProcessUtils.downloadWithCurl(url, tempDir);
                if (process.waitFor() == 0) {
                    log.info("下载完毕: " + url);
                }
            } else {
                log.info("文件已存在,跳过: " + filename);
            }
        }
    }

    /**
     * 安装mysql client
     */
    private void install() throws IOException {
        final Process installProcess = new ProcessBuilder("rpm", "-ivh", "mysql-community-*").directory(tempDir).start();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(installProcess.getInputStream()));
        IoUtils.readLine(reader, i -> log.info("正在安装: {}", i));
        log.info("安装完毕...");
    }
}
