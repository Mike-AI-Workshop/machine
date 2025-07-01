package com.example.machinebackend.service;

import com.example.machinebackend.entity.Room;
import java.util.List;

/**
 * RoomService接口，定义机房相关的业务操作。
 */
public interface RoomService {
    /**
     * 获取所有机房信息
     * @return 机房列表
     */
    List<Room> getAllRooms();

    /**
     * 根据ID获取机房详情
     * @param id 机房ID
     * @return 机房对象
     */
    Room getRoomById(Integer id);

    /**
     * 新增机房
     * @param room 机房对象
     * @return 新增后的机房对象
     */
    Room addRoom(Room room);

    /**
     * 更新机房信息
     * @param room 机房对象
     * @return 更新后的机房对象
     */
    Room updateRoom(Room room);

    /**
     * 根据ID删除机房
     * @param id 机房ID
     * @return 是否删除成功
     */
    boolean deleteRoomById(Integer id);
} 