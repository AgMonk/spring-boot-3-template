package com.gin.springboot3template.operationlog.bo;

import com.gin.springboot3template.operationlog.entity.SystemOperationLog;
import com.gin.springboot3template.operationlog.enums.OperationType;
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
    Long id;
    Long userId;
    String userIp;
    OperationType type;


    public DescriptionContext() {
        this.userId = MySecurityUtils.currentUserDetails().getId();
        this.userIp = WebUtils.getRemoteHost();
    }

    public DescriptionContext(Object param, Long id, OperationType type) {
        this();
        this.param = param;
        this.id = id;
        this.type = type;
    }

    public SystemOperationLog generateLog() {
        final SystemOperationLog log = new SystemOperationLog();
        log.setType(type);
        log.setUserIp(userIp);
        log.setUserId(userId);
        return log;
    }

}
