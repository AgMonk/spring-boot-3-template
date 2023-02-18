package com.gin.springboot3template.operationlog.bo;

import com.gin.springboot3template.sys.security.utils.MySecurityUtils;
import com.gin.springboot3template.sys.utils.WebUtils;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述生成上下文
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/18 16:45
 */
@Getter
@Setter
public class DescriptionContext {
    Object param;
    Object id;
    Long userId;
    String userIp;


    public DescriptionContext() {
        this.userId = MySecurityUtils.currentUserDetails().getId();
        this.userIp = WebUtils.getRemoteHost();
    }

    public DescriptionContext(Object param, Object id) {
        this();
        this.param = param;
        this.id = id;
    }
}
