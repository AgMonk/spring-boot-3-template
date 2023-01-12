package com.gin.springboot3template.sys.vo;

import com.gin.springboot3template.sys.utils.FileUtils;
import com.gin.springboot3template.sys.utils.TimeUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.File;

/**
 * 文件信息
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/1/12 11:10
 */
@Getter
@Schema(description = "文件信息")
@NoArgsConstructor
public class FileInfo {
    private static final long K = 1024;
    private static final long M = K * K;
    private static final long M5 = 5 * M;
    String ext;
    File file;
    String filename;
    Long lastModified;
    String lastModifiedDatetime;
    String shortSize;
    Long size;

    public FileInfo(File file) {
        if (file == null) {
            throw new NullPointerException();
        }
        this.size = file.length();
        this.file = file;
        this.ext = FileUtils.getFileExtName(file.getName());
        this.filename = file.getName();

        if (size < K) {
            //小于1K时显示 B
            this.shortSize = size + " B";
        } else if (size < M5) {
            // 小于5M时 显示 KB
            this.shortSize = size * 10 / K / 10.0 + " KB";
        } else {
            // 其他显示 MB
            this.shortSize = size * 10 / M / 10.0 + " MB";
        }

        this.lastModified = file.lastModified();
        this.lastModifiedDatetime = TimeUtils.format(file.lastModified() / 1000);
    }
}
