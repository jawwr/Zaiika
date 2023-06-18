package com.example.authservice;

import com.zaiika.authservice.AuthServiceApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {AuthServiceApplication.class})
@AutoConfigureMockMvc
@TestPropertySource("/test-properties.yaml")
@Sql(value = "/delete-user-after-test.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(value = "/add-user-before-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Value("${api-token}")
    private String token;

    @Test
    public void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/api/users")
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(9));
    }

    @Test
    public void testAddRole() throws Exception {
        mockMvc.perform(post("/api/users/99999/role")
                        .header("Authorization", "Bearer " + token)
                        .param("role", "ADMIN"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testRoleNotExist() throws Exception {
        mockMvc.perform(post("/api/users/99999/role")
                        .header("Authorization", "Bearer " + token)
                        .param("role", "SOME ROLE"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Role does not exist"));
    }

    @Test
    public void testDeleteRole() throws Exception {
        mockMvc.perform(delete("/api/users/99999/role")
                        .header("Authorization", "Bearer " + token)
                        .param("role", "DUNGEON_MASTER"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/99999")
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
