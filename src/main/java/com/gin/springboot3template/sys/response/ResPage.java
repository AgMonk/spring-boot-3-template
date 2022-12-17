package com.gin.springboot3template.sys.response;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gin.springboot3template.sys.bo.Constant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/17 13:48
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "分页数据响应对象")
public class ResPage<T> extends Res<List<T>> {
    @Schema(description = "页数")
    Long page;
    @Schema(description = "每页条数")
    Long size;
    @Schema(description = "总条数")
    Long total;
    @Schema(description = "总页数")
    Long totalPage;

    public static <T> ResPage<T> of(Page<T> page) {
        final ResPage<T> data = new ResPage<>();
        data.setPage(page.getCurrent());
        data.setSize(page.getSize());
        data.setTotal(page.getTotal());
        data.setData(page.getRecords());
        data.setTotalPage(page.getPages());
        data.setMessage(CollectionUtils.isEmpty(page.getRecords()) ? Constant.MESSAGE_DATA_NOT_FOUND : "ok");
        return data;
    }

}
