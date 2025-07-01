package com.example.machinebackend.service.impl;

import com.example.machinebackend.entity.Marker;
import com.example.machinebackend.mapper.MarkerMapper;
import com.example.machinebackend.service.MarkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * MarkerServiceImpl实现类，实现图标标注相关的业务逻辑。
 */
@Service
public class MarkerServiceImpl implements MarkerService {
    /** MarkerMapper依赖，用于数据库操作 */
    private final MarkerMapper markerMapper;

    @Autowired
    public MarkerServiceImpl(MarkerMapper markerMapper) {
        this.markerMapper = markerMapper;
    }

    /**
     * 获取指定图片上的所有标注点
     * @param parentType 所属类型（cabinet/device）
     * @param parentId 所属ID
     * @param imageType 图片类型（front/back）
     * @return 标注点列表
     */
    @Override
    public List<Marker> getMarkersByParentAndImage(String parentType, Integer parentId, String imageType) {
        return markerMapper.selectByParentAndImage(parentType, parentId, imageType);
    }

    /**
     * 根据ID获取标注点详情
     * @param id 标注点ID
     * @return 标注点对象
     */
    @Override
    public Marker getMarkerById(Integer id) {
        return markerMapper.selectById(id);
    }

    /**
     * 新增标注点
     * @param marker 标注点对象
     * @return 新增后的标注点对象
     */
    @Override
    public Marker addMarker(Marker marker) {
        markerMapper.insert(marker);
        return marker;
    }

    /**
     * 更新标注点信息
     * @param marker 标注点对象
     * @return 更新后的标注点对象
     */
    @Override
    public Marker updateMarker(Marker marker) {
        markerMapper.update(marker);
        return marker;
    }

    /**
     * 根据ID删除标注点
     * @param id 标注点ID
     * @return 是否删除成功
     */
    @Override
    public boolean deleteMarkerById(Integer id) {
        return markerMapper.deleteById(id) > 0;
    }
} 