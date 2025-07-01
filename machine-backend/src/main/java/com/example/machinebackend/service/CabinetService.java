package com.example.machinebackend.service;

import com.example.machinebackend.entity.Cabinet;
import java.util.List;

/**
 * CabinetService接口，定义机柜相关的业务操作。
 */
public interface CabinetService {
    /**
     * 获取指定机柜排下的所有机柜
     * @param rowId 机柜排ID
     * @return 机柜列表
     */
    List<Cabinet> getCabinetsByRowId(Integer rowId);

    /**
     * 根据ID获取机柜详情
     * @param id 机柜ID
     * @return 机柜对象
     */
    Cabinet getCabinetById(Integer id);

    /**
     * 新增机柜
     * @param cabinet 机柜对象
     * @return 新增后的机柜对象
     */
    Cabinet addCabinet(Cabinet cabinet);

    /**
     * 更新机柜信息
     * @param cabinet 机柜对象
     * @return 更新后的机柜对象
     */
    Cabinet updateCabinet(Cabinet cabinet);

    /**
     * 根据ID删除机柜
     * @param id 机柜ID
     * @return 是否删除成功
     */
    boolean deleteCabinetById(Integer id);
} 