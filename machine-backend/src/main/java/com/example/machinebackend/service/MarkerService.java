package com.example.machinebackend.service;

import com.example.machinebackend.entity.Marker;
import java.util.List;

/**
 * MarkerService接口，定义图标标注相关的业务操作。
 */
public interface MarkerService {
    /**
     * 获取指定图片上的所有标注点
     * @param parentType 所属类型（cabinet/device）
     * @param parentId 所属ID
     * @param imageType 图片类型（front/back）
     * @return 标注点列表
     */
    List<Marker> getMarkersByParentAndImage(String parentType, Integer parentId, String imageType);

    /**
     * 根据ID获取标注点详情
     * @param id 标注点ID
     * @return 标注点对象
     */
    Marker getMarkerById(Integer id);

    /**
     * 新增标注点
     * @param marker 标注点对象
     * @return 新增后的标注点对象
     */
    Marker addMarker(Marker marker);

    /**
     * 更新标注点信息
     * @param marker 标注点对象
     * @return 更新后的标注点对象
     */
    Marker updateMarker(Marker marker);

    /**
     * 根据ID删除标注点
     * @param id 标注点ID
     * @return 是否删除成功
     */
    boolean deleteMarkerById(Integer id);
} 