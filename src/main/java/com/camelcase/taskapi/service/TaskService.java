package com.camelcase.taskapi.service;

import com.camelcase.taskapi.exception.ResourceNotFoundException;
import com.camelcase.taskapi.mapper.TaskMapper;
import com.camelcase.taskapi.model.Task;
import com.camelcase.taskapi.model.TaskCreateRequest;
import com.camelcase.taskapi.model.TaskUpdateRequest;
import com.camelcase.taskapi.model.entity.TaskStatusEnum;
import com.camelcase.taskapi.model.entity.TaskEntity;
import com.camelcase.taskapi.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import java.util.Optional;

@Service
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    // Get all tasks with pagination and optional status filtering
    public Page<Task> findAll(int page, int size, String status) {
        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), size);
        Page<TaskEntity> taskPage;

        if (status != null && !status.isEmpty()) {
            TaskStatusEnum statusEnum = TaskStatusEnum.fromString(status);
            taskPage = taskRepository.findByTaskStatus(statusEnum, pageable);
        } else {
            taskPage = taskRepository.findAll(pageable);
        }

        Page<Task> p = taskPage.map(taskMapper::toDto);

        logger.info("🚀 Found {} tasks", p.getContent());

        return p;
    }

    // Get a specific task by ID with error handling
    public Task get(String id) {
        TaskEntity taskEntity = taskRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new ResourceNotFoundException("Task with ID " + id + " not found"));
        return taskMapper.toDto(taskEntity);
    }

    // Create a new task
    public Task create(TaskCreateRequest request) {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setTitle(request.getTitle());
        taskEntity.setDescription(request.getDescription());
        taskEntity.setTaskStatus(TaskStatusEnum.valueOf(request.getTaskStatus().name()));
        taskEntity.setDueDate(request.getDueDate());

        return taskMapper.toDto(taskRepository.save(taskEntity));
    }

    // Delete a task with error handling
    public void delete(String id) {
        Long taskId = Long.valueOf(id);
        if (!taskRepository.existsById(taskId)) {
            throw new ResourceNotFoundException("Task with ID " + id + " not found");
        }
        taskRepository.deleteById(taskId);
    }

    // Update an existing task with proper error handling
    public Task update(String id, TaskUpdateRequest request) {
        Long taskId = Long.valueOf(id);
        TaskEntity existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task with ID " + id + " not found"));

        if (request.getTitle() != null) {
            existingTask.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            existingTask.setDescription(request.getDescription());
        }
        if (request.getTaskStatus() != null) {
            existingTask.setTaskStatus(TaskStatusEnum.valueOf(request.getTaskStatus().name()));
        }
        if (request.getDueDate() != null) {
            existingTask.setDueDate(request.getDueDate());
        }

        return taskMapper.toDto(taskRepository.save(existingTask));
    }

    // Count tasks by status
    public int countTasks(String status) {
        TaskStatusEnum statusEnum = TaskStatusEnum.fromString(status);
        return (int) taskRepository.countByTaskStatus(statusEnum);
    }
}
