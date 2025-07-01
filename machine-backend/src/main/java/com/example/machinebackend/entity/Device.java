package com.example.machinebackend.entity;

import java.io.Serializable;

/**
 * 设备实体类，对应数据库表 device。
 * 包含设备的基本信息，如名称、编号、图片、介绍等。
 */
public class Device implements Serializable {
    /** 主键ID */
    private Integer id;
    /** 机柜ID，关联cabinet表 */
    private Integer cabinetId;
    /** 设备名称 */
    private String name;
    /** 设备编号（机柜下唯一） */
    private String number;
    /** 正面图ID，关联image_resource表 */
    private Integer imageFront;
    /** 背面图ID，关联image_resource表 */
    private Integer imageBack;
    /** 设备介绍 */
    private String description;

    // Getter和Setter方法
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getCabinetId() { return cabinetId; }
    public void setCabinetId(Integer cabinetId) { this.cabinetId = cabinetId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }
    public Integer getImageFront() { return imageFront; }
    public void setImageFront(Integer imageFront) { this.imageFront = imageFront; }
    public Integer getImageBack() { return imageBack; }
    public void setImageBack(Integer imageBack) { this.imageBack = imageBack; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
} 