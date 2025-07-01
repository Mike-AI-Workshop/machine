package com.example.machinebackend.mapper;

import com.example.machinebackend.entity.Room;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * RoomMapper接口，负责机房表(room)的数据库操作。
 */
@Mapper
public interface RoomMapper {
    /**
     * 查询所有机房信息
     * @return 机房列表
     */
    @Select("SELECT id, name, location, image_id, description FROM room")
    List<Room> selectAll();

    /**
     * 根据ID查询机房详情
     * @param id 机房ID
     * @return 机房对象
     */
    @Select("SELECT id, name, location, image_id, description FROM room WHERE id = #{id}")
    Room selectById(@Param("id") Integer id);

    /**
     * 新增机房
     * @param room 机房对象
     * @return 影响行数
     */
    @Insert("INSERT INTO room(name, location, image_id, description) VALUES(#{name}, #{location}, #{imageId}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Room room);

    /**
     * 更新机房信息
     * @param room 机房对象
     * @return 影响行数
     */
    @Update("UPDATE room SET name=#{name}, location=#{location}, image_id=#{imageId}, description=#{description} WHERE id=#{id}")
    int update(Room room);

    /**
     * 根据ID删除机房
     * @param id 机房ID
     * @return 影响行数
     */
    @Delete("DELETE FROM room WHERE id=#{id}")
    int deleteById(@Param("id") Integer id);
} 