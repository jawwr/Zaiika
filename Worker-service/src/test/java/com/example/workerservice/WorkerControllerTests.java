package com.example.workerservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.zaiika.workerservice.WorkerServiceApplication;
import com.zaiika.workerservice.model.UserDto;
import com.zaiika.workerservice.model.WorkerCredentials;
import com.zaiika.workerservice.service.user.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {WorkerServiceApplication.class})
@AutoConfigureMockMvc
@TestPropertySource("/test-properties.yaml")
@Sql(value = "/delete-user-after-test.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(value = "/add-user-before-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class WorkerControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Value("${api-token}")
    private String token;
    @MockBean
    private UserServiceImpl userService;

    @Before
    public void mock() {
        Mockito.when(userService.getUser()).thenReturn(new UserDto(99999L, 99999));
    }

    @Test
    public void testGetAllWorkers() throws Exception {
        mockMvc.perform(get("/api/workers").header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    public void testCreateWorker() throws Exception {
        WorkerCredentials credentials = new WorkerCredentials(
                99998,
                99999,
                "9876",
                "name",
                "sur",
                "patr",
                "TESTLOGIN",
                "TEST PLACE ROLE"
        );
        var body = convertObjectToJson(credentials);
        Mockito.when(userService.saveWorker(credentials)).thenReturn(99999L);
        mockMvc.perform(post("/api/workers")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void testDeleteWorker() throws Exception {
        mockMvc.perform(delete("/api/workers/99999")
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testGetWorkerById() throws Exception {
        mockMvc.perform(get("/api/workers/99999")
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(99999));
    }

    @Test
    public void testAddWorkerRole() throws Exception {
        mockMvc.perform(post("/api/workers/99999")
                        .param("roleName", "TEST PLACE ROLE")
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testAddRoleToNotExistsWorker() throws Exception {
        mockMvc.perform(post("/api/workers/9999")
                        .param("roleName", "TEST PLACE ROLE")
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddWorkerNotExistsRole() throws Exception {
        mockMvc.perform(post("/api/workers/99999")
                        .param("roleName", "NOT_EXIST_ROLE")
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateWorkerWithWrongPinDigit() throws Exception {
        WorkerCredentials credentials = new WorkerCredentials(
                99998,
                99999,
                "98769",
                "name",
                "sur",
                "patr",
                "TESTLOGIN",
                "TEST PLACE ROLE"
        );
        var body = convertObjectToJson(credentials);
        Mockito.when(userService.saveWorker(credentials)).thenReturn(99999L);
        mockMvc.perform(post("/api/workers")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateWorkerWithWrongPinChars() throws Exception {
        WorkerCredentials credentials = new WorkerCredentials(
                99998,
                99999,
                "987C",
                "name",
                "sur",
                "patr",
                "TESTLOGIN",
                "TEST PLACE ROLE"
        );
        var body = convertObjectToJson(credentials);
        Mockito.when(userService.saveWorker(credentials)).thenReturn(99999L);
        mockMvc.perform(post("/api/workers")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    private <T> String convertObjectToJson(T obj) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        return writer.writeValueAsString(obj);
    }
}
