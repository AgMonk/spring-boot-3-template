package com.gin.springboot3template.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.gin.springboot3template.sys.base.BaseAttach;
import com.gin.springboot3template.sys.config.SystemProperties;
import com.gin.springboot3template.sys.exception.BusinessException;
import com.gin.springboot3template.sys.utils.FileUtils;
import com.gin.springboot3template.sys.utils.SpringContextUtils;
import com.gin.springboot3template.sys.utils.TimeUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.gin.springboot3template.sys.utils.FileUtils.PATH_DELIMITER;

/**
 * 附件服务: 建议重写 attachPath 方法 , 在路径中添加附件所有者的类型名.
 * 由于附件可能在其所有者创建之前上传, 因此上传接口中所有者id为可选提供;当未提供时, 创建其所有者之后应当调用 configOwnerId 方法将之前上传的附件划归给该所有者.
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/24 13:45
 */
public interface AttachmentService<T extends BaseAttach> extends MyService<T> {
    String OWNER_ID = "owner_id";
    /**
     * 附件目录名
     */
    String PATH = "attach";

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
        uw.set(OWNER_ID, ownerId).in("id", attachId);
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
     * 根据所有者id 查询附件
     * @param ownerId 所有者id
     * @return 附件
     */
    default List<T> listByOwnerId(long ownerId) {
        final QueryWrapper<T> qw = new QueryWrapper<>();
        qw.eq(OWNER_ID, ownerId);
        return list(qw);
    }

    /**
     * 根据所有者id 查询附件
     * @param ownerId 所有者id
     * @return 附件
     */
    default Map<Long, List<T>> listByOwnerId(Collection<Long> ownerId) {
        final QueryWrapper<T> qw = new QueryWrapper<>();
        qw.in(OWNER_ID, ownerId);
        return list(qw).stream().collect(Collectors.groupingBy(BaseAttach::getOwnerId));
    }

    /**
     * 上传一个附件
     * @param file   上传的文件
     * @param entity 附件对象
     * @return 附件对象
     * @throws IOException 异常
     */
    @Transactional(rollbackFor = Exception.class)
    default T upload(@NotNull MultipartFile file, T entity) throws IOException {
        writeFilePath(file, entity);
        //保存附件
        final File destFile = new File(homePath() + entity.getFilePath());
        FileUtils.mkdir(destFile.getParentFile());
        file.transferTo(destFile);
        //写入数据库
        save(entity);
        return entity;
    }

    /**
     * 生成文件名 , 写入后缀信息 / 原文件名信息 / 文件保存路径
     * @param file   上传的文件
     * @param entity 附件对象
     */
    default void writeFilePath(@NotNull MultipartFile file, T entity) {
        final String oName = file.getOriginalFilename();
        if (ObjectUtils.isEmpty(oName)) {
            throw BusinessException.of(HttpStatus.BAD_REQUEST, "必须提供原文件名");
        }
        final String ext = FileUtils.getFileExtName(oName);
        entity.setOriginalFilename(oName);
        entity.setExt(ext);

        //生成文件路径


    }

//    todo 删除附件
}
