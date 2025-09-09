package com.example.machinebackend.controller;

import com.example.machinebackend.config.CommonResponse;
import com.example.machinebackend.dto.DeviceWithFullPathDTO;
import com.example.machinebackend.entity.Device;
import com.example.machinebackend.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * DeviceController，提供设备相关的RESTful接口。
 * 所有接口统一使用CommonResponse作为标准返回结构。
 */
@RestController
@RequestMapping("/api/devices")
public class DeviceController {

    private final DeviceService deviceService;

    @Autowired
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    /**
     * 根据机柜ID获取设备列表。
     * @param cabinetId 机柜ID
     * @return 包含设备及其路径信息的列表
     */
    @GetMapping
    public CommonResponse<List<DeviceWithFullPathDTO>> getDevices(@RequestParam Integer cabinetId) {
        List<DeviceWithFullPathDTO> devices = deviceService.getDevicesWithFullPathByCabinetId(cabinetId);
        return CommonResponse.success(devices);
    }

    /**
     * 获取单个设备详情
     * @param id 设备ID
     * @return 单个设备信息
     */
    @GetMapping("/{id}")
    public CommonResponse<Device> getDeviceById(@PathVariable Integer id) {
        Device device = deviceService.getDeviceById(id);
        return CommonResponse.success(device);
    }

    /**
     * 获取单个设备详情, 包含完整路径
     * @param id 设备ID
     * @return 单个设备信息 DTO
     */
    @GetMapping("/{id}/with-full-path")
    public CommonResponse<DeviceWithFullPathDTO> getDeviceWithFullPathById(@PathVariable Integer id) {
        DeviceWithFullPathDTO device = deviceService.getDeviceWithFullPathById(id);
        return CommonResponse.success(device);
    }

    /**
     * 新增设备
     * @param device 设备对象（JSON）
     * @return 新增后的设备信息
     */
    @PostMapping
    public CommonResponse<Device> addDevice(@RequestBody Device device) {
        Device newDevice = deviceService.addDevice(device);
        return CommonResponse.success(newDevice);
    }

    /**
     * 修改设备
     * @param id 设备ID
     * @param device 设备对象（JSON）
     * @return 修改后的设备信息
     */
    @PutMapping("/{id}")
    public CommonResponse<Device> updateDevice(@PathVariable Integer id, @RequestBody Device device) {
        device.setId(id);
        Device updatedDevice = deviceService.updateDevice(device);
        return CommonResponse.success(updatedDevice);
    }

    /**
     * 删除设备
     * @param id 设备ID
     * @return 操作成功信息
     */
    @DeleteMapping("/{id}")
    public CommonResponse<String> deleteDevice(@PathVariable Integer id) {
        deviceService.deleteDeviceById(id);
        return CommonResponse.success("删除成功");
    }
} 