package com.company.taskerservice.service;

import com.company.taskerservice.dao.TaskerDao;
import com.company.taskerservice.feign.AdserverFeign;
import com.company.taskerservice.model.Task;
import com.company.taskerservice.model.TaskViewModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;


public class TaskerServiceLayerTest {


    TaskerServiceLayer serviceLayer;
    @Autowired
    AdserverFeign adserverFeign;
    @Autowired
    TaskerDao taskerDao;

    @Before
    public void setUp(){
        setUpFeignMocks();
        setUpDaoMocks();
        serviceLayer = new TaskerServiceLayer(adserverFeign,taskerDao);
    }


    @Test
    public void createGetGetAllTask(){
        TaskViewModel tvm = new TaskViewModel();
        tvm.setDescription("description");
        tvm.setCreateDate(LocalDate.now());
        tvm.setDueDate(LocalDate.now());
        tvm.setAdvertisement(adserverFeign.getAd());
        tvm.setCategory("category");

        tvm = serviceLayer.newTask(tvm);

        assertEquals(tvm, serviceLayer.fetchTask(tvm.getId()));

        assertEquals(1, serviceLayer.fetchAllTasks().size());
    }

    @Test
    public void getTaskByCategory(){
        TaskViewModel tvm = new TaskViewModel();
        tvm.setDescription("description");
        tvm.setCreateDate(LocalDate.now());
        tvm.setDueDate(LocalDate.now());
        tvm.setAdvertisement(adserverFeign.getAd());
        tvm.setCategory("category");

        tvm = serviceLayer.newTask(tvm);

        assertEquals(tvm, serviceLayer.fetchTasksByCategory(tvm.getCategory()).get(0));
    }




    public void setUpFeignMocks(){
        adserverFeign = mock(AdserverFeign.class);
        doReturn("random ad").when(adserverFeign).getAd();
    }

    public void setUpDaoMocks(){
        taskerDao = mock(TaskerDao.class);

        Task task = new Task();
        task.setDescription("description");
        task.setCreateDate(LocalDate.now());
        task.setDueDate(LocalDate.now());
        task.setCategory("category");

        Task task1 = new Task();
        task1.setId(1);
        task1.setDescription("description");
        task1.setCreateDate(LocalDate.now());
        task1.setDueDate(LocalDate.now());
        task1.setCategory("category");

        List<Task> taskList = new ArrayList<>();
        taskList.add(task1);

        List<Task> emptyTaskList = new ArrayList<>();

        doReturn(task1).when(taskerDao).createTask(task);
        doReturn(task1).when(taskerDao).getTask(task1.getId());
        doReturn(taskList).when(taskerDao).getAllTasks();
        doReturn(taskList).when(taskerDao).getTasksByCategory("category");
    }

}
