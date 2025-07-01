package com.example.machinebackend.controller;

import com.example.machinebackend.config.CommonResponse;
import com.example.machinebackend.dto.CreateRowRequest;
import com.example.machinebackend.entity.CabinetRow;
import com.example.machinebackend.service.CabinetRowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rows")
public class CabinetRowController {

    @Autowired
    private CabinetRowService cabinetRowService;

    @GetMapping
    public CommonResponse<List<CabinetRow>> getRowsByRoomId(@RequestParam Integer roomId) {
        List<CabinetRow> rows = cabinetRowService.getRowsByRoomId(roomId);
        return CommonResponse.success(rows);
    }

    @PostMapping
    public CommonResponse<CabinetRow> createRow(@RequestBody CreateRowRequest request) {
        CabinetRow newRow = new CabinetRow();
        newRow.setRoomId(request.getRoomId());
        newRow.setName(request.getName());
        newRow.setDescription(request.getDescription());
        CabinetRow createdRow = cabinetRowService.createRow(newRow);
        return CommonResponse.success(createdRow);
    }

    @PutMapping("/{id}")
    public CommonResponse<Integer> updateRow(@PathVariable Integer id, @RequestBody CabinetRow cabinetRow) {
        cabinetRow.setId(id);
        int result = cabinetRowService.updateRow(cabinetRow);
        return CommonResponse.success(result);
    }

    @DeleteMapping("/{id}")
    public CommonResponse<Integer> deleteRow(@PathVariable Integer id) {
        int result = cabinetRowService.deleteRow(id);
        return CommonResponse.success(result);
    }
} 