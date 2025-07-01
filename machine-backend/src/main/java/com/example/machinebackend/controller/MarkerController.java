package com.example.machinebackend.controller;

import com.example.machinebackend.entity.Marker;
import com.example.machinebackend.service.MarkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MarkerController，提供图标标注相关的RESTful接口。
 */
@RestController
@RequestMapping("/api/markers")
public class MarkerController {
    /** MarkerService依赖，用于业务处理 */
    private final MarkerService markerService;

    @Autowired
    public MarkerController(MarkerService markerService) {
        this.markerService = markerService;
    }

    /**
     * 获取指定图片上的所有标注点
     * @param parentType 所属类型（cabinet/device）
     * @param parentId 所属ID
     * @param imageType 图片类型（front/back）
     * @return 统一返回结构，包含code和data
     */
    @GetMapping
    public Map<String, Object> getMarkersByParentAndImage(@RequestParam String parentType,
                                                         @RequestParam Integer parentId,
                                                         @RequestParam String imageType) {
        List<Marker> markers = markerService.getMarkersByParentAndImage(parentType, parentId, imageType);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("data", markers);
        return result;
    }

    /**
     * 获取单个标注点详情
     * @param id 标注点ID
     * @return 统一返回结构，包含code和data
     */
    @GetMapping("/{id}")
    public Map<String, Object> getMarkerById(@PathVariable Integer id) {
        Marker marker = markerService.getMarkerById(id);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("data", marker);
        return result;
    }

    /**
     * 新增标注点
     * @param marker 标注点对象（JSON）
     * @return 统一返回结构，包含code和data
     */
    @PostMapping
    public Map<String, Object> addMarker(@RequestBody Marker marker) {
        Marker newMarker = markerService.addMarker(marker);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("data", newMarker);
        return result;
    }

    /**
     * 修改标注点
     * @param id 标注点ID
     * @param marker 标注点对象（JSON）
     * @return 统一返回结构，包含code和data
     */
    @PutMapping("/{id}")
    public Map<String, Object> updateMarker(@PathVariable Integer id, @RequestBody Marker marker) {
        marker.setId(id);
        Marker updatedMarker = markerService.updateMarker(marker);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("data", updatedMarker);
        return result;
    }

    /**
     * 删除标注点
     * @param id 标注点ID
     * @return 统一返回结构，包含code和message
     */
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteMarker(@PathVariable Integer id) {
        boolean success = markerService.deleteMarkerById(id);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("message", success ? "删除成功" : "删除失败");
        return result;
    }
} 