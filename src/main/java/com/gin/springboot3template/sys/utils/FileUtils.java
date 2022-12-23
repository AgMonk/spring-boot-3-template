package com.gin.springboot3template.sys.utils;

import com.gin.springboot3template.sys.exception.file.*;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;

/**
 * 文件工具类
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/22 17:16
 */
@Slf4j
public class FileUtils {

    public static final String DOT = ".";
    public static final String PATH_DELIMITER = "/";

    /**
     * 递归创建目录
     * @param dir 文件
     */
    public static void mkdir(File dir) throws DirCreateException, FileExistsException {
        if (dir.exists() && !dir.isDirectory()) {
            throw new FileExistsException(dir);
        }
        if (!dir.exists() && !dir.mkdirs()) {
            throw new DirCreateException(dir);
        }
        log.info("创建目录:" + dir.getPath());
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
    public static void deleteFile(File file) throws FileNotExistsException, FileDeleteException {
        assertExists(file);
        if (!file.delete()) {
            throw new FileDeleteException(file);
        }
        log.info("已删除文件:" + file.getPath());
    }

    /**
     * 移动文件
     * @param src  源文件
     * @param dest 目标文件
     */
    public static void move(File src, File dest) throws FileMoveException, FileNotExistsException, FileExistsException, DirCreateException {
        assertExists(src);
        assertNotExists(dest);
        mkdir(dest.getParentFile());
        if (!src.renameTo(dest)) {
            throw new FileMoveException(src, dest);
        }
        log.info("已移动文件 {} -> {}", src.getPath(), dest.getPath());
    }

    /**
     * 将文件移动到一个指定目录
     * @param src 源文件
     * @param dir 目标目录
     */
    public static void move2Dir(File src, File dir) throws FileNotExistsException, DirCreateException, FileMoveException, FileExistsException {
        move(src, new File(dir.getPath() + PATH_DELIMITER + src.getName()));
    }

    /**
     * 递归删除一个目录
     * @param dir 目录
     */
    public static void deleteDir(File dir) {
        //todo
    }

    /**
     * 根据file是目录还是文件调用不同的删除方法
     * @param file 目录或文件
     */
    public static void delete(File file) throws FileNotExistsException, FileDeleteException {
        assertExists(file);
        if (file.isDirectory()) {
            deleteDir(file);
        } else {
            deleteFile(file);
        }
    }

    /**
     * 断言文件不是null
     * @param file 文件
     */
    private static void assertNotNull(File file) {
        if (file == null) {
            throw new RuntimeException("文件为null");
        }
    }

    /**
     * 断言文件存在
     * @param file 文件
     */
    private static void assertExists(File file) throws FileNotExistsException {
        assertNotNull(file);
        if (!file.exists()) {
            throw new FileNotExistsException(file);
        }
    }

    /**
     * 断言文件不存在
     * @param file 文件
     */
    private static void assertNotExists(File file) throws FileExistsException {
        assertNotNull(file);
        if (!file.exists()) {
            throw new FileExistsException(file);
        }
    }


    /**
     * 复制文件 使用 CHANNEL 方法
     * @param src  源文件
     * @param dest 目标文件
     */
    public static void copyFile(File src, File dest) throws IOException {
        copyFile(src, dest, CopyMethod.CHANNEL);
    }

    /**
     * 复制文件
     * @param src        源文件
     * @param dest       目标文件
     * @param copyMethod 复制方法 默认为  CHANNEL
     */
    public static void copyFile(File src, File dest, CopyMethod copyMethod) throws IOException {
        assertExists(src);
        assertNotExists(dest);
        switch (copyMethod) {
            case JAVA7 -> copyFileUsingJava7Files(src, dest);
            case STREAM -> copyFileUsingFileStreams(src, dest);
            default -> copyFileUsingFileChannels(src, dest);
        }
    }

    /**
     * 使用FileStreams复制 不能吃到任何缓存
     * @param src  源文件
     * @param dest 目标文件
     */
    private static void copyFileUsingFileStreams(File src, File dest) throws IOException {
        try (InputStream input = new FileInputStream(src); OutputStream output = new FileOutputStream(dest)) {
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
        }
    }

    /**
     * 使用FileChannel复制 能吃到3种方式的缓存
     * @param src  源文件
     * @param dest 目标文件
     */
    private static void copyFileUsingFileChannels(File src, File dest) throws IOException {
        try (FileChannel inputChannel = new FileInputStream(src).getChannel(); FileChannel outputChannel = new FileOutputStream(dest).getChannel()) {
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        }
    }

    /**
     * 使用Java7的Files类复制  能吃到3种方式的缓存
     * @param source 源文件
     * @param dest   目标文件
     */
    private static void copyFileUsingJava7Files(File source, File dest) throws IOException {
        Files.copy(source.toPath(), dest.toPath());
    }

    public enum CopyMethod {
        STREAM, CHANNEL, JAVA7
    }

}   
