package com.gin.springboot3template.sys.utils;

import java.util.UUID;

/**
 * 字符串工具类
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/23 09:56
 */
public class StrUtils {

    /**
     * 字符串裁剪(自动处理:越界,负数,位置反写)
     * @param src   源字符串
     * @param start 开始位置
     * @param end   结束位置
     * @return 新字符串
     */
    public static String sub(String src, int start, int end) {
        if (src == null) {
            return null;
        }
        //源字符串长度
        final int length = src.length();
        //越界
        start = Math.min(start, length);
        end = Math.min(end, length);
        //负数
        start += start < 0 ? length : 0;
        end += end < 0 ? length : 0;
        // 位置反写
        final int beginIndex = Math.min(start, end);
        final int endIndex = Math.max(start, end);
        //裁剪
        return src.substring(beginIndex, endIndex);
    }

    /**
     * 生成UUID
     * @return UUID
     */
    public static String uuid() {
        return UUID.randomUUID().toString();
    }

}   
