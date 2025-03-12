package com.camelcase.taskapi.mapper;

import com.camelcase.taskapi.model.Task;
import com.camelcase.taskapi.model.entity.TaskEntity;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskEntity toEntity(Task task);
    Task toDto(TaskEntity taskEntity);
}