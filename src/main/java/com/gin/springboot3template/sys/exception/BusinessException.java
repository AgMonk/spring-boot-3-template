package com.gin.springboot3template.sys.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * 业务异常
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/10 11:05
 */
@Getter
@RequiredArgsConstructor
public class BusinessException extends RuntimeException {

    final HttpStatus httpStatus;
    final String title;
    final List<String> messages;

    public static BusinessException of(HttpStatus httpStatus, String title, List<String> messages) {
        return new BusinessException(httpStatus, title, messages);
    }

    public static BusinessException of(HttpStatus httpStatus, String title, String... messages) {
        return new BusinessException(httpStatus, title, List.of(messages));
    }


}
