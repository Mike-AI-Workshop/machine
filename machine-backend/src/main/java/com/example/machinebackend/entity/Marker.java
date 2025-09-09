package com.example.machinebackend.entity;

import java.io.Serializable;

/**
 * 布局标注实体类，对应数据库表 marker。
 * 包含标注点的基本信息，如位置、尺寸、关联对象等。
 */
public class Marker implements Serializable {
    /** 主键ID */
    private Integer id;
    /** 所属类型（cabinet/device） */
    private String parentType;
    /** 所属ID */
    private Integer parentId;
    /** 图片类型（front/back） */
    private String imageType;
    /** X坐标（百分比） */
    private Float x;
    /** Y坐标（百分比） */
    private Float y;
    /** 关联类型（device/interface） */
    private String refType;
    /** 关联设备/接口ID */
    private Integer refId;
    /** 备注 */
    private String info;
    /** 宽度（占父容器百分比） */
    private Float width;
    /** 高度（占父容器百分比） */
    private Float height;

    // Getter和Setter方法
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getParentType() { return parentType; }
    public void setParentType(String parentType) { this.parentType = parentType; }
    public Integer getParentId() { return parentId; }
    public void setParentId(Integer parentId) { this.parentId = parentId; }
    public String getImageType() { return imageType; }
    public void setImageType(String imageType) { this.imageType = imageType; }
    public Float getX() { return x; }
    public void setX(Float x) { this.x = x; }
    public Float getY() { return y; }
    public void setY(Float y) { this.y = y; }
    public String getRefType() { return refType; }
    public void setRefType(String refType) { this.refType = refType; }
    public Integer getRefId() { return refId; }
    public void setRefId(Integer refId) { this.refId = refId; }
    public String getInfo() { return info; }
    public void setInfo(String info) { this.info = info; }
    public Float getWidth() { return width; }
    public void setWidth(Float width) { this.width = width; }
    public Float getHeight() { return height; }
    public void setHeight(Float height) { this.height = height; }
} 