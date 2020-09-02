package com.company.taskerservice.service;


import com.company.taskerservice.dao.TaskerDao;
import com.company.taskerservice.feign.AdserverFeign;
import com.company.taskerservice.model.Task;
import com.company.taskerservice.model.TaskViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TaskerServiceLayer {

    @Autowired
    TaskerDao dao;

    @Autowired
    private final AdserverFeign adserverFeign;

    public TaskerServiceLayer(AdserverFeign adserverFeign, TaskerDao taskerDao) {
        this.adserverFeign = adserverFeign;
        this.dao = taskerDao;
    }


    public TaskViewModel fetchTask(int id) {
        if (id < 1) {
            throw new IllegalArgumentException("please enter a valid task id");
        }

        Task task = dao.getTask(id);

        return buildTaskViewModel(task);
    }

    public List<TaskViewModel> fetchAllTasks() {
        List<Task> taskList = dao.getAllTasks();
        List<TaskViewModel> tvmList = new ArrayList<>();
        for (Task task:
             taskList) {
            tvmList.add(buildTaskViewModel(task));
        }
        return tvmList;
    }

    public List<TaskViewModel> fetchTasksByCategory(String category) {
        List<Task> taskList = dao.getTasksByCategory(category);
        if (taskList.size() == 0) {
            throw new IllegalArgumentException("Please enter a valid category");
        }
        List<TaskViewModel> tvmList = new ArrayList<>();
        for (Task task:
                taskList) {
            tvmList.add(buildTaskViewModel(task));
        }
        return tvmList;
    }

    public TaskViewModel newTask(TaskViewModel taskViewModel) {
        Task task = new Task();
        task.setDescription(taskViewModel.getDescription());
        task.setCreateDate(taskViewModel.getCreateDate());
        task.setDueDate(taskViewModel.getDueDate());
        task.setCategory(taskViewModel.getCategory());

        task = dao.createTask(task);

        return buildTaskViewModel(task);
    }

    public void deleteTask(int id) {
        if (id < 1) {
            throw new IllegalArgumentException("Please enter a valid task id");
        }
        dao.deleteTask(id);
    }

    public void updateTask(TaskViewModel taskViewModel) {
        Task task = new Task();
        task.setId(taskViewModel.getId());
        task.setDescription(task.getDescription());
        task.setCreateDate(taskViewModel.getCreateDate());
        task.setDueDate(taskViewModel.getDueDate());
        task.setCategory(taskViewModel.getCategory());

        dao.updateTask(task);
    }

    public TaskViewModel buildTaskViewModel(Task task){
        TaskViewModel tvm = new TaskViewModel();
        tvm.setId(task.getId());
        tvm.setDescription(task.getDescription());
        tvm.setCreateDate(task.getCreateDate());
        tvm.setDueDate(task.getDueDate());
        tvm.setAdvertisement(adserverFeign.getAd());
        tvm.setCategory(task.getCategory());
        return  tvm;
    }
}
