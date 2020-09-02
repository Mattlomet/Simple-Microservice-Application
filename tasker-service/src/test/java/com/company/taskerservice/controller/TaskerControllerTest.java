package com.company.taskerservice.controller;

import com.company.taskerservice.model.TaskViewModel;
import com.company.taskerservice.service.TaskerServiceLayer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@WebMvcTest(TaskerController.class)
public class TaskerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    TaskerServiceLayer serviceLayer;



    ObjectMapper mapper = new ObjectMapper();


    TaskViewModel tvm = new TaskViewModel();
    TaskViewModel tvm1 = new TaskViewModel();
    TaskViewModel tvmMalFormed = new TaskViewModel();
    TaskViewModel tvmMalFormed1 = new TaskViewModel();



    @Before
    public void setUp(){
        tvm.setDescription("description");
        tvm.setCreateDate(LocalDate.of(19,11,17));
        tvm.setDueDate(LocalDate.of(19,11,17));
        tvm.setAdvertisement("");
        tvm.setCategory("category");

        tvm1.setId(1);
        tvm1.setDescription("description");
        tvm1.setCreateDate(LocalDate.of(19,11,17));
        tvm1.setDueDate(LocalDate.of(19,11,17));
        tvm1.setAdvertisement("random ad");
        tvm1.setCategory("category");

        tvmMalFormed.setDescription("");
        tvmMalFormed.setCreateDate(LocalDate.of(19,11,17));
        tvmMalFormed.setDueDate(LocalDate.of(19,11,17));
        tvmMalFormed.setAdvertisement("random ad");
        tvmMalFormed.setCategory("");

        tvmMalFormed1.setId(1);
        tvmMalFormed1.setDescription("");
        tvmMalFormed1.setCreateDate(LocalDate.of(19,11,17));
        tvmMalFormed1.setDueDate(LocalDate.of(19,11,17));
        tvmMalFormed1.setAdvertisement("random ad");
        tvmMalFormed1.setCategory("");

        setUpServiceMocks();
    }

    @Test
    public void createTaskShouldReturnTaskViewModel() throws Exception {
        String input = mapper.writeValueAsString(tvm);

        String output = mapper.writeValueAsString(tvm1);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                        .andDo(print())
                        .andExpect(status().isCreated())
                        .andExpect(content().json(output));

    }


    @Test
    public void getTaskByIdShouldReturnTaskViewModel() throws Exception{
        String output = mapper.writeValueAsString(tvm1);

        mockMvc.perform(get("/tasks/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(output));
    }

    @Test
    public void getAllTaskShouldReturnTaskViewModelList() throws Exception{
        List<TaskViewModel> taskViewModelList = new ArrayList<>();
        taskViewModelList.add(tvm1);
        String output = mapper.writeValueAsString(taskViewModelList);

        mockMvc.perform(get("/tasks"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(output));
    }

    @Test
    public void getTaskByCategoryShouldReturnTaskViewModel() throws Exception {

        List<TaskViewModel> taskViewModelList = new ArrayList<>();
        taskViewModelList.add(tvm1);
        String output = mapper.writeValueAsString(taskViewModelList);

        mockMvc.perform(get("/tasks/category/category"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(output));
    }

    @Test
    public void deleteTaskShouldReturn200Status() throws Exception {
        mockMvc.perform(delete("/tasks/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    public void getTaskByWrongIdShouldReturnUprocessEntity() throws Exception {
        String output = mapper.writeValueAsString(tvm1);

        mockMvc.perform(get("/tasks/-1"))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void createMalFormedTaskShouldReturnUnprocessEntity() throws Exception {
        String input = mapper.writeValueAsString(tvmMalFormed);


        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(input))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void updateMalFormedTaskShouldReturnUnprocessEntity() throws Exception {
        String input = mapper.writeValueAsString(tvmMalFormed1);


        mockMvc.perform(put("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(input))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }



    private void setUpServiceMocks() {
        List<TaskViewModel> taskViewModelList = new ArrayList<>();
        taskViewModelList.add(tvm1);

        doReturn(tvm1).when(serviceLayer).newTask(tvm);
        doReturn(tvm1).when(serviceLayer).fetchTask(tvm1.getId());
        doReturn(taskViewModelList).when(serviceLayer).fetchAllTasks();
        doReturn(taskViewModelList).when(serviceLayer).fetchTasksByCategory(tvm1.getCategory());
    }

}
