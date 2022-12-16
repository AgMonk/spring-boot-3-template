package com.gin.springboot3template.sys.security.interfaze.impl;

import com.gin.springboot3template.sys.bo.Constant;
import com.gin.springboot3template.sys.security.bo.MyUserDetails;
import com.gin.springboot3template.sys.security.interfaze.TypeNameAuthorityEvaluator;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/16 17:29
 */
public class RoleAuthorityEvaluatorImpl implements TypeNameAuthorityEvaluator {

    @Override
    public List<String> getTargetTypes() {
        return Collections.singletonList(Constant.EVALUATOR_TYPE_ROLE);
    }

    @Override
    public boolean hasPermission(MyUserDetails userDetails, Serializable targetId, Object permission) {
        return userDetails.hasRole(String.valueOf(targetId));
    }
}
