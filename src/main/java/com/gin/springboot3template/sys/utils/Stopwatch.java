package com.gin.springboot3template.sys.utils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;

/**
 * 秒表
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/23 11:02
 */
@Getter
@Slf4j
public class Stopwatch {
    /**
     * 计时开始的时间
     */
    private final long start;
    /**
     * 计时过程
     */
    private final LinkedHashMap<String, Long> process = new LinkedHashMap<>();
    /**
     * 上一个标签计时点
     */
    private long last;

    public Stopwatch() {
        final long now = now();
        this.start = now;
        this.last = now;
    }

    private static long now() {
        return System.currentTimeMillis();
    }

    /**
     * 标记一个计时点
     * @param tag      标签名
     * @param printLog 是否输出日志
     */
    public void tag(String tag, boolean printLog) {
        final long now = now();
        if (printLog) {
            log.debug("标记计时点 [{}] 距离上一个计时点: {} 总耗时: {}");
        }
        this.last = now;
        process.put(tag, now);
    }
}
