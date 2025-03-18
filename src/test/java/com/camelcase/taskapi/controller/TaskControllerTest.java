package com.camelcase.taskapi.controller;

import com.camelcase.taskapi.model.Delete200Response;
import com.camelcase.taskapi.model.Task;
import com.camelcase.taskapi.model.TaskCreateRequest;
import com.camelcase.taskapi.model.TaskPage;
import com.camelcase.taskapi.model.TaskUpdateRequest;
import com.camelcase.taskapi.service.TaskService;
import com.camelcase.taskapi.model.entity.TaskStatusEnum;
import com.camelcase.taskapi.exception.GlobalExceptionHandler;
import com.camelcase.taskapi.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(taskController)
                .setControllerAdvice(new GlobalExceptionHandler()) // Add global exception handler
                .build();
    }

    @Test
    public void testFindAll() throws Exception {
        Task task1 = new Task();
        Task task2 = new Task();
        Page<Task> taskPage = new PageImpl<>(Arrays.asList(task1, task2));
        when(taskService.findAll(anyInt(), anyInt(), isNull())).thenReturn(taskPage);

        mockMvc.perform(get("/tasks")
                .param("page", "1")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tasks").isArray())
                .andExpect(jsonPath("$.tasks.length()").value(2));

        verify(taskService, times(1)).findAll(anyInt(), anyInt(), isNull());
    }

    @Test
    public void testCreate() throws Exception {
        TaskCreateRequest request = new TaskCreateRequest();
        request.setTitle("Test Task");
        request.setDescription("Test Description");
        request.setTaskStatus(TaskCreateRequest.TaskStatusEnum.PENDING);
        request.setDueDate(java.time.LocalDate.now());

        Task task = new Task();
        task.setTitle("Test Task");
        when(taskService.create(any(TaskCreateRequest.class))).thenReturn(task);

        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Test Task\",\"description\":\"Test Description\",\"taskStatus\":\"pending\",\"dueDate\":\"2025-03-18\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Task"));

        verify(taskService, times(1)).create(any(TaskCreateRequest.class));
    }



    @Test
    public void testGet() throws Exception {
        Task task = new Task();
        task.setTitle("Test Task");
        when(taskService.get(anyString())).thenReturn(task);

        mockMvc.perform(get("/tasks/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Task"));

        verify(taskService, times(1)).get(anyString());
    }

    @Test
    public void testGetNotFound() throws Exception {
        when(taskService.get(anyString())).thenThrow(new ResourceNotFoundException("Task not found"));

        mockMvc.perform(get("/tasks/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(taskService, times(1)).get(anyString());
    }

    @Test
    public void testUpdate() throws Exception {
        TaskUpdateRequest request = new TaskUpdateRequest();
        request.setTitle("Updated Task");

        Task existingTask = new Task();
        existingTask.setTitle("Old Task");

        Task updatedTask = new Task();
        updatedTask.setTitle("Updated Task");

        // Mock the task retrieval first
        when(taskService.get(anyString())).thenReturn(existingTask);

        // Mock the task update
        when(taskService.update(anyString(), any(TaskUpdateRequest.class))).thenReturn(updatedTask);


        mockMvc.perform(put("/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Updated Task\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task"));

        verify(taskService, times(1)).update(anyString(), any(TaskUpdateRequest.class));
    }

    @Test
    public void testDelete() throws Exception {
        doNothing().when(taskService).delete(anyString());

        mockMvc.perform(delete("/tasks/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(taskService, times(1)).delete(anyString());
    }
}