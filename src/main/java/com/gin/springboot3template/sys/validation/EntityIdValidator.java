package com.gin.springboot3template.sys.validation;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gin.springboot3template.sys.exception.BusinessException;
import com.gin.springboot3template.sys.utils.SpringContextUtils;
import org.springframework.http.HttpStatus;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.Serializable;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/10 16:24
 */
public class EntityIdValidator implements ConstraintValidator<EntityId, Serializable> {
    private boolean nullable;
    private Serializable value;

    private IService<?> service;

    private Class<?> clazz;

    @Override
    public void initialize(EntityId constraintAnnotation) {
        this.nullable = constraintAnnotation.nullable();
        this.value = constraintAnnotation.value();
        this.clazz = constraintAnnotation.service();
        this.service = SpringContextUtils.getContext().getBean(constraintAnnotation.service());


    }

    @Override
    public boolean isValid(Serializable serializable, ConstraintValidatorContext constraintValidatorContext) {
        if (this.service == null) {
            throw BusinessException.of(HttpStatus.INTERNAL_SERVER_ERROR, "服务器错误", "未找到指定的service: " + clazz);
        }
        if (value == null || "".equals(value)) {
            if (nullable) {
                return true;
            }
//            changeMessage(constraintValidatorContext,  "编号不允许为空");
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("不允许为空").addConstraintViolation();
            return false;
        }
        if (service.getById(value) == null) {
//            changeMessage(constraintValidatorContext, String.format("Id: %s ,不存在",this.value));
            return false;
        }
        return true;
    }
}
