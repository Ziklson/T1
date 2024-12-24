package com.kuvarin.taskcrud.service;

import com.kuvarin.taskcrud.dto.TaskDTO;
import com.kuvarin.taskcrud.enums.TaskStatus;
import com.kuvarin.taskcrud.exception.TaskNotFoundException;
import com.kuvarin.taskcrud.exception.TasksNotFoundException;
import com.kuvarin.taskcrud.kafka.KafkaTaskProducer;
import com.kuvarin.taskcrud.model.Task;
import com.kuvarin.taskcrud.repository.TaskRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private KafkaTaskProducer kafkaTaskProducer; // нужен для метода update, явно нигде не используется

    @InjectMocks
    private TaskService taskService;

    private Task task;
    @BeforeEach
    void setUp() {
        Long id = 1L;
        String title = "testTitle";
        String description = "testDescription";
        Long userId = 1L;
        TaskStatus status = TaskStatus.NEW;

        task = new Task(id, title, description, userId, status);

    }
    @Test
    @DisplayName("Тест получения задачи по ID")
    void getTask() {
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        TaskDTO actualTask = taskService.getTask(task.getId());

        assertEquals(task.getUserId(), actualTask.getUserId());
        assertEquals(task.getTitle(), actualTask.getTitle());
        assertEquals(task.getDescription(), actualTask.getDescription());
        assertEquals(task.getTaskStatus(), TaskStatus.valueOf(actualTask.getTaskStatus()));

    }

    @Test
    @DisplayName("Тест получения задачи по несуществующему ID")
    void getTaskThrowsTaskNotFoundException() {
        when(taskRepository.findById(any())).thenReturn(Optional.empty());

        Assertions.assertThrows(TaskNotFoundException.class, () -> taskService.getTask(task.getId()));

    }

    @Test
    @DisplayName("Тест на успешное сохранение новой задачи")
    void saveTask() {
        when(taskRepository.save(any())).thenReturn(task);

        TaskDTO taskDTO = new TaskDTO(task.getTitle(), task.getDescription(), task.getUserId(), task.getTaskStatus().name());

        TaskDTO actualTaskDTO = taskService.saveTask(taskDTO);

        assertEquals(task.getUserId(), actualTaskDTO.getUserId());
        assertEquals(task.getTitle(), actualTaskDTO.getTitle());
        assertEquals(task.getDescription(), actualTaskDTO.getDescription());
        assertEquals(task.getTaskStatus(), TaskStatus.valueOf(actualTaskDTO.getTaskStatus()));
    }

    @Test
    @DisplayName("Тест на успешное обновление задачи")
    void updateTask() {
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(taskRepository.save(any())).thenReturn(task);


        String updTitle = "newTitle";
        String updDescription = "newDescription";
        Long updUserId = task.getUserId() + 1;
        TaskStatus updStatus = TaskStatus.COMPLETED;

        TaskDTO updTaskDTO = new TaskDTO(updTitle, updDescription, updUserId, updStatus.toString());

        TaskDTO actualDTO = taskService.updateTask(task.getId(), updTaskDTO);

        assertEquals(updTitle, actualDTO.getTitle());
        assertEquals(updDescription, actualDTO.getDescription());
        assertEquals(updUserId, actualDTO.getUserId());
        assertEquals(updStatus.name(), actualDTO.getTaskStatus());

    }

    @Test
    @DisplayName("Тест обновления задачи по несуществующему ID")
    void updateTaskThrowsTaskNotFoundException() {
        when(taskRepository.findById(any())).thenReturn(Optional.empty());

        String updTitle = "newTitle";
        String updDescription = "newDescription";
        Long updUserId = task.getUserId() + 1;
        TaskStatus updStatus = TaskStatus.COMPLETED;

        TaskDTO updTaskDTO = new TaskDTO(updTitle, updDescription, updUserId, updStatus.toString());

        Assertions.assertThrows(TaskNotFoundException.class,
                () -> taskService.updateTask(task.getId(), updTaskDTO));

    }

    @Test
    @DisplayName("Тест удаления несуществующей задачи")
    void deleteTaskThrowsTaskNotFoundException() {
        when(taskRepository.findById(any())).thenReturn(Optional.empty());

        Assertions.assertThrows(TaskNotFoundException.class,
                () -> taskService.deleteTask(task.getId()));
    }

    @Test
    @DisplayName("Тест удаления существующей задачи")
    void deleteTask() {
        when(taskRepository.findById(any())).thenReturn(Optional.of(task));

        String answer = taskService.deleteTask(task.getId());

        assertEquals(String.format("Task with ID: %d was removed successfully", task.getId()), answer);
    }

    @Test
    @DisplayName("Проверка получения списка всех задач")
    void getAll() {
        when(taskRepository.findAll()).thenReturn(List.of(task));

        TaskDTO taskDTO = new TaskDTO(
                task.getTitle(),
                task.getDescription(),
                task.getUserId(),
                task.getTaskStatus().toString());

        List<TaskDTO> expTaskDTOS = List.of(taskDTO);

        List<TaskDTO> actTaskDTOS = taskService.getAll();
        assertEquals(expTaskDTOS,actTaskDTOS);
    }

    @Test
    @DisplayName("Проверка получения списка всех задач, если задач нет")
    void getAllThrowsTasksNotFoundException() {
        when(taskRepository.findAll()).thenReturn(Collections.emptyList());

        Assertions.assertThrows(TasksNotFoundException.class,
                () -> taskService.getAll());
    }
}