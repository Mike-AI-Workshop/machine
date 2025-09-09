package com.example.machinebackend.mapper;

import com.example.machinebackend.dto.DeviceWithFullPathDTO;
import com.example.machinebackend.entity.Device;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * DeviceMapper接口，定义了与设备相关的数据库操作。
 * 包含基础的CRUD以及自定义的查询方法。
 */
@Mapper
public interface DeviceMapper {

    /**
     * 根据机柜ID查询设备列表。
     * @param cabinetId 机柜ID
     * @return 设备列表
     */
    @Select("SELECT * FROM device WHERE cabinet_id = #{cabinetId}")
    List<Device> findByCabinetId(Integer cabinetId);

    /**
     * 插入一个新的设备。
     * @param device 设备实体
     * @return 影响的行数
     */
    @Insert("INSERT INTO device(cabinet_id, name, number, image_front, image_back, description) VALUES(#{cabinetId}, #{name}, #{number}, #{imageFront}, #{imageBack}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Device device);

    /**
     * 更新一个已有的设备。
     * @param device 设备实体
     * @return 影响的行数
     */
    @Update("UPDATE device SET cabinet_id=#{cabinetId}, name=#{name}, number=#{number}, image_front=#{imageFront}, image_back=#{imageBack}, description=#{description} WHERE id=#{id}")
    int update(Device device);

    /**
     * 根据ID删除一个设备。
     * @param id 设备ID
     * @return 影响的行数
     */
    @Delete("DELETE FROM device WHERE id = #{id}")
    int deleteById(Integer id);

    /**
     * 根据ID查询一个设备。
     * @param id 设备ID
     * @return 设备实体
     */
    @Select("SELECT * FROM device WHERE id = #{id}")
    Device findById(Integer id);

    /**
     * 根据机柜ID查询设备列表，并包含完整的层级路径信息。
     * 这个方法的具体SQL实现在 `resources/mapper/DeviceMapper.xml` 文件中定义。
     * @param cabinetId 机柜ID
     * @return 包含完整路径信息的设备DTO列表
     */
    List<DeviceWithFullPathDTO> selectDevicesWithFullPathByCabinetId(Integer cabinetId);

    /**
     * 根据ID查询单个设备，并包含完整的层级路径信息。
     * 这个方法的具体SQL实现在 `resources/mapper/DeviceMapper.xml` 文件中定义。
     * @param id 设备ID
     * @return 包含完整路径信息的单个设备DTO
     */
    DeviceWithFullPathDTO selectDeviceWithFullPathById(Integer id);
} 