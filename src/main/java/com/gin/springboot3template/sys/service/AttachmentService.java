package com.gin.springboot3template.sys.service;

import com.gin.springboot3template.sys.config.SystemProperties;
import com.gin.springboot3template.sys.utils.SpringContextUtils;
import com.gin.springboot3template.sys.utils.TimeUtils;

import static com.gin.springboot3template.sys.utils.FileUtils.PATH_DELIMITER;

/**
 * 附件服务
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/24 13:45
 */
public interface AttachmentService<T> extends MyService<T> {
    /**
     * 附件目录名
     */
    String PATH = "attach";

    /**
     * 附件在根目录下保存的目录名 默认使用 "/attach" + 当天日期
     * @return 附近目录
     */
    default String attachPath() {
        return PATH_DELIMITER + PATH +
                PATH_DELIMITER + TimeUtils.format(TimeUtils.DATE_FORMATTER);
    }

    /**
     * 保存附件的根目录 默认使用容器中的 SystemProperties 的 homePath
     * @return 根目录
     */
    default String homePath() {
        return SpringContextUtils.getContext().getBean(SystemProperties.class).getHomePath();
    }
}
