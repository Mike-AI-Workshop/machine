package com.example.machinebackend.dto;

public class CreateCabinetRequest {
    private Integer rowId;
    private String name;
    private String number;
    private Integer columnNumber;
    private Integer imageFront;
    private Integer imageBack;
    private String description;
    
    // Getters and Setters
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