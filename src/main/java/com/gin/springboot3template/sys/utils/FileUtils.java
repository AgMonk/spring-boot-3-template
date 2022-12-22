package com.gin.springboot3template.sys.utils;

import java.io.File;
import java.io.IOException;

/**
 * 文件工具类
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/22 17:16
 */
public class FileUtils {
    /**
     * 递归创建目录
     * 如果参数是目录,递归创建该目录;如果参数是文件,递归创建它的父目录
     * @param file 文件
     */
    public static void mkdir(File file) throws IOException {
        File destDir = file.isDirectory() ? file : file.getParentFile();
        if (!destDir.exists() && !destDir.mkdirs()) {
            throw new IOException("目录创建失败: " + destDir.getPath());
        }
    }

}   
