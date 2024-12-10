package com.kuvarin.taskcrud.service;

import com.kuvarin.taskcrud.aspect.annotation.LogException;
import com.kuvarin.taskcrud.dto.TaskDTO;
import com.kuvarin.taskcrud.enums.TaskStatus;
import com.kuvarin.taskcrud.exception.TaskNotFoundException;
import com.kuvarin.taskcrud.exception.TasksNotFoundException;
import com.kuvarin.taskcrud.kafka.KafkaTaskProducer;
import com.kuvarin.taskcrud.mapper.TaskMapper;
import com.kuvarin.taskcrud.model.Task;
import com.kuvarin.taskcrud.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    private final KafkaTaskProducer kafkaTaskProducer;

    @Autowired
    public TaskService (TaskRepository taskRepository, KafkaTaskProducer kafkaTaskProducer) {
        this.taskRepository = taskRepository;
        this.kafkaTaskProducer = kafkaTaskProducer;
    }

    @LogException
    public TaskDTO getTask(Long id) throws TaskNotFoundException {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(String.format("Task with id: %d not found", id)));
        return TaskMapper.taskToDto(task);
    }

    @LogException
    public TaskDTO saveTask(TaskDTO taskDTO) {
        Task task = TaskMapper.requestToTask(taskDTO);
        return TaskMapper.taskToDto(taskRepository.save(task));
    }

    @Transactional
    @LogException
    public TaskDTO updateTask(Long id, TaskDTO taskDTO) throws TaskNotFoundException {
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

        if (!taskDTO.getTaskStatus().equals(task.getTaskStatus().name())) {
            task.setTaskStatus(TaskStatus.valueOf(taskDTO.getTaskStatus()));
            notifyTaskStatusUpdate(taskDTO);
        }

        return TaskMapper.taskToDto(taskRepository.save(task));
    }

    @LogException
    public void deleteTask(Long id) throws TaskNotFoundException {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(String.format("Task with id: %d not found", id)));
        taskRepository.delete(task);
    }

    @LogException
    public List<TaskDTO> getAll() throws TasksNotFoundException {
        List<Task> tasks = taskRepository.findAll();
        if (tasks.isEmpty()) {
            throw new TasksNotFoundException("Tasks not found");
        }
        return tasks.stream().map(TaskMapper::taskToDto).collect(Collectors.toList());
    }

    private void notifyTaskStatusUpdate(TaskDTO taskDTO) {
        kafkaTaskProducer.sendTaskUpdate(taskDTO);
    }


}
