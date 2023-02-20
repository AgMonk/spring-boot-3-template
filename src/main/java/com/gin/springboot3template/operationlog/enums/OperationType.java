package com.gin.springboot3template.operationlog.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

/**
 * 操作类型
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/18 13:55
 */
@RequiredArgsConstructor
public enum OperationType {
    /**
     * 添加
     */
    ADD("添加"),
    /**
     * 删除
     */
    DEL("删除"),
    /**
     * 修改
     */
    UPDATE("修改"),
    /**
     * 查询
     */
    QUERY("查询"),
    ;
    final String name;

    @JsonValue
    public String getName() {
        return name;
    }
}