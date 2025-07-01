package com.example.machinebackend.service.impl;

import com.example.machinebackend.dto.InterfaceWithFullPathDTO;
import com.example.machinebackend.entity.Interface;
import com.example.machinebackend.mapper.InterfaceMapper;
import com.example.machinebackend.service.InterfaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * InterfaceServiceImpl实现类，实现接口相关的业务逻辑。
 */
@Service
public class InterfaceServiceImpl implements InterfaceService {
    /** InterfaceMapper依赖，用于数据库操作 */
    private final InterfaceMapper interfaceMapper;

    @Autowired
    public InterfaceServiceImpl(InterfaceMapper interfaceMapper) {
        this.interfaceMapper = interfaceMapper;
    }

    /**
     * 获取指定设备下的所有接口
     * @param deviceId 设备ID
     * @return 接口列表
     */
    @Override
    public List<Interface> getInterfacesByDeviceId(Integer deviceId) {
        return interfaceMapper.selectByDeviceId(deviceId);
    }

    /**
     * 根据ID获取接口详情
     * @param id 接口ID
     * @return 接口对象
     */
    @Override
    public Interface getInterfaceById(Integer id) {
        return interfaceMapper.selectById(id);
    }

    /**
     * 新增接口
     * @param iface 接口对象
     * @return 新增后的接口对象
     */
    @Override
    public Interface addInterface(Interface iface) {
        // 确保新接口默认状态为开启
        if (iface.getStatus() == null) {
            iface.setStatus(true);
        }
        interfaceMapper.insert(iface);
        return iface;
    }

    /**
     * 更新接口信息
     * @param iface 接口对象
     * @return 更新后的接口对象
     */
    @Override
    public Interface updateInterface(Interface iface) {
        interfaceMapper.update(iface);
        return iface;
    }

    /**
     * 根据ID删除接口
     * @param id 接口ID
     * @return 是否删除成功
     */
    @Override
    public boolean deleteInterfaceById(Integer id) {
        return interfaceMapper.deleteById(id) > 0;
    }

    /**
     * 更新接口的状态
     * @param id 接口ID
     * @param status 新的状态
     * @return 更新后的接口对象
     */
    @Override
    public Interface updateStatus(Integer id, Boolean status) {
        interfaceMapper.updateStatus(id, status);
        return interfaceMapper.selectById(id);
    }

    /**
     * 根据ID获取接口及其完整的层级路径信息
     * @param id 接口ID
     * @return 包含完整路径的接口DTO
     */
    @Override
    public InterfaceWithFullPathDTO getInterfaceWithFullPath(Integer id) {
        InterfaceWithFullPathDTO mainInterfaceDTO = interfaceMapper.selectWithFullPathById(id);
        if (mainInterfaceDTO != null && mainInterfaceDTO.getTargetId() != null) {
            InterfaceWithFullPathDTO targetInterfaceDTO = interfaceMapper.selectWithFullPathById(mainInterfaceDTO.getTargetId());
            if (targetInterfaceDTO != null) {
                mainInterfaceDTO.setTargetFullPath(targetInterfaceDTO.getFullPath());
            }
        }
        return mainInterfaceDTO;
    }
} 