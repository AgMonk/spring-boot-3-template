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

    public static final String DOT = ".";

    /**
     * 递归创建目录
     * @param dir 文件
     */
    public static void mkdir(File dir) throws IOException {
        if (dir.exists() && !dir.isDirectory()) {
            throw new IOException("该文件已存在且不是目录: " + dir.getPath());
        }
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("目录创建失败: " + dir.getPath());
        }
    }

    /**
     * 返回文件的后缀名(小写)
     * @param filename 文件名
     * @return 后缀名(小写)
     */
    public static String getFileExtName(String filename) {
        if (!filename.contains(DOT)) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(DOT) + 1).toLowerCase();

    }

    /**
     * 返回文件的主文件名(排除后缀部分)
     * @param filename 文件名
     * @return 主文件名
     */
    public static String getFileMainName(String filename) {
        return filename.substring(0, filename.lastIndexOf(DOT) + 1);

    }

    /**
     * 删除一个文件
     * @param file 文件
     */
    public static void deleteFile(File file) throws IOException {
        assertExists(file);
        if (!file.delete()) {
            throw new IOException("文件删除失败: " + file.getPath());
        }
    }

    /**
     * 断言文件不是null
     * @param file 文件
     */
    private static void assertNotNull(File file) throws IOException {
        if (file == null) {
            throw new IOException("文件为null");
        }
    }

    /**
     * 断言文件存在
     * @param file 文件
     */
    private static void assertExists(File file) throws IOException {
        assertNotNull(file);
        if (!file.exists()) {
            throw new IOException("文件不存在");
        }
    }
}   
