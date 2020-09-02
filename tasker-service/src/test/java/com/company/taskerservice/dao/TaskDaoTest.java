package com.company.taskerservice.dao;


import com.company.taskerservice.model.Task;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TaskDaoTest {

    @Autowired
    TaskerDao taskerDao;

    @Before
    public void setUp(){
        List<Task> taskList = taskerDao.getAllTasks();

        for (Task task:
             taskList) {
            taskerDao.deleteTask(task.getId());
        }
    }

    @Test
    public void createGetGetAllDeleteTask(){
        Task task = new Task();
        task.setDescription("description");
        task.setCreateDate(LocalDate.now());
        task.setDueDate(LocalDate.now());
        task.setCategory("category");
        task = taskerDao.createTask(task);

        assertEquals(task,taskerDao.getTask(task.getId()));

        assertEquals(1,taskerDao.getAllTasks().size());

        taskerDao.deleteTask(task.getId());

        assertEquals(0,taskerDao.getAllTasks().size());
    }

    @Test
    public void updateTaskTest(){
        Task task = new Task();
        task.setDescription("description");
        task.setCreateDate(LocalDate.now());
        task.setDueDate(LocalDate.now());
        task.setCategory("category");
        task = taskerDao.createTask(task);

        task.setCategory("different category");

        taskerDao.updateTask(task);

        assertEquals("different category", taskerDao.getTask(task.getId()).getCategory());
    }

    @Test
    public void findTaskByCategory(){
        Task task = new Task();
        task.setDescription("description");
        task.setCreateDate(LocalDate.now());
        task.setDueDate(LocalDate.now());
        task.setCategory("category");
        task = taskerDao.createTask(task);

        assertEquals(task, taskerDao.getTasksByCategory("category").get(0));
    }


}
