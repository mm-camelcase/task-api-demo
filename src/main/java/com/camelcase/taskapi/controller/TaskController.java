package com.camelcase.taskapi.controller;

import com.camelcase.taskapi.api.TasksApi;
import com.camelcase.taskapi.exception.ResourceNotFoundException;
import com.camelcase.taskapi.model.Delete200Response;
import com.camelcase.taskapi.model.Task;
import com.camelcase.taskapi.model.TaskCreateRequest;
import com.camelcase.taskapi.model.TaskPage;
import com.camelcase.taskapi.model.TaskUpdateRequest;
import com.camelcase.taskapi.service.TaskService;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;



@RestController
@RequestMapping("/tasks")
public class TaskController implements TasksApi {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // Get all tasks with pagination & optional filtering
    @GetMapping
    @Override
    public ResponseEntity<TaskPage> findAll(
            @Valid @RequestParam(required = false) String status,
            @Valid @RequestParam(defaultValue = "1") Integer page,
            @Valid @RequestParam(defaultValue = "10") Integer size
            ) {
        Page<Task> tasks = taskService.findAll(page, size, status);
        TaskPage taskPage = new TaskPage();
        taskPage.setTasks(tasks.getContent());
        taskPage.setTotalPages(tasks.getTotalPages());
        taskPage.setTotalItems((int) tasks.getTotalElements());
        return ResponseEntity.ok(taskPage);
    }

    // Create a new task
    @PostMapping
    @Override
    public ResponseEntity<Task> create(@Valid @RequestBody TaskCreateRequest request) {

        logger.info("Craete Task: {}", request);
        Task task = taskService.create(request);
        return ResponseEntity.ok(task);
    }

    // Get a specific task by ID
    @GetMapping("/{id}")
    @Override
    public ResponseEntity<Task> get(@PathVariable String id) {
        Task task = taskService.get(id);
        return task != null ? ResponseEntity.ok(task) : ResponseEntity.notFound().build();
    }

    // Update a task by ID
    @PutMapping("/{id}")
    @Override
    public ResponseEntity<Task> update(@PathVariable String id, @Valid @RequestBody TaskUpdateRequest request) {
        Optional<Task> existingTask = Optional.ofNullable(taskService.get(id));
        if (existingTask.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Task updatedTask = taskService.update(id, request);
        return ResponseEntity.ok(updatedTask);
    }

    // Delete a task by ID
    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Delete200Response> delete(@PathVariable String id) {
        taskService.get(id);  // throws ResourceNotFoundException if task not found
        taskService.delete(id);
        
        Delete200Response response = new Delete200Response();
        response.setSuccess(true);
        return ResponseEntity.ok(response);
}
}