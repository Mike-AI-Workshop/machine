package com.example.machinebackend.service.impl;

import com.example.machinebackend.dto.InterfaceWithFullPathDTO;
import com.example.machinebackend.entity.Interface;
import com.example.machinebackend.mapper.InterfaceMapper;
import com.example.machinebackend.service.InterfaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

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
     * 新增接口, 并处理双向绑定
     * @param iface 接口对象
     * @return 新增后的接口对象
     */
    @Override
    @Transactional
    public Interface addInterface(Interface iface) {
        // 1. 确保新接口默认状态为开启
        if (iface.getStatus() == null) {
            iface.setStatus(true);
        }
        // 2. 插入新接口A以获取ID
        interfaceMapper.insert(iface);

        Integer targetId = iface.getTargetId();
        // 3. 如果指定了目标接口B, 则处理双向绑定
        if (targetId != null) {
            Interface targetInterface = interfaceMapper.selectById(targetId);
            if (targetInterface != null) {
                // 3.1 如果B已经连接了其他接口C, 先将C解绑
                Integer oldTargetOfTargetId = targetInterface.getTargetId();
                if (oldTargetOfTargetId != null) {
                    Interface oldTargetOfTarget = interfaceMapper.selectById(oldTargetOfTargetId);
                    if (oldTargetOfTarget != null) {
                        oldTargetOfTarget.setTargetId(null);
                        interfaceMapper.update(oldTargetOfTarget);
                    }
                }
                // 3.2 将B的目标指向新接口A
                targetInterface.setTargetId(iface.getId());
                interfaceMapper.update(targetInterface);
            }
        }
        return iface;
    }

    /**
     * 更新接口信息, 并处理双向绑定
     * @param iface 接口对象
     * @return 更新后的接口对象
     */
    @Override
    @Transactional
    public Interface updateInterface(Interface iface) {
        Integer currentId = iface.getId();
        Integer newTargetId = iface.getTargetId();

        // 1. 获取更新前的接口A状态
        Interface beforeUpdate = interfaceMapper.selectById(currentId);
        Integer oldTargetId = beforeUpdate.getTargetId();

        // 2. 如果A的新旧目标不同，需要解绑旧目标C
        if (!Objects.equals(newTargetId, oldTargetId) && oldTargetId != null) {
            Interface oldTarget = interfaceMapper.selectById(oldTargetId);
            if (oldTarget != null) {
                oldTarget.setTargetId(null);
                interfaceMapper.update(oldTarget);
            }
        }

        // 3. 如果A的新目标B存在, 则处理B的绑定
        if (newTargetId != null) {
            Interface newTarget = interfaceMapper.selectById(newTargetId);
            if (newTarget != null) {
                Integer oldTargetOfNewTargetId = newTarget.getTargetId();
                // 3.1 如果B已经连接了其他接口D, 并且D不是A, 则解绑D
                if (oldTargetOfNewTargetId != null && !oldTargetOfNewTargetId.equals(currentId)) {
                    Interface oldTargetOfNewTarget = interfaceMapper.selectById(oldTargetOfNewTargetId);
                    if (oldTargetOfNewTarget != null) {
                        oldTargetOfNewTarget.setTargetId(null);
                        interfaceMapper.update(oldTargetOfNewTarget);
                    }
                }
                // 3.2 将B的目标指向A
                newTarget.setTargetId(currentId);
                interfaceMapper.update(newTarget);
            }
        }

        // 4. 最后更新接口A本身
        interfaceMapper.update(iface);
        return iface;
    }

    /**
     * 根据ID删除接口, 并解除其伙伴的绑定
     * @param id 接口ID
     * @return 是否删除成功
     */
    @Override
    @Transactional
    public boolean deleteInterfaceById(Integer id) {
        // 1. 获取待删除的接口A
        Interface toDelete = interfaceMapper.selectById(id);
        if (toDelete == null) {
            return false; // 接口不存在
        }

        // 2. 如果A连接了接口B, 则将B解绑
        Integer targetId = toDelete.getTargetId();
        if (targetId != null) {
            Interface target = interfaceMapper.selectById(targetId);
            if (target != null) {
                target.setTargetId(null);
                interfaceMapper.update(target);
            }
        }
        
        // 3. 删除接口A
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