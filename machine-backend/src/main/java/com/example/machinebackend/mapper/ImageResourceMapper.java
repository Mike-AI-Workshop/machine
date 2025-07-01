package com.example.machinebackend.mapper;

import com.example.machinebackend.entity.ImageResource;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * ImageResourceMapper接口，负责图片资源表(image_resource)的数据库操作。
 */
@Mapper
public interface ImageResourceMapper {
    /**
     * 根据ID查询图片资源
     * @param id 图片ID
     * @return 图片资源对象
     */
    @Select("SELECT id, url, type, created_at FROM image_resource WHERE id = #{id}")
    ImageResource selectById(@Param("id") Integer id);

    /**
     * 新增图片资源
     * @param image 图片资源对象
     * @return 影响行数
     */
    @Insert("INSERT INTO image_resource(url, type, created_at) VALUES(#{url}, #{type}, #{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ImageResource image);

    /**
     * 根据ID删除图片资源
     * @param id 图片ID
     * @return 影响行数
     */
    @Delete("DELETE FROM image_resource WHERE id=#{id}")
    int deleteById(@Param("id") Integer id);

    /**
     * 查询所有图片资源（可选，便于管理）
     * @return 图片资源列表
     */
    @Select("SELECT id, url, type, created_at FROM image_resource")
    List<ImageResource> selectAll();
} 