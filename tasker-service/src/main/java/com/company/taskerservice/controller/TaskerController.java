package com.company.taskerservice.controller;

import com.company.taskerservice.model.TaskViewModel;
import com.company.taskerservice.service.TaskerServiceLayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

@RestController
@RefreshScope
public class TaskerController {

    @Autowired
    TaskerServiceLayer service;

    public TaskerController(TaskerServiceLayer service) {
        this.service = service;
    }


    @GetMapping("/tasks/category/{category}")
    public List<TaskViewModel> getTasksByCategory(@PathVariable @Size(min = 1,max = 50, message = "Category must be between 1-50 characters") String category){
        return service.fetchTasksByCategory(category);
    }

    @GetMapping("/tasks")
    public List<TaskViewModel> getAllTasks(){
        return service.fetchAllTasks();
    }

    @GetMapping("/tasks/{id}")
    public TaskViewModel getTaskById(@PathVariable int id){
        if (service.fetchTask(id) == null) {
            throw new IllegalArgumentException("Please enter a valid task id");
        }
        return service.fetchTask(id);
    }

    @PutMapping("/tasks")
    public void updateTask(@RequestBody @Valid TaskViewModel taskViewModel){
        service.updateTask(taskViewModel);
    }

    @PostMapping("/tasks")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskViewModel createTask(@RequestBody @Valid TaskViewModel taskViewModel){
        return service.newTask(taskViewModel);
    }

    @RequestMapping(value = "/tasks/{id}", method = RequestMethod.DELETE)
    public void deleteTask(@PathVariable int id) {
        service.deleteTask(id);
    }
}
