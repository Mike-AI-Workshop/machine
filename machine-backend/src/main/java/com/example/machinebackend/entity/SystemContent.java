package com.example.machinebackend.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统动态内容实体类，对应数据库表 system_content。
 */
public class SystemContent implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Integer id;
    /** 内容唯一键 */
    private String contentKey;
    /** 内容值 */
    private String contentValue;
    /** 内容描述 */
    private String description;
    /** 创建时间 */
    private Date createdAt;
    /** 更新时间 */
    private Date updatedAt;

    // Getter and Setter methods
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContentKey() {
        return contentKey;
    }

    public void setContentKey(String contentKey) {
        this.contentKey = contentKey;
    }

    public String getContentValue() {
        return contentValue;
    }

    public void setContentValue(String contentValue) {
        this.contentValue = contentValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
} 