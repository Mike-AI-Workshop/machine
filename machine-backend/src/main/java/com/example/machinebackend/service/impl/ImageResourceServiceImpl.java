package com.example.machinebackend.service.impl;

import com.example.machinebackend.entity.ImageResource;
import com.example.machinebackend.mapper.ImageResourceMapper;
import com.example.machinebackend.service.ImageResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ImageResourceServiceImpl实现类，实现图片资源相关的业务逻辑。
 */
@Service
public class ImageResourceServiceImpl implements ImageResourceService {
    /** ImageResourceMapper依赖，用于数据库操作 */
    private final ImageResourceMapper imageResourceMapper;

    @Autowired
    public ImageResourceServiceImpl(ImageResourceMapper imageResourceMapper) {
        this.imageResourceMapper = imageResourceMapper;
    }

    /**
     * 根据ID获取图片资源
     * @param id 图片ID
     * @return 图片资源对象
     */
    @Override
    public ImageResource getImageResourceById(Integer id) {
        return imageResourceMapper.selectById(id);
    }

    /**
     * 新增图片资源
     * @param image 图片资源对象
     * @return 新增后的图片资源对象
     */
    @Override
    public ImageResource addImageResource(ImageResource image) {
        imageResourceMapper.insert(image);
        return image;
    }

    /**
     * 根据ID删除图片资源
     * @param id 图片ID
     * @return 是否删除成功
     */
    @Override
    public boolean deleteImageResourceById(Integer id) {
        return imageResourceMapper.deleteById(id) > 0;
    }

    /**
     * 查询所有图片资源
     * @return 图片资源列表
     */
    @Override
    public List<ImageResource> getAllImageResources() {
        return imageResourceMapper.selectAll();
    }
} 