package com.kuvarin.taskcrud.controller;

import com.kuvarin.starter.aspect.annotation.LogController;
import com.kuvarin.taskcrud.dto.TaskDTO;
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

    @LogController
    @GetMapping("/{id}")
    public TaskDTO getTask(@PathVariable Long id) {
        return taskService.getTask(id);
    }

    @LogController
    @GetMapping
    public List<TaskDTO> getTasks() {
        return taskService.getAll();
    }

    @LogController
    @PostMapping
    public TaskDTO createTask(@RequestBody TaskDTO task) {
        return taskService.saveTask(task);
    }

    @LogController
    @PutMapping("/{id}")
    public TaskDTO updateTask(@PathVariable Long id, @RequestBody TaskDTO task) {
        return taskService.updateTask(id, task);
    }

    @LogController
    @DeleteMapping("/{id}")
    public String deleteTask(@PathVariable Long id) {
        return taskService.deleteTask(id);
    }
}
