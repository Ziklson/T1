package com.kuvarin.taskcrud.mapper;

import com.kuvarin.taskcrud.dto.TaskDTO;
import com.kuvarin.taskcrud.enums.TaskStatus;
import com.kuvarin.taskcrud.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskMapperTest {

    private TaskDTO taskDTO;

    private Task task;

    @BeforeEach
    void setUp() {
        taskDTO = TaskDTO.builder()
                .title("Title")
                .description("Description")
                .userId(1L)
                .taskStatus("NEW")
                .build();

        task = Task.builder()
                .title("Title")
                .description("Description")
                .userId(1L)
                .taskStatus(TaskStatus.NEW)
                .build();

    }

    @Test
    @DisplayName("Тест маппинга TaskDTO в Task entity")
    void requestToTask() {

        Task expTask = TaskMapper.requestToTask(taskDTO);

        assertEquals(expTask.getTitle(), taskDTO.getTitle());
        assertEquals(expTask.getDescription(), taskDTO.getDescription());
        assertEquals(expTask.getUserId(), taskDTO.getUserId());
        assertEquals(expTask.getTaskStatus().name(), taskDTO.getTaskStatus());

    }

    @Test
    @DisplayName("Тест маппинга Task entity в TaskDTO")
    void taskToDto() {

        TaskDTO expTaskDTO = TaskMapper.taskToDto(task);

        assertEquals(expTaskDTO.getTitle(), task.getTitle());
        assertEquals(expTaskDTO.getDescription(), task.getDescription());
        assertEquals(expTaskDTO.getUserId(), task.getUserId());
        assertEquals(TaskStatus.valueOf(expTaskDTO.getTaskStatus()), task.getTaskStatus());

    }
}