package com.camelcase.taskapi.service;

import com.camelcase.taskapi.exception.ResourceNotFoundException;
import com.camelcase.taskapi.model.Task;
import com.camelcase.taskapi.model.TaskCreateRequest;
import com.camelcase.taskapi.model.TaskUpdateRequest;

import net.datafaker.Faker;
import org.springframework.stereotype.Service;

import java.util.*;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final Map<String, Task> taskStore = new HashMap<>();
    private final Faker faker = new Faker();

    public TaskService() {
        // Generate 10 fake tasks
        for (int i = 1; i <= 10; i++) {
            Task task = new Task();
            task.setId(UUID.randomUUID().toString());
            task.setTitle(faker.lorem().sentence(3));
            task.setDescription(faker.lorem().paragraph());
            task.setStatus(Task.StatusEnum.valueOf(faker.options().option("pending", "in_progress", "completed").toUpperCase()));
            task.setDueDate(faker.date().future(10, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

            taskStore.put(task.getId(), task);
        }
    }

    // Get all tasks with pagination and optional status filtering
    public List<Task> findAll(Integer page, Integer size, String status) {
        int validPage = Math.max(page, 0); // Ensure non-negative page number

        return taskStore.values().stream()
                .filter(task -> status == null || task.getStatus().name().equalsIgnoreCase(status))
                .skip(Math.max(0, (long) (validPage - 1) * size)) // Prevent negative skip
                .limit(size)
                .collect(Collectors.toList());
    }

    // Get a specific task by ID with error handling
    public Task get(String id) {
        return Optional.ofNullable(taskStore.get(id))
                .orElseThrow(() -> new ResourceNotFoundException("Task with ID " + id + " not found"));
    }

    // Create a new task
    public Task create(TaskCreateRequest request) {
        Task task = new Task();
        task.setId(UUID.randomUUID().toString());
        task.setTitle(request.getTitle() != null ? request.getTitle() : faker.lorem().sentence(3));
        task.setDescription(request.getDescription() != null ? request.getDescription() : faker.lorem().paragraph());
        task.setStatus(request.getStatus() != null ? Task.StatusEnum.valueOf(request.getStatus().name()) : Task.StatusEnum.valueOf(faker.options().option("pending", "in_progress", "completed").toUpperCase()));
        task.setDueDate(request.getDueDate() != null ? request.getDueDate() : faker.date().future(10, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

        taskStore.put(task.getId(), task);
        return task;
    }

    // Delete a task with error handling
    public void delete(String id) {
        if (!taskStore.containsKey(id)) {
            throw new ResourceNotFoundException("Task with ID " + id + " not found");
        }
        taskStore.remove(id);
    }

    // Update an existing task with proper error handling
    public Task update(String id, TaskUpdateRequest request) {
        Task existingTask = Optional.ofNullable(taskStore.get(id))
                .orElseThrow(() -> new ResourceNotFoundException("Task with ID " + id + " not found"));

        if (request.getTitle() != null) {
            existingTask.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            existingTask.setDescription(request.getDescription());
        }
        if (request.getStatus() != null) {
            existingTask.setStatus(Task.StatusEnum.valueOf(request.getStatus().name()));
        }
        if (request.getDueDate() != null) {
            existingTask.setDueDate(request.getDueDate());
        }

        taskStore.put(id, existingTask);
        return existingTask;
    }

    // Count tasks by status
    public int countTasks(String status) {
        return (int) taskStore.values().stream()
                .filter(task -> status == null || task.getStatus().name().equalsIgnoreCase(status))
                .count();
    }
}
