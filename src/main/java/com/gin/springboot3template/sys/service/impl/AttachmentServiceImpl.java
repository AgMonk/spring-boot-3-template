package com.gin.springboot3template.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gin.springboot3template.sys.base.BaseAttach;
import com.gin.springboot3template.sys.config.SystemProperties;
import com.gin.springboot3template.sys.exception.BusinessException;
import com.gin.springboot3template.sys.service.AttachmentService;
import com.gin.springboot3template.sys.utils.FileUtils;
import com.gin.springboot3template.sys.utils.StrUtils;
import com.gin.springboot3template.sys.utils.TimeUtils;
import lombok.RequiredArgsConstructor;
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
 * 由于附件可能在其所有者创建之前上传, 因此上传接口中所有者id为可选提供;当未提供时, 创建其所有者之后应当调用 configOwnerId 方法将之前上传的附件划归给该所有者.
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/24 13:45
 */
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public abstract class AttachmentServiceImpl<M extends BaseMapper<T>, T extends BaseAttach> extends ServiceImpl<M, T> implements AttachmentService<T> {
    private static final String OWNER_ID = "owner_id";
    /**
     * 附件目录名
     */
    private static final String PATH = "attach";
    private final SystemProperties systemProperties;

    @Override
    public final void configOwnerId(long ownerId, Set<Long> attachId) {
        final UpdateWrapper<T> uw = new UpdateWrapper<>();
        uw.set(OWNER_ID, ownerId).in("id", attachId);
        update(uw);
    }

    @Override
    public final List<T> listByOwnerId(long ownerId) {
        final QueryWrapper<T> qw = new QueryWrapper<>();
        qw.eq(OWNER_ID, ownerId);
        return list(qw);
    }

    @Override
    public final Map<Long, List<T>> listByOwnerId(Collection<Long> ownerId) {
        final QueryWrapper<T> qw = new QueryWrapper<>();
        qw.in(OWNER_ID, ownerId);
        return list(qw).stream().collect(Collectors.groupingBy(BaseAttach::getOwnerId));
    }

    @Override
    public final T upload(@NotNull MultipartFile file, T entity) throws IOException {
        final String oName = file.getOriginalFilename();
        if (ObjectUtils.isEmpty(oName)) {
            throw BusinessException.of(HttpStatus.BAD_REQUEST, "必须提供原文件名");
        }
        // 写入后缀信息 / 原文件名信息 / 文件保存路径
        final String ext = FileUtils.getFileExtName(oName);
        entity.setOriginalFilename(oName);
        entity.setExt(ext);
        entity.setFilePath(attachPath() + PATH_DELIMITER + generateFileName(FileUtils.getFileMainName(oName), ext));

        //保存附件
        final File destFile = new File(homePath() + entity.getFilePath());
        FileUtils.mkdir(destFile.getParentFile());
        file.transferTo(destFile);
        //写入数据库
        save(entity);
        return entity;
    }

    /**
     * 附件在根目录下保存的目录名 默认使用 "/attach" + 当天日期
     * 建议重写 , 在路径中添加附件所有者的类型名.
     * @return 附件目录
     */
    public String attachPath() {
        return PATH_DELIMITER + PATH + PATH_DELIMITER + TimeUtils.format(TimeUtils.DATE_FORMATTER);
    }

    /**
     * 生成保存的文件名
     * @param mainName 原名的主文件名
     * @param ext      原名的后缀名
     * @return 保存的文件名
     */
    public String generateFileName(String mainName, String ext) {
        return StrUtils.uuid().substring(18) + FileUtils.DOT + ext;
    }

    /**
     * 保存附件的根目录 默认使用容器中的 SystemProperties 的 homePath
     * @return 根目录
     */
    public String homePath() {
        return systemProperties.getHomePath();
    }

//    todo 删除附件
}