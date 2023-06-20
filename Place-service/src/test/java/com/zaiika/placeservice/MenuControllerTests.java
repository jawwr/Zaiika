package com.zaiika.placeservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.zaiika.placeservice.model.place.Menu;
import com.zaiika.placeservice.model.utils.UserDto;
import com.zaiika.placeservice.service.users.UserServiceImpl;
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
@SpringBootTest(classes = {PlaceServiceApplication.class})
@AutoConfigureMockMvc
@TestPropertySource("/test-properties.yaml")
@Sql(value = "/delete-user-after-test.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(value = "/add-user-before-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class MenuControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Value("${api-token}")
    private String token;

    @MockBean
    private UserServiceImpl userService;

    @Before
    public void mock() {
        Mockito.when(userService.getUser()).thenReturn(new UserDto(99999));
    }

    @Test
    public void testGetAllMenu() throws Exception {
        mockMvc.perform(get("/api/site/99999/menu")
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    public void testGetAllMenuWithNotExistsSite() throws Exception {
        mockMvc.perform(get("/api/site/9999/menu")
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Site with id 9999 does not exist"));
    }

    @Test
    public void testGetMenuById() throws Exception {//работает неправильно из-за кеширования
        mockMvc.perform(get("/api/site/99999/menu/99999")
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(99999));
    }

    @Test
    public void testGetMenuByIdWithNotExistingSite() throws Exception {
        mockMvc.perform(get("/api/site/9999/menu/99999")
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Site with id 9999 does not exist"));
    }

    @Test
    public void testGetMenuByIdWithNotExistingMenu() throws Exception {
        mockMvc.perform(get("/api/site/99999/menu/9999")
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Menu with id 9999 does not exist"));
    }

    @Test
    public void testCreateMenu() throws Exception {
        Menu menu = new Menu(0, "test menu title", null);
        var body = convertObjectToJson(menu);
        mockMvc.perform(post("/api/site/99999/menu")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(menu.getTitle()));
    }

    @Test
    public void testCreateMenuWithNotExistSite() throws Exception {
        Menu menu = new Menu(0, "test menu title", null);
        var body = convertObjectToJson(menu);
        mockMvc.perform(post("/api/site/9999/menu")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Site with id 9999 does not exist"));
    }

    @Test
    public void testUpdateMenu() throws Exception {
        Menu menu = new Menu(0, "update test menu title", null);
        var body = convertObjectToJson(menu);
        mockMvc.perform(put("/api/site/99999/menu/99999")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(menu.getTitle()));
    }

    @Test
    public void testUpdateMenuWithNotExistSite() throws Exception {
        Menu menu = new Menu(0, "update test menu title", null);
        var body = convertObjectToJson(menu);
        mockMvc.perform(put("/api/site/9999/menu/99999")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Site with id 9999 does not exist"));
    }

    @Test
    public void testUpdateMenuWithNotExistMenuId() throws Exception {
        Menu menu = new Menu(0, "update test menu title", null);
        var body = convertObjectToJson(menu);
        mockMvc.perform(put("/api/site/99999/menu/9999")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Menu with id 9999 does not exist"));
    }

    @Test
    public void testDeleteMenu() throws Exception {
        mockMvc.perform(delete("/api/site/99999/menu/99999")
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteMenuWithNotExistSite() throws Exception {
        mockMvc.perform(delete("/api/site/9999/menu/99999")
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Site with id 9999 does not exist"));
    }

    @Test
    public void testDeleteMenuWithNotExistMenuId() throws Exception {
        mockMvc.perform(delete("/api/site/99999/menu/9999")
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Menu with id 9999 does not exist"));
    }

    private <T> String convertObjectToJson(T obj) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        return writer.writeValueAsString(obj);
    }
}
