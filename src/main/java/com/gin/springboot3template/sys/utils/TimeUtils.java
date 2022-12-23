package com.gin.springboot3template.sys.utils;

import static java.util.concurrent.TimeUnit.*;

/**
 * 时间工具类
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/23 11:14
 */
public class TimeUtils {
    /**
     * 千
     */
    private final static long K = 1000L;

    /**
     * 返回简单格式的时间间隔
     * @param start 开始时刻(毫秒)
     * @param end   结束时刻(毫秒)
     * @return 简单格式时间间隔
     */
    @SuppressWarnings("AlibabaUndefineMagicConstant")
    public static String simpleDuration(long start, long end) {
        final long ms = end - start;
        final long seconds = MILLISECONDS.toSeconds(ms);
        final long minutes = SECONDS.toMinutes(seconds);
        //10秒内显示毫秒
        if (ms < SECONDS.toMillis(10)) {
            return ms + "ms";
        }
        //2分钟内显示秒
        if (ms < MINUTES.toMillis(2L)) {
            return seconds + "s";
        }
        // 10分钟内显示 分+秒
        if (ms < MINUTES.toMillis(10)) {
            final long s = seconds - MINUTES.toSeconds(minutes);
            return String.format("%dm%ds", minutes, s);
        }
        // 1小时内显示 分钟
        if (ms < HOURS.toMillis(1)) {
            return SECONDS.toMinutes(seconds) + "m";
        }
        // 2天内 显示 小时+分
        if (ms < DAYS.toMillis(2)) {
            final long h = MINUTES.toHours(minutes);
            final long m = minutes - HOURS.toMinutes(h);
            return String.format("%dh%dm", h, m);
        }
        // 超过2天 显示 天+小时+分
        final long d = MINUTES.toDays(minutes);
        final long h = MINUTES.toHours(minutes - DAYS.toMinutes(d));
        final long m = minutes - DAYS.toMinutes(d) - HOURS.toMinutes(h);
        return String.format("%dd%dh%dm", d, h, m);
    }

}   
