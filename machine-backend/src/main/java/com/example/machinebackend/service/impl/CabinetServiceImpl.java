package com.example.machinebackend.service.impl;

import com.example.machinebackend.entity.Cabinet;
import com.example.machinebackend.entity.CabinetRow;
import com.example.machinebackend.mapper.CabinetMapper;
import com.example.machinebackend.mapper.CabinetRowMapper;
import com.example.machinebackend.service.CabinetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * CabinetServiceImpl实现类，实现机柜相关的业务逻辑。
 */
@Service
public class CabinetServiceImpl implements CabinetService {
    /** CabinetMapper依赖，用于数据库操作 */
    private final CabinetMapper cabinetMapper;
    private final CabinetRowMapper cabinetRowMapper;

    @Autowired
    public CabinetServiceImpl(CabinetMapper cabinetMapper, CabinetRowMapper cabinetRowMapper) {
        this.cabinetMapper = cabinetMapper;
        this.cabinetRowMapper = cabinetRowMapper;
    }

    /**
     * 获取指定机柜排下的所有机柜
     * @param rowId 机柜排ID
     * @return 机柜列表
     */
    @Override
    public List<Cabinet> getCabinetsByRowId(Integer rowId) {
        return cabinetMapper.selectByRowId(rowId);
    }

    /**
     * 根据ID获取机柜详情
     * @param id 机柜ID
     * @return 机柜对象
     */
    @Override
    public Cabinet getCabinetById(Integer id) {
        return cabinetMapper.selectById(id);
    }

    /**
     * 新增机柜
     * 在新增机柜时，自动根据rowId补充roomId，保证数据一致性
     * @param cabinet 机柜对象
     * @return 新增后的机柜对象
     */
    @Override
    @Transactional
    public Cabinet addCabinet(Cabinet cabinet) {
        CabinetRow row = cabinetRowMapper.findById(cabinet.getRowId());
        Objects.requireNonNull(row, "Cannot find cabinet row with id: " + cabinet.getRowId());
        cabinet.setRoomId(row.getRoomId());

        cabinetMapper.insert(cabinet);
        return cabinet;
    }

    /**
     * 更新机柜信息
     * @param cabinet 机柜对象
     * @return 更新后的机柜对象
     */
    @Override
    @Transactional
    public Cabinet updateCabinet(Cabinet cabinet) {
        // 同样在更新时补充roomId，防止前端未传递
        CabinetRow row = cabinetRowMapper.findById(cabinet.getRowId());
        Objects.requireNonNull(row, "Cannot find cabinet row with id: " + cabinet.getRowId());
        cabinet.setRoomId(row.getRoomId());
        
        cabinetMapper.update(cabinet);
        return cabinet;
    }

    /**
     * 根据ID删除机柜
     * @param id 机柜ID
     * @return 是否删除成功
     */
    @Override
    public boolean deleteCabinetById(Integer id) {
        return cabinetMapper.deleteById(id) > 0;
    }
} 