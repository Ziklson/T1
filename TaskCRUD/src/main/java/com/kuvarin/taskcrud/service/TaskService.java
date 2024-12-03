package com.kuvarin.taskcrud.service;

import com.kuvarin.taskcrud.aspect.annotation.LogException;
import com.kuvarin.taskcrud.dto.TaskRequestDTO;
import com.kuvarin.taskcrud.exception.TaskNotFoundException;
import com.kuvarin.taskcrud.exception.TasksNotFoundException;
import com.kuvarin.taskcrud.model.Task;
import com.kuvarin.taskcrud.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService (TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @LogException
    public Task getTask(Long id) throws TaskNotFoundException {
        return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(String.format("Task with id: %d not found", id)));
    }

    @LogException
    public Task saveTask(TaskRequestDTO taskDTO) {
        Task task = new Task();

        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setUserId(taskDTO.getUserId());

        return taskRepository.save(task);
    }

    @Transactional
    @LogException
    public Task updateTask(Long id, TaskRequestDTO taskDTO) throws TaskNotFoundException {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(String.format("Task with id: %d not found", id)));

        if (taskDTO.getTitle() != null) {
            task.setTitle(taskDTO.getTitle());
        }

        if (taskDTO.getDescription() != null) {
            task.setDescription(taskDTO.getDescription());
        }

        if (taskDTO.getUserId() != null) {
            task.setUserId(taskDTO.getUserId());
        }
        return taskRepository.save(task);
    }

    @LogException
    public void deleteTask(Long id) throws TaskNotFoundException {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(String.format("Task with id: %d not found", id)));
        taskRepository.delete(task);
    }

    @LogException
    public List<Task> getAll() throws TasksNotFoundException {
        List<Task> tasks = taskRepository.findAll();
        if (tasks.isEmpty()) {
            throw new TasksNotFoundException("Tasks not found");
        }
        return tasks;
    }


}
