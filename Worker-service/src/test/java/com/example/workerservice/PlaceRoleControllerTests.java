package com.example.workerservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.zaiika.workerservice.WorkerServiceApplication;
import com.zaiika.workerservice.model.PlaceRole;
import com.zaiika.workerservice.model.UserDto;
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
public class PlaceRoleControllerTests {
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
    public void testGetAllPlaceRoles() throws Exception {
        mockMvc.perform(get("/api/role")
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    public void testCreateNewRoles() throws Exception {
        PlaceRole placeRole = new PlaceRole(0, "test new place role", 0, null);
        var body = convertObjectToJson(placeRole);
        mockMvc.perform(post("/api/role")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(placeRole.getName()));
    }

    @Test
    public void testDeletePlaceRole() throws Exception {
        mockMvc.perform(delete("/api/role/99999")
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdatePlaceRole() throws Exception {
        PlaceRole placeRole = new PlaceRole(0, "test new place role", 0, null);
        var body = convertObjectToJson(placeRole);
        mockMvc.perform(put("/api/role/99999")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(placeRole.getName()));
    }

    private <T> String convertObjectToJson(T obj) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        return writer.writeValueAsString(obj);
    }
}
