package com.example.machinebackend.service;

import com.example.machinebackend.entity.Device;
import java.util.List;
import com.example.machinebackend.dto.DeviceWithFullPathDTO;

/**
 * 设备服务接口
 * 定义了设备相关的业务逻辑操作
 */
public interface DeviceService {
    /**
     * 根据机柜ID获取设备列表
     * @param cabinetId 机柜ID
     * @return 设备列表
     */
    List<Device> getDevicesByCabinetId(Integer cabinetId);

    /**
     * 根据机柜ID获取设备列表，并附带其完整的层级路径信息
     * @param cabinetId 机柜ID
     * @return 包含完整路径的设备DTO列表
     */
    List<DeviceWithFullPathDTO> getDevicesWithFullPathByCabinetId(Integer cabinetId);

    /**
     * 根据ID获取单个设备详情, 并附带其完整的层级路径信息
     * @param id 设备ID
     * @return 包含完整路径的设备DTO
     */
    DeviceWithFullPathDTO getDeviceWithFullPathById(Integer id);

    /**
     * 根据ID获取设备详情
     * @param id 设备ID
     * @return 设备对象
     */
    Device getDeviceById(Integer id);

    /**
     * 新增设备
     * @param device 设备信息
     * @return 新增后的设备对象
     */
    Device addDevice(Device device);

    /**
     * 更新设备信息
     * @param device 设备对象
     * @return 更新后的设备对象
     */
    Device updateDevice(Device device);

    /**
     * 根据ID删除设备
     * @param id 设备ID
     * @return 是否删除成功
     */
    boolean deleteDeviceById(Integer id);
} 