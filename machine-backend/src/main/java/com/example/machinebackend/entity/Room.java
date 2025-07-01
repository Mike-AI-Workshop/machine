package com.example.machinebackend.entity;

import java.io.Serializable;

/**
 * 机房实体类，对应数据库表 room。
 * 包含机房的基本信息，如名称、位置、图片、介绍等。
 */
public class Room implements Serializable {
    /** 主键ID */
    private Integer id;
    /** 机房名称 */
    private String name;
    /** 机房位置 */
    private String location;
    /** 图片资源ID，关联image_resource表 */
    private Integer imageId;
    /** 机房介绍 */
    private String description;

    // Getter和Setter方法

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
} 