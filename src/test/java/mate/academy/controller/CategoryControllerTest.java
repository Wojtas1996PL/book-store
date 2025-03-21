package mate.academy.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import mate.academy.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.dto.category.CategoryDto;
import mate.academy.model.Category;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {
    @Autowired
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired DataSource dataSource,
                          @Autowired WebApplicationContext webApplicationContext)
            throws SQLException {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                        new ClassPathResource("database/add-three-random-categories.sql"));
        }
    }

    @AfterAll
    static void afterAll(@Autowired DataSource dataSource) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/remove-all-categories.sql"));
        }
    }

    @WithMockUser(username = "mila", roles = "ADMIN")
    @DisplayName("Verify that method createCategory works")
    @Sql(scripts = "classpath:database/delete-category-action.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void create_Category_ReturnsCategoryDto() throws Exception {
        CategoryDto actionExpected = new CategoryDto();
        actionExpected.setName("Action");
        actionExpected.setDescription("Action books");

        Category action = new Category();
        action.setName("Action");
        action.setDescription("Action books");
        action.setDeleted(false);

        String jsonRequest = objectMapper.writeValueAsString(action);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post("/api/categories")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        CategoryDto actionActual = objectMapper
                .readValue(result
                        .getResponse()
                        .getContentAsString(),
                        CategoryDto.class);

        assertNotNull(actionActual);
        assertThat(actionActual.getName()).isEqualTo(actionExpected.getName());
        assertThat(actionActual.getDescription()).isEqualTo(actionExpected.getDescription());
    }

    @WithMockUser(username = "bob", roles = "USER")
    @DisplayName("Verify that method getAll works")
    @Sql(scripts = "classpath:database/remove-all-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/add-three-random-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    public void get_AllCategories_ReturnsCategoryListDto() throws Exception {
        CategoryDto scienceFiction = new CategoryDto();
        scienceFiction.setId(1L);
        scienceFiction.setName("Science Fiction");
        scienceFiction.setDescription("Science fiction books");

        CategoryDto fantasy = new CategoryDto();
        fantasy.setId(2L);
        fantasy.setName("Fantasy");
        fantasy.setDescription("Fantasy books");

        CategoryDto horror = new CategoryDto();
        horror.setId(3L);
        horror.setName("Horror");
        horror.setDescription("Horror books");

        List<CategoryDto> categoriesExpected = List.of(scienceFiction, fantasy, horror);
        String url = "/api/categories?page=0&size=10&sort=id";
        MvcResult result = mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        List<CategoryDto> categoriesActual = objectMapper
                .readValue(result.getResponse()
                                .getContentAsByteArray(),
                    new TypeReference<>() {});

        assertNotNull(categoriesActual);
        assertThat(categoriesActual).isEqualTo(categoriesExpected);
    }

    @WithMockUser(username = "bob", roles = "USER")
    @DisplayName("Verify that method findById works")
    @Test
    public void getById_Book_ReturnsBookDto() throws Exception {
        CategoryDto scienceFictionExpected = new CategoryDto();
        scienceFictionExpected.setId(1L);
        scienceFictionExpected.setName("Science Fiction");
        scienceFictionExpected.setDescription("Science fiction books");

        MvcResult result = mockMvc.perform(get("/api/categories/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto scienceFictionActual = objectMapper
                .readValue(result
                        .getResponse()
                        .getContentAsString(),
                        CategoryDto.class);

        assertNotNull(scienceFictionActual);
        assertThat(scienceFictionActual).isEqualTo(scienceFictionExpected);
    }

    @WithMockUser(username = "mila", roles = "ADMIN")
    @DisplayName("Verify that method updateCategory works")
    @Test
    public void updateById_Category_ReturnsCategoryDto() throws Exception {
        CategoryDto fantasyExpected = new CategoryDto();
        fantasyExpected.setId(2L);
        fantasyExpected.setName("Fantasy updated");
        fantasyExpected.setDescription("Fantasy books updated");

        String jsonRequest = objectMapper.writeValueAsString(fantasyExpected);

        MvcResult result = mockMvc.perform(put("/api/categories/{id}", 2L)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto fantasyActual = objectMapper
                .readValue(result
                                .getResponse()
                                .getContentAsString(),
                CategoryDto.class);

        assertNotNull(fantasyActual);
        assertThat(fantasyActual).isEqualTo(fantasyExpected);
    }

    @WithMockUser(username = "bob", roles = "USER")
    @DisplayName("Verify that method getBooksByCategoryId works")
    @Sql(scripts = "classpath:database/add-books-with-category-id.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/remove-all-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void getByCategoryId_Books_ReturnsBookDtoWithoutCategoryIdsList() throws Exception {
        BookDtoWithoutCategoryIds vw = new BookDtoWithoutCategoryIds();
        vw.setId(4L);
        vw.setTitle("Volkswagen");
        vw.setAuthor("CC");
        vw.setIsbn("191");
        vw.setPrice(BigDecimal.valueOf(15));

        BookDtoWithoutCategoryIds audi = new BookDtoWithoutCategoryIds();
        audi.setId(5L);
        audi.setTitle("Audi");
        audi.setAuthor("R8");
        audi.setIsbn("219");
        audi.setPrice(BigDecimal.valueOf(16));

        BookDtoWithoutCategoryIds bmw = new BookDtoWithoutCategoryIds();
        bmw.setId(6L);
        bmw.setTitle("BMW");
        bmw.setAuthor("M3");
        bmw.setIsbn("800");
        bmw.setPrice(BigDecimal.valueOf(17));

        List<BookDtoWithoutCategoryIds> booksExpected = List.of(vw, audi, bmw);

        MvcResult result = mockMvc.perform(get("/api/categories/{id}/books", 4L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<BookDtoWithoutCategoryIds> booksActual = objectMapper
                .readValue(result
                                .getResponse()
                                .getContentAsByteArray(),
                    new TypeReference<>() {});

        assertNotNull(booksActual);
        assertThat(booksActual).isEqualTo(booksExpected);
    }
}
