package com.camelcase.taskapi.repository;

import com.camelcase.taskapi.model.entity.TaskEntity;
import com.camelcase.taskapi.model.entity.TaskStatusEnum;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    Page<TaskEntity> findByTaskStatus(TaskStatusEnum statusEnum, Pageable pageable);
    long countByTaskStatus(TaskStatusEnum statusEnum);
}