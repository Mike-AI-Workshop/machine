package com.example.machinebackend.controller;

import com.example.machinebackend.entity.Room;
import com.example.machinebackend.entity.ImageResource;
import com.example.machinebackend.service.RoomService;
import com.example.machinebackend.service.ImageResourceService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * RoomController，提供机房相关的RESTful接口。
 */
@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    /** RoomService依赖，用于业务处理 */
    private final RoomService roomService;
    private final ImageResourceService imageResourceService;

    @Autowired
    public RoomController(RoomService roomService, ImageResourceService imageResourceService) {
        this.roomService = roomService;
        this.imageResourceService = imageResourceService;
    }

    private String getBaseUrl(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    }

    /**
     * 获取全部机房列表
     * @return 统一返回结构，包含code和data
     */
    @GetMapping
    public Map<String, Object> getAllRooms(HttpServletRequest request) {
        List<Room> rooms = roomService.getAllRooms();
        String baseUrl = getBaseUrl(request);

        // 用Map包装每个Room，带imageUrl
        List<Map<String, Object>> roomList = rooms.stream().map(room -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", room.getId());
            map.put("name", room.getName());
            map.put("location", room.getLocation());
            map.put("imageId", room.getImageId());
            map.put("description", room.getDescription());
            if (room.getImageId() != null) {
                ImageResource img = imageResourceService.getImageResourceById(room.getImageId());
                map.put("imageUrl", img != null ? baseUrl + img.getUrl() : null);
            } else {
                map.put("imageUrl", null);
            }
            return map;
        }).collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("data", roomList);
        return result;
    }

    /**
     * 获取单个机房详情
     * @param id 机房ID
     * @return 统一返回结构，包含code和data
     */
    @GetMapping("/{id}")
    public Map<String, Object> getRoomById(@PathVariable Integer id, HttpServletRequest request) {
        Room room = roomService.getRoomById(id);
        String baseUrl = getBaseUrl(request);

        Map<String, Object> map = new java.util.HashMap<>();
        map.put("id", room.getId());
        map.put("name", room.getName());
        map.put("location", room.getLocation());
        map.put("imageId", room.getImageId());
        map.put("description", room.getDescription());
        if (room.getImageId() != null) {
            ImageResource img = imageResourceService.getImageResourceById(room.getImageId());
            map.put("imageUrl", img != null ? baseUrl + img.getUrl() : null);
        } else {
            map.put("imageUrl", null);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("data", map);
        return result;
    }

    /**
     * 新增机房
     * @param room 机房对象（JSON）
     * @return 统一返回结构，包含code和data
     */
    @PostMapping
    public Map<String, Object> addRoom(@RequestBody Room room) {
        Room newRoom = roomService.addRoom(room);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("data", newRoom);
        return result;
    }

    /**
     * 修改机房
     * @param id 机房ID
     * @param room 机房对象（JSON）
     * @return 统一返回结构，包含code和data
     */
    @PutMapping("/{id}")
    public Map<String, Object> updateRoom(@PathVariable Integer id, @RequestBody Room room) {
        room.setId(id);
        Room updatedRoom = roomService.updateRoom(room);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("data", updatedRoom);
        return result;
    }

    /**
     * 删除机房
     * @param id 机房ID
     * @return 统一返回结构，包含code和message
     */
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteRoom(@PathVariable Integer id) {
        boolean success = roomService.deleteRoomById(id);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("message", success ? "删除成功" : "删除失败");
        return result;
    }
} 