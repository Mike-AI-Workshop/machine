package com.example.machinebackend.mapper;

import com.example.machinebackend.entity.SystemContent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * SystemContentMapper接口，负责系统内容表(system_content)的数据库操作。
 */
@Mapper
public interface SystemContentMapper {

    /**
     * 根据键名M前缀查询内容列表
     * @param prefix 键名前缀
     * @return 内容列表
     */
    @Select("SELECT * FROM system_content WHERE content_key LIKE CONCAT(#{prefix}, '%')")
    List<SystemContent> findByKeyPrefix(@Param("prefix") String prefix);

    /**
     * 根据键名更新内容值
     * @param key   内容唯一键
     * @param value 新的内容值
     * @return 影响行数
     */
    @Update("UPDATE system_content SET content_value = #{value} WHERE content_key = #{key}")
    int updateValueByKey(@Param("key") String key, @Param("value") String value);

} 