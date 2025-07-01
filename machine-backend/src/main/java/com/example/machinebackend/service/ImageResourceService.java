package com.example.machinebackend.service;

import com.example.machinebackend.entity.ImageResource;
import java.util.List;

/**
 * ImageResourceService接口，定义图片资源相关的业务操作。
 */
public interface ImageResourceService {
    /**
     * 根据ID获取图片资源
     * @param id 图片ID
     * @return 图片资源对象
     */
    ImageResource getImageResourceById(Integer id);

    /**
     * 新增图片资源
     * @param image 图片资源对象
     * @return 新增后的图片资源对象
     */
    ImageResource addImageResource(ImageResource image);

    /**
     * 根据ID删除图片资源
     * @param id 图片ID
     * @return 是否删除成功
     */
    boolean deleteImageResourceById(Integer id);

    /**
     * 查询所有图片资源（可选，便于管理）
     * @return 图片资源列表
     */
    List<ImageResource> getAllImageResources();
} 