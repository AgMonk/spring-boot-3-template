package com.gin.springboot3template.sys.validation;


import com.gin.springboot3template.sys.bo.Constant;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static com.gin.springboot3template.sys.validation.ValidatorUtils.changeMessage;

/**
 * @author bx002
 */
public class PasswordValidator implements ConstraintValidator<Password, String> {
    String prefix;
    private boolean nullable;

    @Override
    public void initialize(Password constraint) {
        this.nullable = constraint.nullable();
        this.prefix = constraint.prefix();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {
        if (s == null || "".equals(s)) {
            if (nullable) {
                return true;
            }
            changeMessage(context, prefix + "密码不允许为空");
            return false;
        }
        if (s.length() < Constant.Security.PASSWORD_MIN_LENGTH || s.length() > Constant.Security.PASSWORD_MAX_LENGTH) {
            changeMessage(context,
                          prefix + String.format("密码长度应介于 [%d,%d]",
                                                 Constant.Security.PASSWORD_MIN_LENGTH,
                                                 Constant.Security.PASSWORD_MAX_LENGTH));
            return false;
        }

        return true;
    }
}
