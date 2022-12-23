package com.gin.springboot3template.sys.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.*;
import java.util.function.Function;

/**
 * 文件IO工具类
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/22 16:07
 */
public class FileIoUtils {
    private final static ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 从文件获取 BufferedReader
     * @param file 文件
     * @return BufferedReader
     */
    public static BufferedReader getReader(File file) throws FileNotFoundException {
        return new BufferedReader(new FileReader(file));
    }

    /**
     * 从文件获取 BufferedInputStream
     * @param file 文件
     * @return BufferedInputStream
     */
    public static BufferedInputStream getInputStream(File file) throws FileNotFoundException {
        return new BufferedInputStream(new FileInputStream(file));
    }

    /**
     * 从文件获取 BufferedOutputStream
     * @param file 文件
     * @return BufferedOutputStream
     */
    public static BufferedOutputStream getOutputStream(File file) throws FileNotFoundException {
        return new BufferedOutputStream(new FileOutputStream(file));
    }

    /**
     * 从文件获取 PrintWriter
     * @param file 文件
     * @return PrintWriter
     */
    public static PrintWriter getWriter(File file) throws IOException {
        FileUtils.mkdir(file.getParentFile());
        return new PrintWriter(new FileWriter(file));
    }

    /**
     * 将对象转换成json格式写入文件;如果文件已存在将会覆盖文件
     * @param file 文件
     * @param obj  对象
     */
    public static void writeObj(File file, Object obj) throws IOException {
        try (PrintWriter writer = getWriter(file)) {
            MAPPER.writerWithDefaultPrettyPrinter().writeValue(writer, obj);
        }
    }

    /**
     * 从文件中读取json字符串并解析为对象
     * @param file      文件
     * @param valueType 类对象
     * @param <T>       T
     * @return 指定类的对象
     */
    public static <T> T readObj(File file, Class<T> valueType) throws IOException {
        try (BufferedReader reader = getReader(file)) {
            return MAPPER.readValue(reader, valueType);
        }
    }

    /**
     * 从文件中读取json字符串并解析为对象
     * @param file     文件
     * @param javaType javaType 构造泛型:MAPPER.getTypeFactory().constructParametricType
     * @param <T>      T
     * @return 指定类对象
     */
    public static <T> T readObj(File file, JavaType javaType) throws IOException {
        try (BufferedReader reader = getReader(file)) {
            return MAPPER.readValue(reader, javaType);
        }
    }

    /**
     * 从文件中读取json字符串并解析为对象
     * @param file          文件
     * @param typeReference typeReference
     * @param <T>           T
     * @return 指定类对象
     */
    public static <T> T readObj(File file, TypeReference<T> typeReference) throws IOException {
        try (BufferedReader reader = getReader(file)) {
            return MAPPER.readValue(reader, typeReference);
        }
    }


    /**
     * 从文件中读取json字符串并解析为对象
     * @param file 文件
     * @param func 通过 TypeFactory 的 constructParametricType 等方法创建 JavaType
     * @param <T>  T
     * @return 指定类对象
     */
    public static <T> T readObj(File file, Function<TypeFactory, JavaType> func) throws IOException {
        return readObj(file, func.apply(MAPPER.getTypeFactory()));
    }


    public static void main(String[] args) throws IOException {
    }
}
