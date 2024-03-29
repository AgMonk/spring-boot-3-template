package com.gin.springboot3template.sys.service;

import com.gin.springboot3template.sys.base.BaseAttach;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/26 15:37
 */
public interface AttachmentService<T extends BaseAttach> extends MyService<T> {
    /**
     * 配置附件的持有者id
     * @param ownerId  持有者id
     * @param attachId 附件id
     */
    void configOwnerId(long ownerId, Set<Long> attachId);

    /**
     * 根据所有者id 查询附件
     * @param ownerId 所有者id
     * @return 附件
     */
    List<T> listByOwnerId(long ownerId);

    /**
     * 根据所有者id 查询附件
     * @param ownerId 所有者id
     * @return 附件
     */
    Map<Long, List<T>> listByOwnerId(Collection<Long> ownerId);

    /**
     * 上传一个附件
     * @param file   上传的文件
     * @param entity 附件对象
     * @return 附件对象
     * @throws IOException 异常
     */
    T upload(@NotNull MultipartFile file, T entity) throws IOException;

    /**
     * 删除附件
     * @param attachments 附件实体
     * @return 被删除的附件
     */
    List<T> deleteEntities(List<T> attachments);

    /**
     * 校验上传文件
     * @param file 上传的文件
     */
    void validateMultipartFile(@NotNull MultipartFile file);

    /**
     * 允许的文件ContentType 应当在接收文件之前进行校验
     * @return ContentType 列表
     */
    default List<String> acceptContentType() {
        return null;
    }
}
