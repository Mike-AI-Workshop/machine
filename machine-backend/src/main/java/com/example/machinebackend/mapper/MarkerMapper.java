package com.example.machinebackend.mapper;

import com.example.machinebackend.entity.Marker;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * MarkerMapper接口，负责图标标注表(marker)的数据库操作。
 */
@Mapper
public interface MarkerMapper {
    /**
     * 查询指定图片上的所有标注点
     * @param parentType 所属类型（cabinet/device）
     * @param parentId 所属ID
     * @param imageType 图片类型（front/back）
     * @return 标注点列表
     */
    @Select("SELECT id, parent_type, parent_id, image_type, x, y, ref_type, ref_id, info, icon FROM marker WHERE parent_type = #{parentType} AND parent_id = #{parentId} AND image_type = #{imageType}")
    List<Marker> selectByParentAndImage(@Param("parentType") String parentType, @Param("parentId") Integer parentId, @Param("imageType") String imageType);

    /**
     * 根据ID查询标注点详情
     * @param id 标注点ID
     * @return 标注点对象
     */
    @Select("SELECT id, parent_type, parent_id, image_type, x, y, ref_type, ref_id, info, icon FROM marker WHERE id = #{id}")
    Marker selectById(@Param("id") Integer id);

    /**
     * 新增标注点
     * @param marker 标注点对象
     * @return 影响行数
     */
    @Insert("INSERT INTO marker(parent_type, parent_id, image_type, x, y, ref_type, ref_id, info, icon) VALUES(#{parentType}, #{parentId}, #{imageType}, #{x}, #{y}, #{refType}, #{refId}, #{info}, #{icon})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Marker marker);

    /**
     * 更新标注点信息
     * @param marker 标注点对象
     * @return 影响行数
     */
    @Update("UPDATE marker SET parent_type=#{parentType}, parent_id=#{parentId}, image_type=#{imageType}, x=#{x}, y=#{y}, ref_type=#{refType}, ref_id=#{refId}, info=#{info}, icon=#{icon} WHERE id=#{id}")
    int update(Marker marker);

    /**
     * 根据ID删除标注点
     * @param id 标注点ID
     * @return 影响行数
     */
    @Delete("DELETE FROM marker WHERE id=#{id}")
    int deleteById(@Param("id") Integer id);
} 