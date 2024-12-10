package com.kuvarin.taskcrud.mapper;

import com.kuvarin.taskcrud.dto.TaskDTO;
import com.kuvarin.taskcrud.enums.TaskStatus;
import com.kuvarin.taskcrud.model.Task;

public class TaskMapper {

    public static Task requestToTask(TaskDTO taskDto) {
        return Task.builder()
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .userId(taskDto.getUserId())
                .taskStatus(TaskStatus.valueOf(taskDto.getTaskStatus()))
                .build();
    }

    public static TaskDTO taskToDto(Task task) {
        return TaskDTO.builder()
                .title(task.getTitle())
                .description(task.getDescription())
                .userId(task.getUserId())
                .taskStatus(task.getTaskStatus().name())
                .build();
    }
}
