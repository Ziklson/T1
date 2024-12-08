package com.kuvarin.taskcrud.controller;

import com.kuvarin.taskcrud.dto.TaskRequestDTO;
import com.kuvarin.taskcrud.dto.TaskResponseDTO;
import com.kuvarin.taskcrud.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController (TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{id}")
    public TaskResponseDTO getTask(@PathVariable Long id) {
        return taskService.getTask(id);
    }

    @GetMapping
    public List<TaskResponseDTO> getTasks() {
        return taskService.getAll();
    }

    @PostMapping
    public TaskResponseDTO createTask(@RequestBody TaskRequestDTO task) {
        return taskService.saveTask(task);
    }

    @PutMapping("/{id}")
    public TaskResponseDTO updateTask(@PathVariable Long id, @RequestBody TaskRequestDTO task) {
        return taskService.updateTask(id, task);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}
