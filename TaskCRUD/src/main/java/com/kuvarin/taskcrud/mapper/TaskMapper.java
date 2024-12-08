package com.kuvarin.taskcrud.mapper;

import com.kuvarin.taskcrud.dto.TaskRequestDTO;
import com.kuvarin.taskcrud.dto.TaskResponseDTO;
import com.kuvarin.taskcrud.model.Task;

public class TaskMapper {

    public static Task requestToTask(TaskRequestDTO taskRequestDto) {
        Task task = new Task();
        task.setTitle(taskRequestDto.getTitle());
        task.setDescription(taskRequestDto.getDescription());
        task.setUserId(taskRequestDto.getUserId());
        return task;
    }

    public static TaskResponseDTO taskToDto(Task task) {
        TaskResponseDTO responseDTO = new TaskResponseDTO();
        responseDTO.setDescription(task.getDescription());
        responseDTO.setTitle(task.getTitle());
        responseDTO.setUserId(task.getUserId());
        return responseDTO;
    }
}
