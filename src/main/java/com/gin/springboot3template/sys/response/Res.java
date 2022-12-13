package com.gin.springboot3template.sys.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * 统一响应类
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/5 10:21
 */
@Schema(description = "统一响应对象")
public record Res<T>(
        @Schema(description = "消息")
        String message,
        @Schema(description = "数据")
        T data,
        @Schema(description = "时间戳(UNIX秒)")
        Long timestamp
) implements Serializable {
    public Res(String message, T data) {
        this(message, data, System.currentTimeMillis() / 1000);
    }

    public static <T> Res<T> of(T data) {
        return of(data, "ok");
    }

    public static <T> Res<T> of(T data, String message) {
        return new Res<>(message, data);
    }
}

