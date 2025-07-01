package com.example.machinebackend.service;

import com.example.machinebackend.entity.CabinetRow;
import com.example.machinebackend.mapper.CabinetRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CabinetRowService {

    @Autowired
    private CabinetRowMapper cabinetRowMapper;

    public List<CabinetRow> getRowsByRoomId(Integer roomId) {
        return cabinetRowMapper.findByRoomId(roomId);
    }

    public CabinetRow createRow(CabinetRow cabinetRow) {
        cabinetRowMapper.insert(cabinetRow);
        return cabinetRow;
    }

    public int updateRow(CabinetRow cabinetRow) {
        return cabinetRowMapper.update(cabinetRow);
    }

    public int deleteRow(Integer id) {
        return cabinetRowMapper.delete(id);
    }
} 