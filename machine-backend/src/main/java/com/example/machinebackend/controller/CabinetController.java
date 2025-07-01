package com.example.machinebackend.controller;

import com.example.machinebackend.config.CommonResponse;
import com.example.machinebackend.dto.CreateCabinetRequest;
import com.example.machinebackend.entity.Cabinet;
import com.example.machinebackend.service.CabinetService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CabinetController，提供机柜相关的RESTful接口。
 */
@RestController
@RequestMapping("/api/cabinets")
public class CabinetController {
    /** CabinetService依赖，用于业务处理 */
    private final CabinetService cabinetService;

    @Autowired
    public CabinetController(CabinetService cabinetService) {
        this.cabinetService = cabinetService;
    }

    /**
     * 获取指定机房下的机柜列表
     * @param rowId 机房ID
     * @return 统一返回结构，包含code和data
     */
    @GetMapping
    public CommonResponse<List<Cabinet>> getCabinetsByRowId(@RequestParam Integer rowId) {
        List<Cabinet> cabinets = cabinetService.getCabinetsByRowId(rowId);
        return CommonResponse.success(cabinets);
    }

    /**
     * 获取单个机柜详情
     * @param id 机柜ID
     * @return 统一返回结构，包含code和data
     */
    @GetMapping("/{id}")
    public CommonResponse<Cabinet> getCabinetById(@PathVariable Integer id) {
        Cabinet cabinet = cabinetService.getCabinetById(id);
        return CommonResponse.success(cabinet);
    }

    /**
     * 新增机柜
     * @param request 机柜对象（JSON）
     * @return 统一返回结构，包含code和data
     */
    @PostMapping
    public CommonResponse<Cabinet> createCabinet(@RequestBody CreateCabinetRequest request) {
        Cabinet newCabinet = new Cabinet();
        BeanUtils.copyProperties(request, newCabinet);
        Cabinet createdCabinet = cabinetService.addCabinet(newCabinet);
        return CommonResponse.success(createdCabinet);
    }

    /**
     * 修改机柜
     * @param id 机柜ID
     * @param cabinet 机柜对象（JSON）
     * @return 统一返回结构，包含code和data
     */
    @PutMapping("/{id}")
    public CommonResponse<Cabinet> updateCabinet(@PathVariable Integer id, @RequestBody Cabinet cabinet) {
        cabinet.setId(id);
        Cabinet updatedCabinet = cabinetService.updateCabinet(cabinet);
        return CommonResponse.success(updatedCabinet);
    }

    /**
     * 删除机柜
     * @param id 机柜ID
     * @return 统一返回结构，包含code和message
     */
    @DeleteMapping("/{id}")
    public CommonResponse<Boolean> deleteCabinet(@PathVariable Integer id) {
        boolean result = cabinetService.deleteCabinetById(id);
        return CommonResponse.success(result);
    }
} 