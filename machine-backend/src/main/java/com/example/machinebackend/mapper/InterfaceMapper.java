package com.example.machinebackend.mapper;

import com.example.machinebackend.dto.InterfaceWithFullPathDTO;
import com.example.machinebackend.entity.Interface;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * InterfaceMapper接口，负责接口表(interface)的数据库操作。
 * SQL语句定义在 resources/mapper/InterfaceMapper.xml 中。
 */
@Mapper
public interface InterfaceMapper {
    /**
     * 查询指定设备下的所有接口
     * @param deviceId 设备ID
     * @return 接口列表
     */
    List<Interface> selectByDeviceId(@Param("deviceId") Integer deviceId);

    /**
     * 根据ID查询接口详情
     * @param id 接口ID
     * @return 接口对象
     */
    Interface selectById(@Param("id") Integer id);

    /**
     * 新增接口
     * @param iface 接口对象
     * @return 影响行数
     */
    int insert(Interface iface);

    /**
     * 更新接口信息
     * @param iface 接口对象
     * @return 影响行数
     */
    int update(Interface iface);

    /**
     * 根据ID删除接口
     * @param id 接口ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Integer id);

    /**
     * 更新指定ID接口的状态
     * @param id 接口ID
     * @param status 新的状态
     * @return 影响行数
     */
    int updateStatus(@Param("id") Integer id, @Param("status") Boolean status);

    /**
     * 根据ID查询接口及其完整的层级路径信息
     * @param id 接口ID
     * @return 包含完整路径的接口DTO
     */
    InterfaceWithFullPathDTO selectWithFullPathById(@Param("id") Integer id);
} 