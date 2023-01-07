package com.gin.springboot3template.sys.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 过滤条件
 * @author bx002
 */
@Schema(description = "分页查询选项,在分页查询接口中使用")
@Getter
@Setter
@AllArgsConstructor
public class PageOption implements Serializable {
    @Schema(description = "出现次数")
    Integer count;
    @Schema(description = "标签")
    String label;
    @Schema(description = "值")
    Serializable value;

    public static <T> List<PageOption> of(List<T> source, Function<T, PageOption> func) {
        return source.stream().map(func).collect(Collectors.toList());
    }
}