package com.example.machinebackend.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 图片资源实体类，对应数据库表 image_resource。
 * 包含图片的基本信息，如URL、类型、上传时间等。
 */
public class ImageResource implements Serializable {
    /** 主键ID */
    private Integer id;
    /** 图片URL */
    private String url;
    /** 图片类型 */
    private String type;
    /** 上传时间 */
    private Date createdAt;

    // Getter和Setter方法
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
} 