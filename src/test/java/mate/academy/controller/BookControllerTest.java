package mate.academy.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import mate.academy.dto.book.BookDto;
import mate.academy.dto.book.CreateBookRequestDto;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
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
                    new ClassPathResource("database/add-three-random-books.sql"));
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
                    new ClassPathResource("database/remove-all-books.sql"));
        }
    }

    @WithMockUser(username = "bob", roles = "USER")
    @Test
    @DisplayName("Verify that method findAll works")
    @Sql(scripts = "classpath:database/remove-all-books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/add-three-random-books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAll_Books_ReturnsBookDtoList() throws Exception {
        BookDto starWars = new BookDto();
        starWars.setId(1L);
        starWars.setTitle("Star Wars");
        starWars.setAuthor("George Lucas");
        starWars.setIsbn("123");
        starWars.setPrice(BigDecimal.valueOf(13));
        starWars.setCategories(new HashSet<>());

        BookDto jedi = new BookDto();
        jedi.setId(2L);
        jedi.setTitle("Jedi");
        jedi.setAuthor("Lucas");
        jedi.setIsbn("12111");
        jedi.setPrice(BigDecimal.valueOf(14));
        jedi.setCategories(new HashSet<>());

        BookDto gandhi = new BookDto();
        gandhi.setId(3L);
        gandhi.setTitle("Gandhi");
        gandhi.setAuthor("Wright");
        gandhi.setIsbn("13");
        gandhi.setPrice(BigDecimal.valueOf(15));
        gandhi.setCategories(new HashSet<>());

        List<BookDto> booksExpected = List.of(starWars, jedi, gandhi);
        MvcResult result = mockMvc.perform(get("/api/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        BookDto[] booksActual = objectMapper
                .readValue(result
                        .getResponse()
                        .getContentAsByteArray(),
                        BookDto[].class);

        assertThat(Arrays.stream(booksActual).toList()).isEqualTo(booksExpected);
        assertNotNull(booksActual);
    }

    @WithMockUser(username = "bob", roles = "USER")
    @Sql(scripts = "classpath:database/add-book-dune.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/delete-book-dune.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    @DisplayName("Verify that method getBookById works")
    public void getById_Book_ReturnsBookDto() throws Exception {
        BookDto duneExpected = new BookDto();
        duneExpected.setId(4L);
        duneExpected.setTitle("Dune");
        duneExpected.setAuthor("Dennis");
        duneExpected.setIsbn("12");
        duneExpected.setPrice(BigDecimal.valueOf(13));
        duneExpected.setCategories(new HashSet<>());

        MvcResult result = mockMvc.perform(get("/api/books/{id}", 4L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        BookDto duneActual = objectMapper
                .readValue(result.getResponse()
                        .getContentAsString(),
                        BookDto.class);

        assertThat(duneActual).isEqualTo(duneExpected);
        assertNotNull(duneActual);
    }

    @WithMockUser(username = "mila", roles = "ADMIN")
    @Test
    @Sql(scripts = "classpath:database/delete-book-spider-man.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Verify that method createBook works")
    public void create_Book_ReturnsBookDto() throws Exception {
        CreateBookRequestDto spiderMan = new CreateBookRequestDto();
        spiderMan.setTitle("Spider-man");
        spiderMan.setDescription("Superhero book");
        spiderMan.setAuthor("Stan Lee");
        spiderMan.setIsbn("200");
        spiderMan.setPrice(BigDecimal.valueOf(100));

        BookDto spiderManExpected = new BookDto();
        spiderManExpected.setTitle("Spider-man");
        spiderManExpected.setDescription("Superhero book");
        spiderManExpected.setAuthor("Stan Lee");
        spiderManExpected.setIsbn("200");
        spiderManExpected.setPrice(BigDecimal.valueOf(100));
        spiderManExpected.setCategories(new HashSet<>());

        String jsonRequest = objectMapper.writeValueAsString(spiderMan);

        MvcResult result = mockMvc
                .perform(post("/api/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        BookDto spiderManActual = objectMapper
                .readValue(result
                        .getResponse()
                        .getContentAsString(),
                        BookDto.class);

        spiderManExpected.setId(spiderManActual.getId());

        assertNotNull(spiderManActual);
        assertThat(spiderManActual).isEqualTo(spiderManExpected);
    }

    @WithMockUser(username = "mila", roles = "ADMIN")
    @Test
    @DisplayName("Verify that method updateBook works")
    public void update_Book_ReturnsBookDto() throws Exception {
        BookDto starWarsExpected = new BookDto();
        starWarsExpected.setId(1L);
        starWarsExpected.setTitle("SW");
        starWarsExpected.setIsbn("300");
        starWarsExpected.setAuthor("GL");
        starWarsExpected.setPrice(BigDecimal.valueOf(500));
        starWarsExpected.setCategories(new HashSet<>());

        CreateBookRequestDto starWars = new CreateBookRequestDto();
        starWars.setTitle("SW");
        starWars.setIsbn("300");
        starWars.setAuthor("GL");
        starWars.setPrice(BigDecimal.valueOf(500));

        String jsonRequest = objectMapper.writeValueAsString(starWars);

        MvcResult result = mockMvc.perform(put("/api/books/{id}", 1L)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto starWarsActual = objectMapper
                .readValue(result.getResponse().getContentAsString(),
                        BookDto.class);

        assertNotNull(starWarsActual);
        assertThat(starWarsActual).isEqualTo(starWarsExpected);
    }

    @WithMockUser(username = "bob", roles = "USER")
    @Test
    @DisplayName("Verify that method searchBooks works")
    public void search_Books_ReturnsBookListDto() throws Exception {
        BookDto jedi = new BookDto();
        jedi.setId(2L);
        jedi.setTitle("Jedi");
        jedi.setAuthor("Lucas");
        jedi.setIsbn("12111");
        jedi.setPrice(BigDecimal.valueOf(14));
        jedi.setCategories(new HashSet<>());

        BookDto gandhi = new BookDto();
        gandhi.setId(3L);
        gandhi.setTitle("Gandhi");
        gandhi.setAuthor("Wright");
        gandhi.setIsbn("13");
        gandhi.setPrice(BigDecimal.valueOf(15));
        gandhi.setCategories(new HashSet<>());

        List<BookDto> expectedBooks = List.of(jedi, gandhi);

        String url = "/api/books/search?titles=Jedi,Gandhi&authors=Lucas,"
                + "Wright&page=0&size=10&sort=id";

        MvcResult result = mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<BookDto> actualBooks = objectMapper
                .readValue(result
                        .getResponse()
                        .getContentAsByteArray(),
                        new TypeReference<>() {});

        assertNotNull(actualBooks);
        assertThat(actualBooks).isEqualTo(expectedBooks);
    }
}
