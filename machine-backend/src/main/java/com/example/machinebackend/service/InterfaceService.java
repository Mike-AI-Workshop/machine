package com.example.machinebackend.service;

import com.example.machinebackend.dto.InterfaceWithFullPathDTO;
import com.example.machinebackend.entity.Interface;
import java.util.List;

/**
 * InterfaceService接口，定义接口相关的业务操作。
 */
public interface InterfaceService {
    /**
     * 获取指定设备下的所有接口
     * @param deviceId 设备ID
     * @return 接口列表
     */
    List<Interface> getInterfacesByDeviceId(Integer deviceId);

    /**
     * 根据ID获取接口详情
     * @param id 接口ID
     * @return 接口对象
     */
    Interface getInterfaceById(Integer id);

    /**
     * 新增接口
     * @param iface 接口对象
     * @return 新增后的接口对象
     */
    Interface addInterface(Interface iface);

    /**
     * 更新接口信息
     * @param iface 接口对象
     * @return 更新后的接口对象
     */
    Interface updateInterface(Interface iface);

    /**
     * 根据ID删除接口
     * @param id 接口ID
     * @return 是否删除成功
     */
    boolean deleteInterfaceById(Integer id);

    /**
     * 更新接口的状态
     * @param id 接口ID
     * @param status 新的状态
     * @return 更新后的接口对象
     */
    Interface updateStatus(Integer id, Boolean status);

    /**
     * 根据ID获取接口及其完整的层级路径信息
     * @param id 接口ID
     * @return 包含完整路径的接口DTO
     */
    InterfaceWithFullPathDTO getInterfaceWithFullPath(Integer id);
} 