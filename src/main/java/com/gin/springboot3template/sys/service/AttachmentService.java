package com.gin.springboot3template.sys.service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.gin.springboot3template.sys.base.BaseAttach;
import com.gin.springboot3template.sys.config.SystemProperties;
import com.gin.springboot3template.sys.utils.FileUtils;
import com.gin.springboot3template.sys.utils.SpringContextUtils;
import com.gin.springboot3template.sys.utils.TimeUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import static com.gin.springboot3template.sys.utils.FileUtils.PATH_DELIMITER;

/**
 * 附件服务: 建议重写 attachPath 方法 , 在路径中添加附件所有者的类型名.
 * 由于附件可能在其所有者创建之前上传, 因此上传接口中所有者id为可选提供;当未提供时, 创建其所有者之后应当调用 configOwnerId 方法将之前上传的附件划归给该所有者.
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/24 13:45
 */
public interface AttachmentService<T extends BaseAttach> extends MyService<T> {
    /**
     * 附件目录名
     */
    String PATH = "attach";

    /**
     * 创建附件对象
     * @param multipartFile 上传的文件
     * @param ownerId       所有者id
     * @return 附件对象
     */
    T createAttach(MultipartFile multipartFile, Long ownerId);

    /**
     * 附件在根目录下保存的目录名 默认使用 "/attach" + 当天日期
     * @return 附近目录
     */
    default String attachPath() {
        return PATH_DELIMITER + PATH + PATH_DELIMITER + TimeUtils.format(TimeUtils.DATE_FORMATTER);
    }

    /**
     * 配置附件的持有者id
     * @param ownerId  持有者id
     * @param attachId 附件id
     */
    default void configOwnerId(long ownerId, Set<Long> attachId) {
        final UpdateWrapper<T> uw = new UpdateWrapper<>();
        uw.set("owner_id", ownerId).in("id", attachId);
        update(uw);
    }

    /**
     * 保存附件的根目录 默认使用容器中的 SystemProperties 的 homePath
     * @return 根目录
     */
    default String homePath() {
        return SpringContextUtils.getContext().getBean(SystemProperties.class).getHomePath();
    }

    /**
     * 保存附件文件
     * @param multipartFile 上传的附件
     * @param entity        实体对象
     * @throws IOException 异常
     */
    default void saveMultipartFile(MultipartFile multipartFile, T entity) throws IOException {
        final File destFile = new File(homePath() + attachPath() + entity.getFilePath());
        FileUtils.mkdir(destFile.getParentFile());
        multipartFile.transferTo(destFile);
    }

    /**
     * 上传一个附件
     * @param file    上传的文件
     * @param ownerId 所有者id
     * @return 附件对象
     * @throws IOException 异常
     */
    default T upload(MultipartFile file, Long ownerId) throws IOException {
        final T entity = createAttach(file, ownerId);
        saveMultipartFile(file, entity);
        save(entity);
        return entity;
    }

//    todo 查询附件
//    todo 删除附件
}
