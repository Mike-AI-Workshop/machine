package com.example.machinebackend.service.impl;

import com.example.machinebackend.entity.Room;
import com.example.machinebackend.mapper.RoomMapper;
import com.example.machinebackend.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * RoomServiceImpl实现类，实现机房相关的业务逻辑。
 */
@Service
public class RoomServiceImpl implements RoomService {
    /** RoomMapper依赖，用于数据库操作 */
    private final RoomMapper roomMapper;

    @Autowired
    public RoomServiceImpl(RoomMapper roomMapper) {
        this.roomMapper = roomMapper;
    }

    /**
     * 获取所有机房信息
     * @return 机房列表
     */
    @Override
    public List<Room> getAllRooms() {
        return roomMapper.selectAll();
    }

    /**
     * 根据ID获取机房详情
     * @param id 机房ID
     * @return 机房对象
     */
    @Override
    public Room getRoomById(Integer id) {
        return roomMapper.selectById(id);
    }

    /**
     * 新增机房
     * @param room 机房对象
     * @return 新增后的机房对象
     */
    @Override
    public Room addRoom(Room room) {
        roomMapper.insert(room);
        return room;
    }

    /**
     * 更新机房信息
     * @param room 机房对象
     * @return 更新后的机房对象
     */
    @Override
    public Room updateRoom(Room room) {
        roomMapper.update(room);
        return room;
    }

    /**
     * 根据ID删除机房
     * @param id 机房ID
     * @return 是否删除成功
     */
    @Override
    public boolean deleteRoomById(Integer id) {
        return roomMapper.deleteById(id) > 0;
    }
} 