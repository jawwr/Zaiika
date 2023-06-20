package com.zaiika.placeservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.zaiika.placeservice.model.place.Ingredient;
import com.zaiika.placeservice.model.place.Product;
import com.zaiika.placeservice.model.place.ProductModification;
import com.zaiika.placeservice.model.place.ProductModificationCategory;
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

import java.util.List;

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
public class ProductControllerTests {
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
    public void testGetAllProduct() throws Exception {
        mockMvc.perform(get("/api/menu/99999/products")
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$.[0].title").value("TEST PRODUCT TITLE"))
                .andExpect(jsonPath("$.[0].price").value(200.5));
    }

    @Test
    public void testGetAllProductWithNotExistsMenu() throws Exception {
        mockMvc.perform(get("/api/menu/9999/products")
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Menu with id 9999 does not exist"));
    }

    @Test
    public void testGetProductById() throws Exception {
        mockMvc.perform(get("/api/menu/99999/products/99999")
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(99999));
    }

    @Test
    public void testGetProductByIdWithNotExistsProduct() throws Exception {
        mockMvc.perform(get("/api/menu/99999/products/9999")
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Product with id 9999 does not exist"));
    }

    @Test
    public void testGetProductByIdWithNotExistsMenu() throws Exception {
        mockMvc.perform(get("/api/menu/9999/products/99999")
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Menu with id 9999 does not exist"));
    }

    @Test
    public void testCreateProductWithoutCompAndMod() throws Exception {
        Product product = new Product(
                0,
                "test title",
                null,
                null,
                null,
                123.75);
        var body = convertObjectToJson(product);
        mockMvc.perform(post("/api/menu/99999/products")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(product.getTitle()))
                .andExpect(jsonPath("$.price").value(product.getPrice()));
    }

    @Test
    public void testCreateProductWithNotExistsMenu() throws Exception {
        Product product = new Product(
                0,
                "test title",
                null,
                null,
                null,
                123.75);
        var body = convertObjectToJson(product);
        mockMvc.perform(post("/api/menu/9999/products")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Menu with id 9999 does not exist"));
    }

    @Test
    public void testCreateProduct() throws Exception {
        Product product = new Product(
                0,
                "test title",
                List.of(
                        new Ingredient(0, "TEST INGREDIENT TITLE 1", 1000.2, 802.76, null, true),
                        new Ingredient(0, "TEST INGREDIENT TITLE 2", 1000.2, 802.76, null, false)
                ),
                List.of(
                        new ProductModificationCategory(
                                0,
                                null,
                                List.of(
                                        new ProductModification(0, null, "TEST PRODUCT MODIFICATION", 200.9)
                                ),
                                "TEST MODIFICATION CATEGORY TITLE"
                        )
                ),
                null,
                123.75);
        var body = convertObjectToJson(product);
        mockMvc.perform(post("/api/menu/99999/products")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(product.getTitle()))
                .andExpect(jsonPath("$.price").value(product.getPrice()))
                .andExpect(jsonPath("$.composition.size()").value(2))
                .andExpect(jsonPath("$.composition.[0].title").value(product.getComposition().get(0).getTitle()))
                .andExpect(jsonPath("$.modifications.size()").value(1))
                .andExpect(jsonPath("$.modifications.[0].title").value(product.getModifications().get(0).getTitle()))
                .andExpect(jsonPath("$.modifications.[0].modification.[0].title").value(product.getModifications().get(0).getModification().get(0).getTitle()));
    }

    @Test
    public void testDeleteProduct() throws Exception {
        mockMvc.perform(delete("/api/menu/99999/products/99999")
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteProductWithNotExistsId() throws Exception {
        mockMvc.perform(delete("/api/menu/99999/products/9999")
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Product with id 9999 does not exist"));
    }

    @Test
    public void testDeleteProductWithNotExistsMenu() throws Exception {
        mockMvc.perform(delete("/api/menu/9999/products/99999")
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Menu with id 9999 does not exist"));
    }

    @Test
    public void testUpdateProductWithoutCompAndMod() throws Exception {
        Product product = new Product(
                0,
                "test title",
                null,
                null,
                null,
                123.75);
        var body = convertObjectToJson(product);
        mockMvc.perform(put("/api/menu/99999/products/99999")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(product.getTitle()))
                .andExpect(jsonPath("$.price").value(product.getPrice()));
    }

    @Test
    public void testUpdateProductWithNotExistsMenu() throws Exception {
        Product product = new Product(
                0,
                "test title",
                null,
                null,
                null,
                123.75);
        var body = convertObjectToJson(product);
        mockMvc.perform(put("/api/menu/9999/products/99999")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Menu with id 9999 does not exist"));
    }

    @Test
    public void testUpdateProductWithNotExistsId() throws Exception {
        Product product = new Product(
                0,
                "test title",
                null,
                null,
                null,
                123.75);
        var body = convertObjectToJson(product);
        mockMvc.perform(put("/api/menu/99999/products/9999")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Product with id 9999 does not exist"));
    }

    @Test
    public void testUpdateProduct() throws Exception {
        Product product = new Product(
                0,
                "test title",
                List.of(
                        new Ingredient(0, "TEST INGREDIENT TITLE 1", 1000.2, 802.76, null, true),
                        new Ingredient(0, "TEST INGREDIENT TITLE 2", 1000.2, 802.76, null, false)
                ),
                List.of(
                        new ProductModificationCategory(
                                0,
                                null,
                                List.of(
                                        new ProductModification(0, null, "TEST PRODUCT MODIFICATION", 200.9)
                                ),
                                "TEST MODIFICATION CATEGORY TITLE"
                        )
                ),
                null,
                123.75);
        var body = convertObjectToJson(product);
        mockMvc.perform(put("/api/menu/99999/products/99999")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(product.getTitle()))
                .andExpect(jsonPath("$.price").value(product.getPrice()))
                .andExpect(jsonPath("$.composition.size()").value(2))
                .andExpect(jsonPath("$.composition.[0].title").value(product.getComposition().get(0).getTitle()))
                .andExpect(jsonPath("$.modifications.size()").value(1))
                .andExpect(jsonPath("$.modifications.[0].title").value(product.getModifications().get(0).getTitle()))
                .andExpect(jsonPath("$.modifications.[0].modification.[0].title").value(product.getModifications().get(0).getModification().get(0).getTitle()));
    }

    private <T> String convertObjectToJson(T obj) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        return writer.writeValueAsString(obj);
    }
}
