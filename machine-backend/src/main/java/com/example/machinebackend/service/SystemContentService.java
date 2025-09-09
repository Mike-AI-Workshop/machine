package com.example.machinebackend.service;

import java.util.Map;

/**
 * SystemContentService接口，定义系统内容相关的业务操作。
 */
public interface SystemContentService {

    /**
     * 获取首页内容，并组织成嵌套Map结构
     * @return 嵌套的Map，符合前端JSON结构
     */
    Map<String, Object> getHomeContentAsMap();

    /**
     * 批量更新首页内容
     * @param contentData 包含键值对的Map
     */
    void updateHomeContent(Map<String, String> contentData);

} 