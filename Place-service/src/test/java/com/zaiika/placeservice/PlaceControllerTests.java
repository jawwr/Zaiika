package com.zaiika.placeservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.zaiika.placeservice.model.place.Place;
import com.zaiika.placeservice.model.utils.UserDto;
import com.zaiika.placeservice.service.permission.CustomPermissionEvaluator;
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
public class PlaceControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Value("${api-token}")
    private String token;

    @MockBean
    private CustomPermissionEvaluator permissionEvaluator;

    @MockBean
    private UserServiceImpl userService;

    @Before
    public void mock() {
        Mockito.when(permissionEvaluator.hasPermission(null, null, "VIEW_PLACE")).thenReturn(true);
        Mockito.when(permissionEvaluator.hasPermission("VIEW_PLACE")).thenReturn(true);
        Mockito.when(userService.getUser()).thenReturn(new UserDto(99999));
    }

    @Test
    public void testGetAllPlaces() throws Exception {
        mockMvc.perform(get("/api/places")
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(4));
    }

    @Test
    public void testCreatePlace() throws Exception {
        Place place = new Place(0, "TEST PLACE", 99999);
        var body = convertObjectToJson(place);
        mockMvc.perform(post("/api/places")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(place.getName()));
    }

    @Test
    public void testDeletePlace() throws Exception {
        mockMvc.perform(delete("/api/places/99999")
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testGetPlaceById() throws Exception {
        mockMvc.perform(get("/api/places/99999")
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("TEST PLACE"));
    }

    @Test
    public void testUpdatePlace() throws Exception {
        Place place = new Place(0, "TEST PLACE", 99999);
        var body = convertObjectToJson(place);
        mockMvc.perform(put("/api/places/99999")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(place.getName()));
    }

    @Test
    public void testUpdatePlaceWithNotExistId() throws Exception {
        Place place = new Place(0, "TEST PLACE", 99999);
        var body = convertObjectToJson(place);
        mockMvc.perform(put("/api/places/99998")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdatePlaceWithNotExistOwner() throws Exception {
        Mockito.when(userService.getUser()).thenReturn(new UserDto(99998));
        Place place = new Place(0, "TEST PLACE", 99999);
        var body = convertObjectToJson(place);
        mockMvc.perform(put("/api/places/99999")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
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
