package com.example.machinebackend.service.impl;

import com.example.machinebackend.entity.SystemContent;
import com.example.machinebackend.mapper.SystemContentMapper;
import com.example.machinebackend.service.SystemContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SystemContentServiceImpl implements SystemContentService {

    private final SystemContentMapper systemContentMapper;

    @Autowired
    public SystemContentServiceImpl(SystemContentMapper systemContentMapper) {
        this.systemContentMapper = systemContentMapper;
    }

    @Override
    public Map<String, Object> getHomeContentAsMap() {
        List<SystemContent> contentList = systemContentMapper.findByKeyPrefix("home.");
        Map<String, Object> result = new HashMap<>();

        for (SystemContent item : contentList) {
            deepPut(result, item.getContentKey(), item.getContentValue());
        }

        return (Map<String, Object>) result.getOrDefault("home", new HashMap<>());
    }
    
    @SuppressWarnings("unchecked")
    private void deepPut(Map<String, Object> map, String key, String value) {
        String[] parts = key.split("\\.");
        Map<String, Object> current = map;

        for (int i = 0; i < parts.length - 1; i++) {
            String part = parts[i];
            
            boolean isNextPartArrayIndex = (i + 1 < parts.length) && parts[i + 1].matches("\\d+");

            if (isNextPartArrayIndex) {
                List<Object> list = (List<Object>) current.computeIfAbsent(part, k -> new ArrayList<>());
                int index = Integer.parseInt(parts[i + 1]);

                while (list.size() <= index) {
                    list.add(null);
                }

                if (i + 2 == parts.length) { // The array element itself is the value
                    list.set(index, value);
                    return; 
                } else { // The array element is an object
                    Map<String, Object> objInList = (Map<String, Object>) list.get(index);
                    if (objInList == null) {
                        objInList = new HashMap<>();
                        list.set(index, objInList);
                    }
                    current = objInList;
                    i++; 
                }
            } else {
                current = (Map<String, Object>) current.computeIfAbsent(part, k -> new HashMap<>());
            }
        }
        current.put(parts[parts.length - 1], value);
    }


    @Override
    @Transactional
    public void updateHomeContent(Map<String, String> contentData) {
        for (Map.Entry<String, String> entry : contentData.entrySet()) {
            systemContentMapper.updateValueByKey(entry.getKey(), entry.getValue());
        }
    }
} 