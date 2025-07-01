package com.example.machinebackend.controller;

import com.example.machinebackend.dto.InterfaceWithFullPathDTO;
import com.example.machinebackend.entity.Interface;
import com.example.machinebackend.service.InterfaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * InterfaceController，提供接口相关的RESTful接口。
 */
@RestController
@RequestMapping("/api/interfaces")
public class InterfaceController {
    /** InterfaceService依赖，用于业务处理 */
    private final InterfaceService interfaceService;

    @Autowired
    public InterfaceController(InterfaceService interfaceService) {
        this.interfaceService = interfaceService;
    }

    /**
     * 获取指定设备下的接口列表
     * @param deviceId 设备ID
     * @return 统一返回结构，包含code和data
     */
    @GetMapping
    public Map<String, Object> getInterfacesByDeviceId(@RequestParam Integer deviceId) {
        List<Interface> interfaces = interfaceService.getInterfacesByDeviceId(deviceId);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("data", interfaces);
        return result;
    }

    /**
     * 获取单个接口详情
     * @param id 接口ID
     * @return 统一返回结构，包含code和data
     */
    @GetMapping("/{id}")
    public Map<String, Object> getInterfaceById(@PathVariable Integer id) {
        Interface iface = interfaceService.getInterfaceById(id);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("data", iface);
        return result;
    }

    /**
     * 获取单个接口详情及其完整路径信息
     * @param id 接口ID
     * @return 统一返回结构，包含code和data(DTO)
     */
    @GetMapping("/{id}/with-full-path")
    public Map<String, Object> getInterfaceWithFullPath(@PathVariable Integer id) {
        InterfaceWithFullPathDTO ifaceDTO = interfaceService.getInterfaceWithFullPath(id);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("data", ifaceDTO);
        return result;
    }

    /**
     * 新增接口
     * @param iface 接口对象（JSON）
     * @return 统一返回结构，包含code和data
     */
    @PostMapping
    public Map<String, Object> addInterface(@RequestBody Interface iface) {
        Interface newIface = interfaceService.addInterface(iface);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("data", newIface);
        return result;
    }

    /**
     * 修改接口
     * @param id 接口ID
     * @param iface 接口对象（JSON）
     * @return 统一返回结构，包含code和data
     */
    @PutMapping("/{id}")
    public Map<String, Object> updateInterface(@PathVariable Integer id, @RequestBody Interface iface) {
        iface.setId(id);
        Interface updatedIface = interfaceService.updateInterface(iface);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("data", updatedIface);
        return result;
    }

    /**
     * 删除接口
     * @param id 接口ID
     * @return 统一返回结构，包含code和message
     */
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteInterface(@PathVariable Integer id) {
        boolean success = interfaceService.deleteInterfaceById(id);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("message", success ? "删除成功" : "删除失败");
        return result;
    }

    /**
     * 更新接口的状态
     * @param id 接口ID
     * @param payload 包含新状态的请求体, e.g. {"status": true}
     * @return 统一返回结构，包含code和data
     */
    @PutMapping("/{id}/status")
    public Map<String, Object> updateInterfaceStatus(@PathVariable Integer id, @RequestBody Map<String, Boolean> payload) {
        Boolean status = payload.get("status");
        Interface updatedIface = interfaceService.updateStatus(id, status);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("data", updatedIface);
        return result;
    }
} 