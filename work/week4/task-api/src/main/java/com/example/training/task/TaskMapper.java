package com.example.training.task;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TaskMapper {
    List<Task> findAll();

    Task findById(@Param("id") Long id);

    int insert(Task task);

    int update(Task task);

    int delete(@Param("id") Long id);
}