package com.example.machinebackend.service.impl;

import com.example.machinebackend.entity.Device;
import com.example.machinebackend.mapper.DeviceMapper;
import com.example.machinebackend.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * DeviceServiceImpl实现类，实现设备相关的业务逻辑。
 */
@Service
public class DeviceServiceImpl implements DeviceService {
    /** DeviceMapper依赖，用于数据库操作 */
    private final DeviceMapper deviceMapper;

    @Autowired
    public DeviceServiceImpl(DeviceMapper deviceMapper) {
        this.deviceMapper = deviceMapper;
    }

    /**
     * 获取指定机柜下的所有设备
     * @param cabinetId 机柜ID
     * @return 设备列表
     */
    @Override
    public List<Device> getDevicesByCabinetId(Integer cabinetId) {
        return deviceMapper.selectByCabinetId(cabinetId);
    }

    /**
     * 根据ID获取设备详情
     * @param id 设备ID
     * @return 设备对象
     */
    @Override
    public Device getDeviceById(Integer id) {
        return deviceMapper.selectById(id);
    }

    /**
     * 新增设备
     * @param device 设备对象
     * @return 新增后的设备对象
     */
    @Override
    public Device addDevice(Device device) {
        deviceMapper.insert(device);
        return device;
    }

    /**
     * 更新设备信息
     * @param device 设备对象
     * @return 更新后的设备对象
     */
    @Override
    public Device updateDevice(Device device) {
        deviceMapper.update(device);
        return device;
    }

    /**
     * 根据ID删除设备
     * @param id 设备ID
     * @return 是否删除成功
     */
    @Override
    public boolean deleteDeviceById(Integer id) {
        return deviceMapper.deleteById(id) > 0;
    }
} 