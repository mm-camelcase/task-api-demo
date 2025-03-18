package com.camelcase.taskapi.service;

import com.camelcase.taskapi.exception.ResourceNotFoundException;
import com.camelcase.taskapi.mapper.TaskMapper;
import com.camelcase.taskapi.model.Task;
import com.camelcase.taskapi.model.TaskCreateRequest;
import com.camelcase.taskapi.model.TaskUpdateRequest;
import com.camelcase.taskapi.model.entity.TaskEntity;
import com.camelcase.taskapi.model.entity.TaskStatusEnum;
import com.camelcase.taskapi.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {
        TaskEntity taskEntity1 = new TaskEntity();
        taskEntity1.setTaskStatus(TaskStatusEnum.COMPLETED);
        TaskEntity taskEntity2 = new TaskEntity();
        taskEntity2.setTaskStatus(TaskStatusEnum.PENDING);

        Page<TaskEntity> taskPage = new PageImpl<>(Arrays.asList(taskEntity1, taskEntity2));
        when(taskRepository.findAll(any(Pageable.class))).thenReturn(taskPage);
        when(taskMapper.toDto(any(TaskEntity.class))).thenReturn(new Task());

        Page<Task> result = taskService.findAll(1, 10, null);

        assertThat(result.getContent()).hasSize(2);
        verify(taskRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    public void testFindAllWithStatus() {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setTaskStatus(TaskStatusEnum.COMPLETED);

        Page<TaskEntity> taskPage = new PageImpl<>(Arrays.asList(taskEntity));
        when(taskRepository.findByTaskStatus(any(TaskStatusEnum.class), any(Pageable.class))).thenReturn(taskPage);
        when(taskMapper.toDto(any(TaskEntity.class))).thenReturn(new Task());

        Page<Task> result = taskService.findAll(1, 10, "COMPLETED");

        assertThat(result.getContent()).hasSize(1);
        verify(taskRepository, times(1)).findByTaskStatus(any(TaskStatusEnum.class), any(Pageable.class));
    }

    @Test
    public void testGet() {
        TaskEntity taskEntity = new TaskEntity();
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(taskEntity));
        when(taskMapper.toDto(any(TaskEntity.class))).thenReturn(new Task());

        Task result = taskService.get("1");

        assertThat(result).isNotNull();
        verify(taskRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testGetNotFound() {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taskService.get("1"));
        verify(taskRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testCreate() {
        TaskCreateRequest request = new TaskCreateRequest();
        request.setTitle("Test Task");
        request.setDescription("Test Description");
        request.setTaskStatus(TaskCreateRequest.TaskStatusEnum.valueOf(TaskStatusEnum.PENDING.name()));
        request.setDueDate(null);

        TaskEntity taskEntity = new TaskEntity();
        when(taskRepository.save(any(TaskEntity.class))).thenReturn(taskEntity);
        when(taskMapper.toDto(any(TaskEntity.class))).thenReturn(new Task());

        Task result = taskService.create(request);

        assertThat(result).isNotNull();
        verify(taskRepository, times(1)).save(any(TaskEntity.class));
    }

    @Test
    public void testDelete() {
        when(taskRepository.existsById(anyLong())).thenReturn(true);

        taskService.delete("1");

        verify(taskRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void testDeleteNotFound() {
        when(taskRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> taskService.delete("1"));
        verify(taskRepository, times(1)).existsById(anyLong());
    }

    @Test
    public void testUpdate() {
        TaskUpdateRequest request = new TaskUpdateRequest();
        request.setTitle("Updated Task");

        TaskEntity taskEntity = new TaskEntity();
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(taskEntity));
        when(taskRepository.save(any(TaskEntity.class))).thenReturn(taskEntity);
        when(taskMapper.toDto(any(TaskEntity.class))).thenReturn(new Task());

        Task result = taskService.update("1", request);

        assertThat(result).isNotNull();
        verify(taskRepository, times(1)).findById(anyLong());
        verify(taskRepository, times(1)).save(any(TaskEntity.class));
    }

    @Test
    public void testUpdateNotFound() {
        TaskUpdateRequest request = new TaskUpdateRequest();
        request.setTitle("Updated Task");

        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taskService.update("1", request));
        verify(taskRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testCountTasks() {
        when(taskRepository.countByTaskStatus(any(TaskStatusEnum.class))).thenReturn(5L);

        int result = taskService.countTasks("COMPLETED");

        assertThat(result).isEqualTo(5);
        verify(taskRepository, times(1)).countByTaskStatus(any(TaskStatusEnum.class));
    }
}