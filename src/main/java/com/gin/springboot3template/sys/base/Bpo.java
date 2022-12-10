package com.gin.springboot3template.sys.base;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 基础持久化对象
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/6 14:18
 */
@Getter
@Setter
public class Bpo implements Serializable {
    @TableId(type = IdType.AUTO)
    @Column(comment = "id", isAutoIncrement = true)
    Long id;

    @Column(comment = "记录创建时间", isNull = false)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    Long timeCreate;

    public Bpo() {
        this.timeCreate = System.currentTimeMillis() / 1000;
    }
}
