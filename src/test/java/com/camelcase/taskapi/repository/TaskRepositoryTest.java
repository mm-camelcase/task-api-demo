package com.camelcase.taskapi.repository;

import com.camelcase.taskapi.model.entity.TaskEntity;
import com.camelcase.taskapi.model.entity.TaskStatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    public void setUp() {
        TaskEntity task1 = new TaskEntity();
        task1.setTaskStatus(TaskStatusEnum.COMPLETED);
        taskRepository.save(task1);

        TaskEntity task2 = new TaskEntity();
        task2.setTaskStatus(TaskStatusEnum.PENDING);
        taskRepository.save(task2);

        TaskEntity task3 = new TaskEntity();
        task3.setTaskStatus(TaskStatusEnum.COMPLETED);
        taskRepository.save(task3);
    }

    @Test
    public void testFindByTaskStatus() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<TaskEntity> completedTasks = taskRepository.findByTaskStatus(TaskStatusEnum.COMPLETED, pageable);

        assertThat(completedTasks.getContent()).hasSize(2);
    }

    @Test
    public void testCountByTaskStatus() {
        long completedCount = taskRepository.countByTaskStatus(TaskStatusEnum.COMPLETED);

        assertThat(completedCount).isEqualTo(2);
    }
}
