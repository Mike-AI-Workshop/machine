package com.example.machinebackend.service;

import com.example.machinebackend.entity.Device;
import java.util.List;

/**
 * DeviceService接口，定义设备相关的业务操作。
 */
public interface DeviceService {
    /**
     * 获取指定机柜下的所有设备
     * @param cabinetId 机柜ID
     * @return 设备列表
     */
    List<Device> getDevicesByCabinetId(Integer cabinetId);

    /**
     * 根据ID获取设备详情
     * @param id 设备ID
     * @return 设备对象
     */
    Device getDeviceById(Integer id);

    /**
     * 新增设备
     * @param device 设备对象
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