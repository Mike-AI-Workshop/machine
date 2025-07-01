package com.example.machinebackend.controller;

import com.example.machinebackend.entity.Device;
import com.example.machinebackend.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DeviceController，提供设备相关的RESTful接口。
 */
@RestController
@RequestMapping("/api/devices")
public class DeviceController {
    /** DeviceService依赖，用于业务处理 */
    private final DeviceService deviceService;

    @Autowired
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    /**
     * 获取指定机柜下的设备列表
     * @param cabinetId 机柜ID
     * @return 统一返回结构，包含code和data
     */
    @GetMapping
    public Map<String, Object> getDevicesByCabinetId(@RequestParam Integer cabinetId) {
        List<Device> devices = deviceService.getDevicesByCabinetId(cabinetId);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("data", devices);
        return result;
    }

    /**
     * 获取单个设备详情
     * @param id 设备ID
     * @return 统一返回结构，包含code和data
     */
    @GetMapping("/{id}")
    public Map<String, Object> getDeviceById(@PathVariable Integer id) {
        Device device = deviceService.getDeviceById(id);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("data", device);
        return result;
    }

    /**
     * 新增设备
     * @param device 设备对象（JSON）
     * @return 统一返回结构，包含code和data
     */
    @PostMapping
    public Map<String, Object> addDevice(@RequestBody Device device) {
        Device newDevice = deviceService.addDevice(device);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("data", newDevice);
        return result;
    }

    /**
     * 修改设备
     * @param id 设备ID
     * @param device 设备对象（JSON）
     * @return 统一返回结构，包含code和data
     */
    @PutMapping("/{id}")
    public Map<String, Object> updateDevice(@PathVariable Integer id, @RequestBody Device device) {
        device.setId(id);
        Device updatedDevice = deviceService.updateDevice(device);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("data", updatedDevice);
        return result;
    }

    /**
     * 删除设备
     * @param id 设备ID
     * @return 统一返回结构，包含code和message
     */
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteDevice(@PathVariable Integer id) {
        boolean success = deviceService.deleteDeviceById(id);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("message", success ? "删除成功" : "删除失败");
        return result;
    }
} 