package com.example.machinebackend.entity;

import java.io.Serializable;
import jakarta.persistence.*;

/**
 * 机柜实体类，对应数据库表 cabinet。
 * 包含机柜的基本信息，如名称、编号、排数、列数、图片、介绍等。
 */
@Entity
@Table(name = "cabinet")
public class Cabinet implements Serializable {
    /** 主键ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /** 机房ID，关联room表 */
    @Column(name = "room_id", nullable = false)
    private Integer roomId;
    /** 机柜所在排数 */
    @Column(name = "row_id", nullable = false)
    private Integer rowId;
    /** 机柜名称 */
    @Column(nullable = false)
    private String name;
    /** 机柜编号（机房下唯一） */
    @Column(nullable = false)
    private String number;
    /** 机柜所在列数 */
    @Column(name = "column_number")
    private Integer columnNumber;
    /** 正面图ID，关联image_resource表 */
    @Column(name = "image_front")
    private Integer imageFront;
    /** 背面图ID，关联image_resource表 */
    @Column(name = "image_back")
    private Integer imageBack;
    /** 机柜介绍 */
    private String description;

    // Getter和Setter方法
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getRoomId() { return roomId; }
    public void setRoomId(Integer roomId) { this.roomId = roomId; }
    public Integer getRowId() { return rowId; }
    public void setRowId(Integer rowId) { this.rowId = rowId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }
    public Integer getColumnNumber() { return columnNumber; }
    public void setColumnNumber(Integer columnNumber) { this.columnNumber = columnNumber; }
    public Integer getImageFront() { return imageFront; }
    public void setImageFront(Integer imageFront) { this.imageFront = imageFront; }
    public Integer getImageBack() { return imageBack; }
    public void setImageBack(Integer imageBack) { this.imageBack = imageBack; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
} 