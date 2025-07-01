package com.example.machinebackend.mapper;

import com.example.machinebackend.entity.Device;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * DeviceMapper接口，负责设备表(device)的数据库操作。
 */
@Mapper
public interface DeviceMapper {
    /**
     * 查询指定机柜下的所有设备
     * @param cabinetId 机柜ID
     * @return 设备列表
     */
    @Select("SELECT id, cabinet_id, name, number, image_front, image_back, description FROM device WHERE cabinet_id = #{cabinetId}")
    List<Device> selectByCabinetId(@Param("cabinetId") Integer cabinetId);

    /**
     * 根据ID查询设备详情
     * @param id 设备ID
     * @return 设备对象
     */
    @Select("SELECT id, cabinet_id, name, number, image_front, image_back, description FROM device WHERE id = #{id}")
    Device selectById(@Param("id") Integer id);

    /**
     * 新增设备
     * @param device 设备对象
     * @return 影响行数
     */
    @Insert("INSERT INTO device(cabinet_id, name, number, image_front, image_back, description) VALUES(#{cabinetId}, #{name}, #{number}, #{imageFront}, #{imageBack}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Device device);

    /**
     * 更新设备信息
     * @param device 设备对象
     * @return 影响行数
     */
    @Update("UPDATE device SET cabinet_id=#{cabinetId}, name=#{name}, number=#{number}, image_front=#{imageFront}, image_back=#{imageBack}, description=#{description} WHERE id=#{id}")
    int update(Device device);

    /**
     * 根据ID删除设备
     * @param id 设备ID
     * @return 影响行数
     */
    @Delete("DELETE FROM device WHERE id=#{id}")
    int deleteById(@Param("id") Integer id);
} 