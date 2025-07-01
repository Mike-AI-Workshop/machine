package com.example.machinebackend.entity;

import java.io.Serializable;

/**
 * 接口实体类，对应数据库表 interface。
 * 包含接口的基本信息，如名称、编号、描述、指向等。
 */
public class Interface implements Serializable {
    /** 主键ID */
    private Integer id;
    /** 设备ID，关联device表 */
    private Integer deviceId;
    /** 接口名称 */
    private String name;
    /** 接口编号（设备下唯一） */
    private String number;
    /** 接口描述 */
    private String description;
    /** 指向接口ID（可空，关联本表id） */
    private Integer targetId;
    /** 接口状态: 1=开启, 0=关闭 */
    private Boolean status;

    // Getter和Setter方法
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getDeviceId() { return deviceId; }
    public void setDeviceId(Integer deviceId) { this.deviceId = deviceId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getTargetId() { return targetId; }
    public void setTargetId(Integer targetId) { this.targetId = targetId; }
    public Boolean getStatus() { return status; }
    public void setStatus(Boolean status) { this.status = status; }
} 