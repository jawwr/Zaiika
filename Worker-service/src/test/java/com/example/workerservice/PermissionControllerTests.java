package com.example.workerservice;

import com.zaiika.workerservice.WorkerServiceApplication;
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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {WorkerServiceApplication.class})
@AutoConfigureMockMvc
@TestPropertySource("/test-properties.yaml")
@Sql(value = "/delete-user-after-test.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(value = "/add-user-before-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class PermissionControllerTests {
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
    public void testGetAllPermissions() throws Exception {
        mockMvc.perform(get("/api/permission")
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(15));
    }

    @Test
    public void testGetPermissionById() throws Exception {
        mockMvc.perform(get("/api/permission/3")
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("VIEW_MENU"));
    }

    @Test
    public void testGetPermissionByIdWithNotExistingId() throws Exception {
        mockMvc.perform(get("/api/permission/999")
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
