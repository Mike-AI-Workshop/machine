package com.example.machinebackend.mapper;

import com.example.machinebackend.entity.CabinetRow;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CabinetRowMapper {

    @Select("SELECT * FROM cabinet_row WHERE room_id = #{roomId}")
    List<CabinetRow> findByRoomId(Integer roomId);

    @Insert("INSERT INTO cabinet_row (room_id, name, description) VALUES (#{roomId}, #{name}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(CabinetRow cabinetRow);

    @Update("UPDATE cabinet_row SET name = #{name}, description = #{description} WHERE id = #{id}")
    int update(CabinetRow cabinetRow);

    @Delete("DELETE FROM cabinet_row WHERE id = #{id}")
    int delete(Integer id);

    @Select("SELECT * FROM cabinet_row WHERE id = #{id}")
    CabinetRow findById(Integer id);
} 