package com.kuvarin.taskcrud.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuvarin.taskcrud.container.PostgresContainer;
import com.kuvarin.taskcrud.dto.TaskDTO;
import com.kuvarin.taskcrud.enums.TaskStatus;
import com.kuvarin.taskcrud.repository.TaskRepository;
import com.kuvarin.taskcrud.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.kuvarin.taskcrud.model.Task;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;




import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class TaskControllerTest extends PostgresContainer {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;


    @Autowired
    private ObjectMapper objectMapper;

    private Task task1;

    private Task task2;

    private TaskDTO taskDTO;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();

        task1 = new Task();
        task1.setUserId(1L);
        task1.setTitle("title1");
        task1.setDescription("description1");
        task1.setTaskStatus(TaskStatus.NEW);

        task2 = new Task();
        task2.setUserId(2L);
        task2.setTitle("title2");
        task2.setDescription("description2");
        task2.setTaskStatus(TaskStatus.NEW);


        taskDTO = createTaskDTO();

    }

    @Test
    @DisplayName("Тест получения задачи по ID")
    void getTask() throws Exception {

        Long id = taskRepository.save(task1).getId();

        mockMvc.perform(get("/tasks/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value(task1.getTitle()))
                .andExpect(jsonPath("$.description").value(task1.getDescription()))
                .andExpect(jsonPath("$.userId").value(task1.getUserId()))
                .andExpect(jsonPath("$.status").value(task1.getTaskStatus().toString()));

    }

    @Test
    @DisplayName("Тест обработки TaskNotFoundException при запросе задачи с несуществующим ID")
    void getTaskThrowsTaskNotFoundException() throws Exception {
        Long invalidId = 999L;

        mockMvc.perform(get("/tasks/{id}", invalidId))
                .andExpect(status().isNotFound())
                .andExpect(content().string(String.format("Task with id: %d not found", invalidId)));
    }

    @Test
    @DisplayName("Тест получения списка всех задач")
    void getTasks() throws Exception {
        taskRepository.saveAll(List.of(task1, task2));

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").value(task1.getTitle()))
                .andExpect(jsonPath("$[0].description").value(task1.getDescription()))
                .andExpect(jsonPath("$[0].userId").value(task1.getUserId()))
                .andExpect(jsonPath("$[0].status").value(task1.getTaskStatus().toString()))
                .andExpect(jsonPath("$[1].title").value(task2.getTitle()))
                .andExpect(jsonPath("$[1].description").value(task2.getDescription()))
                .andExpect(jsonPath("$[1].userId").value(task2.getUserId()))
                .andExpect(jsonPath("$[1].status").value(task2.getTaskStatus().toString()));
    }

    @Test
    @DisplayName("Тест обработки TasksNotFoundException при отсутствии задач")
    void getTasksThrowsTasksNotFoundException() throws Exception {


        mockMvc.perform(get("/tasks"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Tasks not found"));
    }

    @Test
    @DisplayName("Тест создания новой задачи")
    void createTask() throws Exception {

        mockMvc.perform(post("/tasks").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value(taskDTO.getTitle()))
                .andExpect(jsonPath("$.description").value(taskDTO.getDescription()))
                .andExpect(jsonPath("$.userId").value(taskDTO.getUserId()))
                .andExpect(jsonPath("$.status").value(taskDTO.getTaskStatus()));
    }

    @Test
    @DisplayName("Тест обновления существующей задачи")
    void updateTask() throws Exception {

        Long id = taskRepository.save(task1).getId();

        mockMvc.perform(put("/tasks/{id}", id).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(taskDTO.getTitle()))
                .andExpect(jsonPath("$.description").value(taskDTO.getDescription()))
                .andExpect(jsonPath("$.userId").value(taskDTO.getUserId()))
                .andExpect(jsonPath("$.status").value(taskDTO.getTaskStatus()));
    }

    @Test
    @DisplayName("Тест обработки TaskNotFoundException при обновлении задачи с несуществующим ID")
    void updateTaskThrowsTaskNotFoundException() throws Exception {
        Long invalidId = 999L;

        mockMvc.perform(put("/tasks/{id}", invalidId).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDTO)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(String.format("Task with id: %d not found", invalidId)));
    }

    @Test
    @DisplayName("Тест удаления существующей задачи")
    void deleteTask() throws Exception {
        Long id = taskRepository.save(task1).getId();

        mockMvc.perform(delete("/tasks/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string(String.format("Task with ID: %d was removed successfully", id)));
    }

    @Test
    @DisplayName("Тест обработки TaskNotFoundException при удалении задачи с несуществующим ID")
    void deleteTaskThrowsTaskNotFoundException() throws Exception {
        Long invalidId = 999L;

        mockMvc.perform(delete("/tasks/{id}", invalidId))
                .andExpect(status().isNotFound())
                .andExpect(content().string(String.format("Task with id: %d not found", invalidId)));
    }







    private TaskDTO createTaskDTO() {
        return TaskDTO.builder()
                .title("newTitle")
                .description("newDescription")
                .userId(1L)
                .taskStatus(TaskStatus.NEW.toString())
                .build();
    }
}