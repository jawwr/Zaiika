package com.example.authservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.zaiika.authservice.AuthServiceApplication;
import com.zaiika.authservice.model.authCredentials.LoginCredential;
import com.zaiika.authservice.model.authCredentials.RegisterCredential;
import com.zaiika.authservice.model.worker.WorkerCredential;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {AuthServiceApplication.class})
@AutoConfigureMockMvc
@TestPropertySource("/test-properties.yaml")
@Sql(value = "/delete-user-after-test.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(value = "/add-user-before-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class AuthControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testRegister() throws Exception {
        RegisterCredential credential = new RegisterCredential(
                "test LOGIN",
                "1234",
                "test name",
                "test surname");
        var body = convertObjectToJson(credential);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").isString());
    }

    @Test
    public void testUserAlreadyExist() throws Exception {
        RegisterCredential credential = new RegisterCredential(
                "TEST login",
                "1234",
                "test name",
                "test surname");
        var body = convertObjectToJson(credential);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("User already exist"));
    }

    @Test
    public void testUserNotExist() throws Exception {
        LoginCredential credential = new LoginCredential("tl", "pass");
        var body = convertObjectToJson(credential);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUserLogin() throws Exception {
        LoginCredential credential = new LoginCredential("TEST login", "pass");
        var body = convertObjectToJson(credential);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isString());
    }

    @Test
    public void testEmployeeLogin() throws Exception {
        WorkerCredential credential = new WorkerCredential("pass");
        var body = convertObjectToJson(credential);
        mockMvc.perform(post("/api/auth/99999/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isString());
    }

    @Test
    public void testWorkerNotExist() throws Exception {
        WorkerCredential credential = new WorkerCredential("pas");
        var body = convertObjectToJson(credential);
        mockMvc.perform(post("/api/auth/99999/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Worker with this pin not exist"));
    }

    private <T> String convertObjectToJson(T obj) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        return writer.writeValueAsString(obj);
    }
}
