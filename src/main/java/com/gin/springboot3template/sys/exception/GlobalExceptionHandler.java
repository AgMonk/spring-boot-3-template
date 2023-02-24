package com.gin.springboot3template.sys.exception;

import com.gin.springboot3template.sys.bo.Constant;
import com.gin.springboot3template.sys.bo.ExpressionExceptionParser;
import com.gin.springboot3template.sys.vo.response.Res;
import com.gin.springboot3template.user.entity.SystemPermission;
import com.gin.springboot3template.user.service.SystemPermissionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.authentication.rememberme.CookieTheftException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 统一异常处理
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/10 11:16
 */
@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final SystemPermissionService systemPermissionService;

    @ResponseBody
    @ExceptionHandler({CookieTheftException.class})
    public ResponseEntity<Res<Void>> exceptionHandler(CookieTheftException e) {
        return new ResponseEntity<>(Res.of(null, e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseBody
    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Res<String>> exceptionHandler(AccessDeniedException e, @SuppressWarnings("unused") HttpServletRequest request) {
        log.warn(e.getLocalizedMessage());
        final SystemPermission permission = systemPermissionService.getByPath(request.getRequestURI());
        final String message = permission == null ? null : new ExpressionExceptionParser(permission.getPreAuthorize(), request).explain();
        return new ResponseEntity<>(Res.of(message, Constant.Messages.ACCESS_DENIED), HttpStatus.FORBIDDEN);
    }

    @ResponseBody
    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    public ResponseEntity<Res<String>> exceptionHandler(
            HttpMediaTypeNotSupportedException e,
            @SuppressWarnings("unused") HttpServletRequest request
    ) {
        log.warn(e.getLocalizedMessage());
        return new ResponseEntity<>(Res.of("不允许的 Content-Type", e.getLocalizedMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * 兜底异常处理
     * @param e 其他异常
     * @return 处理
     */
    @ResponseBody
    @ExceptionHandler({Exception.class})
    public ResponseEntity<Res<Void>> exceptionHandler(Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>(Res.of(null, "服务器错误 请通知管理员"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseBody
    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ResponseEntity<Res<String>> exceptionHandler(MissingServletRequestParameterException e) {
        return new ResponseEntity<>(Res.of(e.getLocalizedMessage(), "请求缺少必须的参数"), HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler({MaxUploadSizeExceededException.class})
    public ResponseEntity<Res<String>> exceptionHandler(MaxUploadSizeExceededException e) {
        return new ResponseEntity<>(Res.of("文件大小超出限制", e.getLocalizedMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * 参数校验处理
     * @param e 阐述校验异常
     * @return 处理
     */
    @ResponseBody
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Res<String>> exceptionHandler(ConstraintViolationException e) {
        return new ResponseEntity<>(Res.of(e.getLocalizedMessage(), "参数校验异常"), HttpStatus.BAD_REQUEST);
    }

    /**
     * 业务异常处理
     * @param e 业务异常
     * @return 处理
     */
    @ResponseBody
    @ExceptionHandler({BusinessException.class})
    public ResponseEntity<Res<List<String>>> exceptionHandler(BusinessException e) {
        log.warn("code: {} , 业务异常: {} , value: {}", e.getHttpStatus().value(), e.getTitle(), e.getMessages());
        return new ResponseEntity<>(Res.of(e.getMessages(), e.getTitle()), e.getHttpStatus());
    }

    @ResponseBody
    @ExceptionHandler({BindException.class})
    public ResponseEntity<Res<?>> exceptionHandler(BindException e) {
        final List<MyFieldError> fieldErrors = e.getFieldErrors().stream().map(MyFieldError::new).collect(Collectors.toList());
        if (fieldErrors.size() == 0) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(Res.of(fieldErrors, "参数校验错误"), HttpStatus.BAD_REQUEST);
    }

    /**
     * 不支持的请求方法
     * @param e 其他异常
     * @return 处理
     */
    @ResponseBody
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<Res<String>> exceptionHandler(HttpRequestMethodNotSupportedException e) {
        return new ResponseEntity<>(Res.of(e.getLocalizedMessage(), "不支持的请求方法"), HttpStatus.BAD_REQUEST);
    }
}   
