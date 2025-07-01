package com.example.machinebackend.dto;

import com.example.machinebackend.entity.Interface;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用于API响应的DTO，包含了接口的完整层级路径信息。
 */
@Data
@NoArgsConstructor
public class InterfaceWithFullPathDTO {

    // --- 接口自身的基本字段 ---
    private Integer id;
    private Integer deviceId;
    private String name;
    private String number;
    private String description;
    private Integer targetId;
    private Boolean status;

    // --- 新增的路径信息字段 ---
    private FullPathInfoDTO fullPath;
    private FullPathInfoDTO targetFullPath; // 目标接口的完整路径

    /**
     * 从基础的Interface实体创建DTO。
     * @param entity Interface实体
     */
    public InterfaceWithFullPathDTO(Interface entity) {
        this.id = entity.getId();
        this.deviceId = entity.getDeviceId();
        this.name = entity.getName();
        this.number = entity.getNumber();
        this.description = entity.getDescription();
        this.targetId = entity.getTargetId();
        this.status = entity.getStatus();
    }

    /**
     * 内部静态类，用于表示路径结构。
     */
    @Data
    public static class FullPathInfoDTO {
        private String roomName;
        private Integer roomId;
        private String rowName;
        private Integer rowId;
        private String cabinetName;
        private Integer cabinetId;
        private String deviceName;
        private Integer deviceId;
    }
} 