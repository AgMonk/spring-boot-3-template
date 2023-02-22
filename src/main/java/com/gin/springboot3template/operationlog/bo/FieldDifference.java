package com.gin.springboot3template.operationlog.bo;

/**
 * 字段差异
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/22 10:19
 */
public record FieldDifference<F, V>(
        F field,
        V beforeValue,
        V afterValue
) {

}   
