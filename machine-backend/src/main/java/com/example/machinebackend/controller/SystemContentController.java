package com.example.machinebackend.controller;

import com.example.machinebackend.service.SystemContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/content")
public class SystemContentController {

    private final SystemContentService systemContentService;

    @Autowired
    public SystemContentController(SystemContentService systemContentService) {
        this.systemContentService = systemContentService;
    }

    /**
     * 获取首页内容
     * @return 统一返回结构，包含code和data
     */
    @GetMapping("/home")
    public Map<String, Object> getHomeContent() {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> homeContent = systemContentService.getHomeContentAsMap();
            result.put("code", 0);
            result.put("data", homeContent);
        } catch (Exception e) {
            result.put("code", 1);
            result.put("message", "获取内容失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 更新首页内容 (仅限管理员)
     * @param contentData 包含键值对的Map
     * @return 统一返回结构
     */
    @PutMapping("/home")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> updateHomeContent(@RequestBody Map<String, String> contentData) {
        Map<String, Object> result = new HashMap<>();
        try {
            // Service层需要一个方法来处理这种平面的map
            // 比如 updateHomeContent(contentData);
            systemContentService.updateHomeContent(contentData);
            result.put("code", 0);
            result.put("message", "更新成功");
        } catch (Exception e) {
            result.put("code", 1);
            result.put("message", "更新失败: " + e.getMessage());
        }
        return result;
    }
} 