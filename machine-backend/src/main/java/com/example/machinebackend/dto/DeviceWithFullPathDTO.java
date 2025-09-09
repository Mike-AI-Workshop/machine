package com.example.machinebackend.dto;

import com.example.machinebackend.entity.Device;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * DeviceWithFullPathDTO (数据传输对象)
 *
 * @author: phil
 * @date: 2024-06-03
 *
 * 继承自Device实体，并额外包含了设备的完整层级路径信息（机房、机柜排、机柜名称）。
 * 这个DTO专门用于向前端返回设备列表，以便在UI上可以方便地展示每个设备的位置。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DeviceWithFullPathDTO extends Device {

    /**
     * 机柜名称
     */
    private String cabinetName;

    /**
     * 机柜排名称
     */
    private String rowName;

    /**
     * 机房名称
     */
    private String roomName;
} 