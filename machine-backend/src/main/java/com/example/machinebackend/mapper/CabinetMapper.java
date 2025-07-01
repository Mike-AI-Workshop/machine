package com.example.machinebackend.mapper;

import com.example.machinebackend.entity.Cabinet;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * CabinetMapper接口，负责机柜表(cabinet)的数据库操作。
 */
@Mapper
public interface CabinetMapper {
    /**
     * 查询指定机柜排下的所有机柜
     * @param rowId 机柜排ID
     * @return 机柜列表
     */
    @Select("SELECT id, room_id, row_id, name, number, column_number, image_front, image_back, description FROM cabinet WHERE row_id = #{rowId}")
    List<Cabinet> selectByRowId(@Param("rowId") Integer rowId);

    /**
     * 根据ID查询机柜详情
     * @param id 机柜ID
     * @return 机柜对象
     */
    @Select("SELECT id, room_id, row_id, name, number, column_number, image_front, image_back, description FROM cabinet WHERE id = #{id}")
    Cabinet selectById(@Param("id") Integer id);

    /**
     * 新增机柜
     * @param cabinet 机柜对象
     * @return 影响行数
     */
    @Insert("INSERT INTO cabinet(room_id, row_id, name, number, column_number, image_front, image_back, description) VALUES(#{roomId}, #{rowId}, #{name}, #{number}, #{columnNumber}, #{imageFront}, #{imageBack}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Cabinet cabinet);

    /**
     * 更新机柜信息
     * @param cabinet 机柜对象
     * @return 影响行数
     */
    @Update("UPDATE cabinet SET room_id=#{roomId}, row_id=#{rowId}, name=#{name}, number=#{number}, column_number=#{columnNumber}, image_front=#{imageFront}, image_back=#{imageBack}, description=#{description} WHERE id=#{id}")
    int update(Cabinet cabinet);

    /**
     * 根据ID删除机柜
     * @param id 机柜ID
     * @return 影响行数
     */
    @Delete("DELETE FROM cabinet WHERE id=#{id}")
    int deleteById(@Param("id") Integer id);
} 