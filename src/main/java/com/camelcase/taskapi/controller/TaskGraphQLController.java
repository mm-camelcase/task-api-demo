package com.camelcase.taskapi.controller;

import com.camelcase.taskapi.exception.ResourceNotFoundException;
import com.camelcase.taskapi.model.Task;
import com.camelcase.taskapi.model.TaskCreateRequest;
import com.camelcase.taskapi.model.TaskPage;
import com.camelcase.taskapi.model.TaskUpdateRequest;
import com.camelcase.taskapi.service.TaskService;
import com.camelcase.taskapi.model.entity.TaskStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@Component
@Controller
public class TaskGraphQLController {

    private static final Logger logger = LoggerFactory.getLogger(TaskGraphQLController.class);
    private final TaskService taskService;

    public TaskGraphQLController(TaskService taskService) {
        this.taskService = taskService;
        logger.info("🚀 INIT");
    }

    /**
     * Query to fetch a paginated list of tasks.
     */
    // @QueryMapping(name = "taskPage")
    // public Page<Task> taskPage(@Argument int page, @Argument int size, @Argument TaskStatusEnum taskStatus) {
    //     logger.info("🚀 taskPage query called with page={}, size={}, taskStatus={}", page, size, taskStatus);
    //     return taskService.findAll(page, size, taskStatus != null ? taskStatus.name() : null);
    // }

    @QueryMapping(name = "taskPage")
    public TaskPage taskPage(@Argument int page, @Argument int size, @Argument TaskStatusEnum taskStatus) {
        logger.info("🚀 taskPage query called with page={}, size={}, taskStatus={}", page, size, taskStatus);
        
        Page<Task> pageResult = taskService.findAll(page, size, taskStatus != null ? taskStatus.name() : null);
        
        TaskPage taskPage = new TaskPage();
        taskPage.setTasks(pageResult.getContent());
        taskPage.setTotalPages(pageResult.getTotalPages());
        taskPage.setTotalItems((int) pageResult.getTotalElements());

        return taskPage;

        // return new TaskPage(
        //     pageResult.getContent(),  // ✅ Ensure we return a list, not a Page<>
        //     pageResult.getTotalPages(),
        //     pageResult.getTotalElements()
        // );
    }

    /**
     * Query to fetch a single task by ID.
     */
    @QueryMapping(name = "task")  // FIX: Method name matches schema
    public Task task(@Argument String id) {
        logger.info("🚀 task query called with id={}", id);
        return taskService.get(id);
    }

    /**
     * Mutation to create a new task.
     */
    @MutationMapping(name = "create")
    public Task create(@Argument("taskCreateRequestInput") TaskCreateRequest request) {
        return taskService.create(request);
    }

    /**
     * Mutation to update an existing task.
     */
    @MutationMapping(name = "update")
    public Task update(@Argument String id, @Argument("taskUpdateRequestInput") TaskUpdateRequest request) {
        return taskService.update(id, request);
    }

    /**
     * Mutation to delete a task.
     */
    @MutationMapping
    public Boolean deleteTask(@Argument String id) {
        try {
            taskService.delete(id);
            return true;
        } catch (ResourceNotFoundException e) {
            return false;
        }
    }

    /**
     * Query to count tasks by status.
     */
    @QueryMapping
    public int countTasks(@Argument String status) {
        return taskService.countTasks(status);
    }
}
